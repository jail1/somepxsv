package ro.activemall.photoxserver.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ro.activemall.photoxserver.api.ApiUrls;
import ro.activemall.photoxserver.entities.PlatformSetting;
import ro.activemall.photoxserver.enums.RolesAsStrings;
import ro.activemall.photoxserver.services.PlatformSettingsService;

@RestController
public class PlatformSettingsController {
	@Autowired
	PlatformSettingsService service;

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.SAVESETTING, method = RequestMethod.POST, consumes = ApiUrls.JSON, produces = ApiUrls.JSON)
	public PlatformSetting createOrUpdateSetting(
			@Valid @RequestBody PlatformSetting setting) {
		return service.createOrUpdateSetting(setting);
	}
}
