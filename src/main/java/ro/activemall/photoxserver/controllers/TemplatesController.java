package ro.activemall.photoxserver.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ro.activemall.photoxserver.annotations.CurrentlyLoggedUser;
import ro.activemall.photoxserver.api.ApiUrls;
import ro.activemall.photoxserver.entities.User;
import ro.activemall.photoxserver.services.TemplatesService;

//WIP
@RestController
public class TemplatesController {
	@Autowired
	TemplatesService service;

	@RequestMapping(value = ApiUrls.SAVETEMPLATE, method = RequestMethod.POST, produces = ApiUrls.JSON)
	@ResponseBody
	public void saveTemplate(@RequestBody String templateContent,
			@CurrentlyLoggedUser User authenticatedUser)
			throws UnsupportedEncodingException {
		service.saveOrUpdateTemplate(
				URLDecoder.decode(templateContent, "UTF-8"), authenticatedUser);
	}
}
