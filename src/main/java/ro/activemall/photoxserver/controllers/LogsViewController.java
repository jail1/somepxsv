package ro.activemall.photoxserver.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ro.activemall.photoxserver.api.ApiUrls;
import ro.activemall.photoxserver.entities.UserLogActivity;
import ro.activemall.photoxserver.enums.RolesAsStrings;
import ro.activemall.photoxserver.json.LogsCountPerUserJSON;
import ro.activemall.photoxserver.services.LogsViewService;

//WIP
@RestController
public class LogsViewController {
	@Autowired
	LogsViewService service;

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.GETLOGSFORUSER, method = RequestMethod.GET, produces = ApiUrls.JSON)
	public List<UserLogActivity> getLogsForUser(@PathVariable Long userId) {
		return service.getLogsForUser(userId);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.COUNTLOGS, method = RequestMethod.GET, produces = ApiUrls.JSON)
	public List<LogsCountPerUserJSON> getNumberOfLogs() {
		return service.getNumberOfLogs();
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.DELETELOGSFORUSER, method = RequestMethod.DELETE, produces = ApiUrls.JSON)
	public int deleteLogsForUser(@PathVariable Long userId) {
		return service.deleteLogsForUser(userId);
	}
}
