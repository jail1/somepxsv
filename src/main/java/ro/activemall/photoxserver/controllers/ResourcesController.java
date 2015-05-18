package ro.activemall.photoxserver.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ro.activemall.photoxserver.annotations.CurrentlyLoggedUser;
import ro.activemall.photoxserver.api.ApiUrls;
import ro.activemall.photoxserver.entities.Resource;
import ro.activemall.photoxserver.entities.ResourceTagName;
import ro.activemall.photoxserver.entities.User;
import ro.activemall.photoxserver.enums.RolesAsStrings;
import ro.activemall.photoxserver.json.PaginationJSON;
import ro.activemall.photoxserver.services.ResourcesService;

//WIP
@RestController
public class ResourcesController {
	@Autowired
	ResourcesService service;

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.SAVERESOURCE, method = RequestMethod.POST, produces = ApiUrls.JSON)
	public List<Resource> createOrUpdateResources(
			@RequestBody List<Resource> resource,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.createOrUpdateResources(resource, loggedinUser);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(method = RequestMethod.POST, value = ApiUrls.SAVECHILDRESOURCE)
	public Resource saveChildResource(@RequestBody Resource resource,
			@PathVariable Long attachToResourceId,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.saveChildResource(resource, attachToResourceId,
				loggedinUser);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(method = RequestMethod.POST, value = ApiUrls.CREATEROOTRESOURCE)
	public Resource createRootResource(@RequestBody Resource resource,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.createRootResource(resource, loggedinUser);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(method = RequestMethod.POST, value = ApiUrls.CLONERESOURCE)
	public Resource cloneResource(@PathVariable Long resourceId,
			@PathVariable Long targetUserId,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.cloneResource(resourceId, targetUserId, loggedinUser);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(method = RequestMethod.POST, value = ApiUrls.TRANSFERRESOURCE)
	public Resource transferResource(@PathVariable Long resourceId,
			@PathVariable Long newParentId,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.transferResource(resourceId, newParentId, loggedinUser);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.GETRESWITHEXTRAFIELDSET, method = RequestMethod.GET, produces = ApiUrls.JSON)
	public List<Resource> getResourcesWithExtraFieldSet(
			@CurrentlyLoggedUser User loggedinUser) {
		return service.getResourcesWithExtraFieldSet(loggedinUser);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.LISTRESOURCES, method = RequestMethod.GET, produces = ApiUrls.JSON)
	public List<Resource> findResourcesOwnedByAuthenticatedUser(
			@CurrentlyLoggedUser User loggedinUser) {
		return service.findResourcesOwnedByAuthenticatedUser(loggedinUser);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.LISTRESOURCES, method = RequestMethod.POST, produces = ApiUrls.JSON)
	public List<Resource> getResources(
			@RequestBody PaginationJSON paginationData,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.getResources(paginationData, loggedinUser);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.GETRESOURCEBYID, method = RequestMethod.GET, produces = ApiUrls.JSON)
	public Resource getResourceById(@PathVariable Long resourceId,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.getResourceById(resourceId, loggedinUser);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.DELETERESOURCE, method = RequestMethod.DELETE, consumes = ApiUrls.JSON, produces = ApiUrls.JSON)
	public Resource deleteResource(@RequestBody Resource resource,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.deleteResource(resource, loggedinUser);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.SEARCHRESOURCE, method = RequestMethod.POST, produces = ApiUrls.JSON)
	public List<Resource> searchResources(@RequestBody String term,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.searchResources(term, loggedinUser);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(method = RequestMethod.POST, value = ApiUrls.DECLARETAGTORESOURCE)
	public Resource declareTagToResource(@RequestBody ResourceTagName tag,
			@PathVariable Long attachToResourceId,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.declareTagToResource(tag, attachToResourceId,
				loggedinUser);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.DELETETAGFROMRESOURCE, method = RequestMethod.DELETE, consumes = ApiUrls.JSON, produces = ApiUrls.JSON)
	public Resource deleteTagFromResource(@RequestBody ResourceTagName tag,
			@PathVariable Long attachToResourceId,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.deleteTagFromResource(tag, attachToResourceId,
				loggedinUser);
	}
}
