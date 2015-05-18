package ro.activemall.photoxserver.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.activemall.photoxserver.entities.Product;
import ro.activemall.photoxserver.entities.RawMaterial;
import ro.activemall.photoxserver.entities.RawMaterialGroup;
import ro.activemall.photoxserver.repositories.ProductsRepository;
import ro.activemall.photoxserver.repositories.RawMaterialGroupsRepository;
import ro.activemall.photoxserver.repositories.RawMaterialsRepository;

@Service
public class RawMaterialsService extends ApplicationAbstractService {

	private static Logger log = Logger.getLogger(RawMaterialsService.class);

	@PersistenceContext
	EntityManager entityManager;

	// TODO : replace @Autowired here with @repository (remember that you've
	// tried this before, and it didn't worked)
	@Autowired
	RawMaterialGroupsRepository groupsRepository;

	@Autowired
	RawMaterialsRepository repository;

	@Autowired
	ProductsRepository productsRepository;

	@Transactional
	public RawMaterialGroup createOrUpdateRawMaterialGroup(RawMaterialGroup group) {
		RawMaterialGroup result;
		if (group.getId() == null || group.getId() == 0)
		{
			result = groupsRepository.save(group);
			// TODO : validate - name unique, not null, not empty
			if (group.getIsRequiredInEveryProduct()) {
				// add it to every defined product
				List<Product> allProducts = productsRepository.findAll();
				for(Product product : allProducts)
				{
					String sqlQuery = "INSERT INTO photox_products_selectors (product_id, rawmaterialgroup_id) VALUES(:productId, :rawGroupId)";
					Query query = entityManager.createNativeQuery(sqlQuery);
					query.setParameter("productId", product.getId());
					query.setParameter("rawGroupId", result.getId());
					query.executeUpdate();
				}
			}
		}else{
			result = groupsRepository.save(group);
		}
		return result;
	}

	@Transactional
	public RawMaterial createOrUpdateRawMaterial(Long groupId, RawMaterial rawmaterial) {
		// TODO : validate before save - name+price not null, not empty
		RawMaterialGroup parentGroup = groupsRepository.findOne(groupId);
		rawmaterial.setParentGroup(parentGroup);
		repository.save(rawmaterial);
		return rawmaterial;
	}

	@Transactional
	public void transferRawMaterial(Long toGroupId, Long materialId) {
		RawMaterial targetMaterial = repository.findOne(materialId);
		RawMaterialGroup parentGroup = groupsRepository.findOne(toGroupId);
		targetMaterial.setParentGroup(parentGroup);
		repository.save(targetMaterial);
		log.info("raw material transfered");
	}	
	
	@Transactional
	public void deleteRawMaterial(Long materialId) {	
		String sqlQuery = "DELETE FROM photox_rawmaterials WHERE id = :materialId";
		Query query = entityManager.createNativeQuery(sqlQuery);
		query.setParameter("materialId", materialId);		 
		int result = query.executeUpdate();		 
		if (result > 0) {
		    log.info("Raw material was removed");
		}else{
			log.error("ERROR REMOVING RAW MATERIAL");
		}
	}

	@Transactional
	public void deleteRawMaterialGroup(Long groupId) {
		RawMaterialGroup group = groupsRepository.findOne(groupId);
		groupsRepository.delete(group);
	}

	public List<RawMaterialGroup> getAllMaterialsGrouped() {
		return groupsRepository.findAll();
	}
}
