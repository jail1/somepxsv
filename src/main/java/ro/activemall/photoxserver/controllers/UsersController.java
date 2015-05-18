package ro.activemall.photoxserver.controllers;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ro.activemall.photoxserver.annotations.CurrentlyLoggedUser;
import ro.activemall.photoxserver.api.ApiUrls;
import ro.activemall.photoxserver.entities.HierarchicalUser;
import ro.activemall.photoxserver.entities.PlatformSetting;
import ro.activemall.photoxserver.entities.User;
import ro.activemall.photoxserver.enums.RolesAsStrings;
import ro.activemall.photoxserver.json.JodaDateTimeObjectMapper;
import ro.activemall.photoxserver.json.PaginationJSON;
import ro.activemall.photoxserver.json.UserAndRoleJSON;
import ro.activemall.photoxserver.json.UserPreferencesDataJSON;
import ro.activemall.photoxserver.repositories.PlatformSettingsRepository;
import ro.activemall.photoxserver.services.UsersService;

@RestController
public class UsersController {

	@Autowired
	UsersService service;

	@Autowired
	PlatformSettingsRepository settingsRepository;

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER, RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.LOGGEDINUSER_JS, method = RequestMethod.GET, produces = "application/javascript;charset=UTF-8")
	public String authenticatedPrincipal(@CurrentlyLoggedUser User authenticated) {
		JodaDateTimeObjectMapper mapper = new JodaDateTimeObjectMapper();
		String userAsJson = "null";
		try {
			userAsJson = mapper.writeValueAsString(authenticated);
		} catch (Exception ex) {
			System.out.println(ex.getStackTrace());
		}
		String result = "var loggedinUser = " + userAsJson
				+ ";\nvar serverSettings = {";
		List<PlatformSetting> settings = settingsRepository.findAll();
		for (PlatformSetting setting : settings) {
			result += setting.getKey() + ":" + setting.getEffectiveValue()
					+ ",";
		}
		result += "};";
		//System.out.println(result);
		return result;
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.GETUSERBYID, method = RequestMethod.GET, produces = ApiUrls.JSON)
	public User getUser(@PathVariable Long userId,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.getUser(userId);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER })
	@RequestMapping(value = ApiUrls.INVITEACCOUNT, method = RequestMethod.POST, produces = ApiUrls.JSON)
	public User createAccount(@RequestBody String email, @CurrentlyLoggedUser User loggedinUser) {
		return service.createAccountOnlyWithEmail(email);
	}
	
	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN})
	@RequestMapping(value = ApiUrls.CREATEACCOUNT, method = RequestMethod.POST, produces = ApiUrls.JSON)
	public User createAccount(@RequestBody User user, @CurrentlyLoggedUser User loggedinUser) {
		return service.createAccount(user, loggedinUser);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.UPDATEACCOUNT, method = RequestMethod.POST, consumes = ApiUrls.JSON, produces = ApiUrls.JSON)
	public User updateUser(@RequestBody @Valid User user,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.updateUser(user, loggedinUser);
	}

	@Secured({RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER, RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT})
	@RequestMapping(value = ApiUrls.UPDATEPROFILE, method = RequestMethod.POST, consumes = ApiUrls.JSON, produces = ApiUrls.JSON)
	@ResponseBody
	public User updateUserProfile(@RequestBody @Valid User user,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.updateUserProfile(user, loggedinUser);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.SETROLEFORUSER, method = RequestMethod.POST, consumes = ApiUrls.JSON, produces = ApiUrls.JSON)
	public User setRoleForUser(@RequestBody UserAndRoleJSON user,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.setRoleForUser(user, loggedinUser);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.DELETEACCOUNT, method = RequestMethod.DELETE, consumes = ApiUrls.JSON, produces = ApiUrls.JSON)
	public User deleteUser(@RequestBody User user,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.deleteUser(user);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER })
	@RequestMapping(value = ApiUrls.EMAILUNIQUE, method = RequestMethod.POST, produces = ApiUrls.JSON)
	public String emailCheck(@RequestBody String email,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.emailCheck(email);
	}
	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER })
	@RequestMapping(value = ApiUrls.LOGINUNIQUE, method = RequestMethod.POST, produces = ApiUrls.JSON)
	public String loginCheck(@RequestBody String loginname,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.loginCheck(loginname);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.GETUSERSPAGED, method = RequestMethod.POST, produces = ApiUrls.JSON, consumes = ApiUrls.JSON)
	public List<User> getUsersPagedAndOrdered(
			@RequestBody PaginationJSON paginationData,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.getUsersPagedAndOrdered(paginationData);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.GETALLUSERS, method = RequestMethod.GET, produces = ApiUrls.JSON)
	public List<User> getAllUsers(@CurrentlyLoggedUser User loggedinUser) {
		return service.getAllUsers();
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.SEARCHUSERS, method = RequestMethod.GET, produces = ApiUrls.JSON)
	public List<User> search(@RequestParam(value = "fragment") String fragment,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.search(fragment);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.SETLANGUAGEFORUSER, method = RequestMethod.POST, consumes = ApiUrls.JSON, produces = ApiUrls.JSON)
	public String setLanguageForLoggedInUser(@RequestBody String lang,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.setLanguageForLoggedInUser(lang, loggedinUser);
	}

	@RequestMapping(value = ApiUrls.ACTIVATEACCOUNT, method = RequestMethod.GET, produces = ApiUrls.JSON)
	public String activateAccount(@PathVariable Long userId,
			@PathVariable String token) {
		return service.activateAccount(token, userId);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.SAVEUSERSPREFERENCES, method = RequestMethod.POST, consumes = ApiUrls.JSON, produces = ApiUrls.JSON)
	private UserPreferencesDataJSON saveUserPreference(
			UserPreferencesDataJSON data, @CurrentlyLoggedUser User loggedinUser) {
		return service.saveUserPreference(loggedinUser, data);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER })
	@RequestMapping(value = ApiUrls.GETREFEREELIST, method = RequestMethod.GET, produces = ApiUrls.JSON)
	public Set<HierarchicalUser> getRefereeList(@CurrentlyLoggedUser User loggedinUser) {
		return service.getRefereeList(loggedinUser);
	}
	//TEMPORARY - TO ALLOW ALL SORT OF TESTS
	//TODO : remove access to this method
	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN })
	@RequestMapping(value = ApiUrls.SETPARENTOFUSER, method = RequestMethod.POST, produces = ApiUrls.JSON)
	public ResponseEntity<String> setParentOfUser(@PathVariable Long childUserId, @PathVariable Long parentUserId){
		service.setParentOfUser(childUserId, parentUserId);
		return new ResponseEntity<String>("", HttpStatus.OK);
	}
	
}
