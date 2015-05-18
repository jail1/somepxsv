package ro.activemall.photoxserver.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.activemall.photoxserver.entities.Template;
import ro.activemall.photoxserver.entities.User;
import ro.activemall.photoxserver.repositories.TemplatesRepository;

@Service
public class TemplatesService extends ApplicationAbstractService {
	private static Logger log = Logger.getLogger(TemplatesService.class);

	@Autowired
	TemplatesRepository repository;

	public void saveOrUpdateTemplate(String templateContent,
			User authenticatedUser) {
		// TODO : make validations and stuff
		Template template = new Template(templateContent);
		template.setOwner(authenticatedUser);
		template.setTitle("Template");// TODO : catch the title as well
		repository.save(template);
	}
}
