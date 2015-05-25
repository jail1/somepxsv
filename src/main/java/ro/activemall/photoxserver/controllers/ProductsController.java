package ro.activemall.photoxserver.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ro.activemall.photoxserver.api.ApiUrls;
import ro.activemall.photoxserver.entities.Product;
import ro.activemall.photoxserver.enums.RolesAsStrings;
import ro.activemall.photoxserver.json.ProductJSON;
import ro.activemall.photoxserver.services.ProductsService;

@RestController
public class ProductsController {
	
	@Autowired
	ProductsService service;

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.GETPRODUCTBYID, method = RequestMethod.GET, produces = ApiUrls.JSON)
	public Product getProductById(@PathVariable Long productId) {
		return service.getProductById(productId);
	}
	//TODO : make this cacheable - and eviscerate cache on products changes
	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN , RolesAsStrings.ROLE_PHOTOGRAPHER , RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.GETPRODUCTS, method = RequestMethod.GET, produces = ApiUrls.JSON)
	public List<ProductJSON> getProducts() {
		return service.getProducts();
	}
	
	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.GETPRODUCTSLIST, method = RequestMethod.GET, produces = ApiUrls.JSON)
	public List<Product> getAllProducts() {
		return service.getAllProducts();
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.GETUNLISTEDPRODUCTS, method = RequestMethod.GET, produces = ApiUrls.JSON)
	public List<Product> getAllUnlistedProducts(
			@PathVariable Long exceptProductId) {
		return service.getAllUnlistedProducts(exceptProductId);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.SAVEPRODUCT, method = RequestMethod.POST, produces = ApiUrls.JSON)
	public Product createOrUpdateProduct(@RequestBody @Valid Product product) {
		return service.createOrUpdateProduct(product);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.ADDRAWGROUPTOPRODUCT, method = RequestMethod.POST, produces = ApiUrls.JSON)
	public ResponseEntity<String> addRawGroupToProduct(
			@PathVariable Long productId, @PathVariable Long toGroupId) {
		service.addRawGroupToProduct(productId, toGroupId);
		return new ResponseEntity<String>("", HttpStatus.OK);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.REMOVERAWGROUPFROMPROD, method = RequestMethod.DELETE, produces = ApiUrls.JSON)
	public ResponseEntity<String> removeRawGroupFromProduct(
			@PathVariable Long productId, @PathVariable Long theGroupId) {
		service.removeRawGroupFromProduct(productId, theGroupId);
		return new ResponseEntity<String>("", HttpStatus.OK);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.ADDPRODUCTCHILD, method = RequestMethod.POST, produces = ApiUrls.JSON)
	public ResponseEntity<String> addChildToProduct(
			@PathVariable Long productId, @PathVariable Long childProductId) {
		service.addChildToProduct(productId, childProductId);
		return new ResponseEntity<String>("", HttpStatus.OK);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.REMOVEPARENTOFPRODUCT, method = RequestMethod.POST, produces = ApiUrls.JSON)
	public ResponseEntity<String> removeParentOfProduct(@PathVariable Long productId) {
		service.removeParentOfProduct(productId);
		return new ResponseEntity<String>("", HttpStatus.OK);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.CLONEPRODUCT, method = RequestMethod.POST, consumes=ApiUrls.JSON, produces = ApiUrls.JSON)
	public Product cloneProduct(@PathVariable Long productId, @RequestBody String[] data) {
		return service.cloneProduct(productId, data);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.DELETEPRODUCT, method = RequestMethod.DELETE, produces = ApiUrls.JSON)
	public ResponseEntity<String> deleteProduct(@RequestBody Long productId) {
		service.deleteProduct(productId);
		return new ResponseEntity<String>("", HttpStatus.OK);
	}
	
	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.STORESELLINGPRICE, method = RequestMethod.POST, consumes = ApiUrls.JSON, produces = ApiUrls.JSON)
	public ResponseEntity<String> storeSellingPrice(
			@PathVariable Long productId, @PathVariable Long materialId, @RequestBody Double sellingPrice) {
		service.storeSellingPrice(productId, materialId, sellingPrice);
		return new ResponseEntity<String>("", HttpStatus.OK);
	}
	
	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.STOREMINQUANTITY, method = RequestMethod.POST, consumes = ApiUrls.JSON, produces = ApiUrls.JSON)
	public ResponseEntity<String> storeMinQuantity(@PathVariable Long productId, @RequestBody int quantity){
		service.storeMinQuantity(productId, quantity);
		return new ResponseEntity<String>("", HttpStatus.OK);
	}
}