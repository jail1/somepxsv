package ro.activemall.photoxserver.resolvers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import ro.activemall.photoxserver.entities.Template;
import ro.activemall.photoxserver.repositories.TemplatesRepository;

import com.google.common.collect.Sets;

/**
 * 
 * @author Badu
 * 
 *         Thymeleaf doesn't work by default with database templates In order to
 *         allow customization of templates, we need to keep them inside
 *         database
 *
 */
public class DbTemplateResolver extends TemplateResolver {

	private final static String PREFIX = "db:";

	@Autowired
	TemplatesRepository repository;

	public DbTemplateResolver() {
		setResourceResolver(new DbResourceResolver());
		setResolvablePatterns(Sets.newHashSet(PREFIX + "*"));
		setCharacterEncoding("UTF-8");
	}

	@Override
	public void setPrefix(String prefix) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSuffix(String suffix) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected String computeResourceName(
			TemplateProcessingParameters templateProcessingParameters) {
		String templateName = templateProcessingParameters.getTemplateName();
		return templateName.substring(PREFIX.length());
	}

	private class DbResourceResolver implements IResourceResolver {

		@Override
		public InputStream getResourceAsStream(
				TemplateProcessingParameters templateProcessingParameters,
				String resourceName) {

			Template template = repository.findOne(Long.valueOf(resourceName));
			if (template != null) {
				return new ByteArrayInputStream(template.getContent()
						.getBytes());
			}

			return null;
		}

		@Override
		public String getName() {
			return "dbResourceResolver";
		}
	}
}
