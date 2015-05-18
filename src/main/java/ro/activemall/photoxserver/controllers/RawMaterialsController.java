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
import ro.activemall.photoxserver.entities.RawMaterial;
import ro.activemall.photoxserver.entities.RawMaterialGroup;
import ro.activemall.photoxserver.enums.RolesAsStrings;
import ro.activemall.photoxserver.services.RawMaterialsService;

@RestController
public class RawMaterialsController {

	@Autowired
	RawMaterialsService service;

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.GETALLRAWGROUPS, method = RequestMethod.GET, produces = ApiUrls.JSON)
	public List<RawMaterialGroup> getAllMaterialsGrouped() {
		return service.getAllMaterialsGrouped();
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.SAVERAWGROUP, method = RequestMethod.POST, consumes = ApiUrls.JSON, produces = ApiUrls.JSON)
	public RawMaterial createOrUpdateRawMaterial(@PathVariable Long groupId,
			@RequestBody RawMaterial material) {
		return service.createOrUpdateRawMaterial(groupId, material);
	}

	// currently not used
	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.TRANSFERRAW, method = RequestMethod.POST, produces = ApiUrls.JSON)
	public ResponseEntity<String> transferRawMaterial(
			@PathVariable Long materialId, @PathVariable Long toGroupId) {
		service.transferRawMaterial(toGroupId, materialId);
		return new ResponseEntity<String>("", HttpStatus.OK);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.DELETERAW, method = RequestMethod.DELETE, consumes = ApiUrls.JSON, produces = ApiUrls.JSON)
	public ResponseEntity<String> deleteRawMaterial(@RequestBody Long materialId) {
		service.deleteRawMaterial(materialId);
		return new ResponseEntity<String>("", HttpStatus.OK);
	}

	// @See RawMaterialGroup @Size annotation for @Valid
	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.SAVERAW, method = RequestMethod.POST, consumes = ApiUrls.JSON, produces = ApiUrls.JSON)
	public RawMaterialGroup createOrUpdateRawMaterialGroup(
			@Valid @RequestBody RawMaterialGroup group) {
		return service.createOrUpdateRawMaterialGroup(group);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.DELETERAWGROUP, method = RequestMethod.DELETE, consumes = ApiUrls.JSON, produces = ApiUrls.JSON)
	public ResponseEntity<String> deleteRawMaterialGroup(
			@RequestBody Long groupId) {
		service.deleteRawMaterialGroup(groupId);
		return new ResponseEntity<String>("", HttpStatus.OK);
	}	
}