package ro.activemall.photoxserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ro.activemall.photoxserver.repositories.TemplatesRepository;
import ro.activemall.photoxserver.repositories.UsersRepository;

/**
 * 
 * @author Badu
 *
 *         This is a web controller (displays pages, which are templates)
 */
@Controller
public class HomeController {

	@Autowired
	TemplatesRepository repository;

	@Autowired
	UsersRepository usersRepository;

	@RequestMapping("/")
	public String home(ModelMap map) {
		return "index";
	}

	@RequestMapping("/public/API/v1/accountActivated")
	public String accountActivated(ModelMap map) {
		return "accountActivated";
	}

	// test URL:
	// http://localhost/public/API/v1/previewTemplate?name=Badu&templateId=1
	@RequestMapping("/public/API/v1/previewTemplate")
	public String helloTemplate(@RequestParam("userId") Long userId,
			@RequestParam("templateId") Long templateId, Model model) {
		model.addAttribute("templateId", templateId);
		model.addAttribute("user", usersRepository.findOne(userId));

		return "displayTemplate";// name of the html template
									// ("displayTemplate.html" from folder
									// "templates")
	}
}