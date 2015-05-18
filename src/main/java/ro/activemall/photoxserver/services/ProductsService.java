package ro.activemall.photoxserver.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ro.activemall.photoxserver.entities.Product;
import ro.activemall.photoxserver.entities.ProductPrice;
import ro.activemall.photoxserver.entities.RawMaterial;
import ro.activemall.photoxserver.entities.RawMaterialGroup;
import ro.activemall.photoxserver.json.ProductJSON;
import ro.activemall.photoxserver.repositories.ProductPricesRepository;
import ro.activemall.photoxserver.repositories.ProductsRepository;
import ro.activemall.photoxserver.repositories.RawMaterialGroupsRepository;
import ro.activemall.photoxserver.repositories.RawMaterialsRepository;

@Service
public class ProductsService extends ApplicationAbstractService {

	private static Logger log = Logger.getLogger(ProductsService.class);

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	ProductsRepository repository;

	@Autowired
	ProductPricesRepository pricesRepository;

	@Autowired
	RawMaterialsRepository materialsRepository;

	@Autowired
	RawMaterialGroupsRepository groupsRepository;

	public Product getProductById(Long productId) {
		Product result = repository.findOne(productId);
		//log.info(result.toString());
		return result;
	}

	public List<Product> getAllProducts() {
		return repository.findAll(sortByComplexity());
	}

	public Sort sortByComplexity(){
		return new Sort(Sort.Direction.DESC, "isComplex");
	}

	public List<Product> getAllUnlistedProducts(Long exceptProductId) {
		return repository.getAllUnlistedProducts(exceptProductId);
	}
	@Transactional
	public Product createOrUpdateProduct(Product product) {
		Product result;
		if (product.getId() != null && product.getId() > 0) {
			// to preserve children and selectors
			result = repository.findOne(product.getId());
			log.info(product.getIsListable());
			result.setListable(product.getIsListable());
			result.setName(product.getName());
			result.setDescription(product.getDescription());
			result.setExtraData(product.getExtraData());
			result.setRatio(product.getRatio());
			result.setLostRatio(product.getLostRatio());
			result.setMinQuantity(product.getMinQuantity());
			result = repository.save(result);
		} else {
			result = repository.save(product);
			// if the product is fresh, always required material groups should be added automatically
			List<RawMaterialGroup> requiredGroups = groupsRepository.findRequiredInEveryProductMaterials();
			for (RawMaterialGroup group : requiredGroups) {
				String sqlQuery = "INSERT INTO photox_products_selectors (product_id, rawmaterialgroup_id) VALUES(:productId, :rawGroupId)";
				Query query = entityManager.createNativeQuery(sqlQuery);
				query.setParameter("productId", result.getId());
				query.setParameter("rawGroupId", group.getId());
				query.executeUpdate();
			}
		}
		return result;
	}

	@Transactional
	public Product cloneProduct(Long productId, String[] data) {
		ProductPrice priceToAdd;
		Set<ProductPrice> productPrices = new HashSet<ProductPrice>();
		Product target = repository.findOne(productId);
		String newName = data[0];
		Product clonedProduct = target.cloneForProduct();
		clonedProduct.setName(newName);

		String newWidth = data[1];
		String newHeight = data[2];
		clonedProduct.setExtraData("{\"width\":"+newWidth+",\"height\":"+newHeight+"}");
		clonedProduct.setRatio(Double.valueOf(Double.valueOf(newWidth) * Double.valueOf(newHeight) / 1000000));

		// save the product, then the children
		clonedProduct = repository.save(clonedProduct);
		//log.info("Clone saved "+clonedProduct.getId());
		if (clonedProduct.getIsComplex()) {		
			List<Product> savedChildren = repository.save(clonedProduct.getChildren());
			for (Product child : savedChildren){
				//log.info("Clone's child "+child.getId());
				addChildToProduct(clonedProduct.getId(), child.getId());
				if (child.getSelectors() != null){
					for (RawMaterialGroup group : child.getSelectors()) {					
						addRawGroupToProduct(child.getId(), group.getId());					
					}
				}
				if (child.getPrices() != null){
					//log.info("Clone's child selectors saved");				
					for (ProductPrice price : child.getPrices()){
						priceToAdd = new ProductPrice();
						priceToAdd.setMaterial(price.getMaterial());
						priceToAdd.setProduct(child);
						priceToAdd.setSellingPrice(price.getSellingPrice());
						productPrices.add(priceToAdd);
					}			
					pricesRepository.save(productPrices);
				}
				//log.info("Clone's child prices saved");				
			}
			//log.info("===Clone's children saved===");
		} else if (target.getSelectors() != null) {			
			for (RawMaterialGroup group : target.getSelectors()) {				
				addRawGroupToProduct(clonedProduct.getId(), group.getId());
			}
			if (target.getPrices()!=null){
				//log.info("Clone's selectors saved");			
				for (ProductPrice price : target.getPrices()){
					priceToAdd = new ProductPrice();
					priceToAdd.setMaterial(price.getMaterial());
					priceToAdd.setProduct(clonedProduct);
					priceToAdd.setSellingPrice(price.getSellingPrice());
					productPrices.add(priceToAdd);
				}			
				pricesRepository.save(productPrices);
			}
			//log.info("Clone's prices saved");
		}
		//log.info(clonedProduct.toString());
		return clonedProduct;

	}

	@Transactional
	public void addRawGroupToProduct(Long productId, Long groupId) {
		String sqlQuery = "INSERT INTO photox_products_selectors (product_id, rawmaterialgroup_id) VALUES(:productId, :rawGroupId)";
		Query query = entityManager.createNativeQuery(sqlQuery);
		query.setParameter("productId", productId);
		query.setParameter("rawGroupId", groupId);
		query.executeUpdate();
		log.info("Adding " + groupId + " to product " + productId);
	}

	@Transactional
	public void removeRawGroupFromProduct(Long productId, Long groupId) {
		try {
			String sqlQuery = "DELETE FROM photox_products_selectors WHERE product_id = :productId AND rawmaterialgroup_id = :rawGroupId";
			Query query = entityManager.createNativeQuery(sqlQuery);
			query.setParameter("productId", productId);
			query.setParameter("rawGroupId", groupId);
			query.executeUpdate();
		} catch (Exception ex) {
			log.error("Exception", ex);
		}
	}

	public void deleteProduct(Long productId) {
		Product product = repository.findOne(productId);
		repository.delete(product);
	}

	@Transactional
	public void removeParentOfProduct(Long childId) {
		String sqlQuery = "UPDATE photox_products SET parent_id = NULL WHERE id = :childId";
		Query query = entityManager.createNativeQuery(sqlQuery);		
		query.setParameter("childId", childId);
		query.executeUpdate();
	}

	@Transactional
	public void addChildToProduct(Long productId, Long childId) {
		String sqlQuery = "UPDATE photox_products SET parent_id = :productId WHERE id = :childId";
		Query query = entityManager.createNativeQuery(sqlQuery);
		query.setParameter("productId", productId);
		query.setParameter("childId", childId);
		query.executeUpdate();
	}

	@Transactional
	public void storeSellingPrice(Long productId, Long materialId, Double sellingPrice) {
		ProductPrice productPrice = pricesRepository.findPriceForProductAndMaterial(productId, materialId);						
		if (productPrice == null){
			Product targetProduct = repository.findOne(productId);
			RawMaterial targetMaterial = materialsRepository.findOne(materialId);
			productPrice  = new ProductPrice();
			productPrice.setProduct(targetProduct);
			productPrice.setMaterial(targetMaterial);
			productPrice.setSellingPrice(sellingPrice);
		}else{
			productPrice.setSellingPrice(sellingPrice);
		}
		pricesRepository.save(productPrice);
		log.info("Selling price per selector save = " + sellingPrice);
	}


	public void storeMinQuantity(Long productId, int quantity) {
		Product product = repository.findOne(productId);
		product.setMinQuantity(quantity);
		log.info("Min quantity of "+product.getName()+" = " + quantity);
		repository.save(product);
	}

	public List<ProductJSON> getProducts(){
		List<Product> listedProducts = repository.getListedProducts();
		List<ProductJSON> result = new ArrayList<ProductJSON>();
		for (Product product : listedProducts){			
			ProductJSON endProduct = new ProductJSON(product);
			result.add(endProduct);
		}
		return result;
	}
}
