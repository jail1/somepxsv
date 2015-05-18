package ro.activemall.photoxserver.services;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.support.ResourceBundleMessageSource;

import ro.activemall.photoxserver.entities.User;

/**
 * 
 * @author Badu
 *
 *         Parent of all services - so we won't have code that repeats itself
 */
public class ApplicationAbstractService {

	@Value("${application.default.server.language}")
	protected String defaultServerLanguage;

	@Autowired
	protected ResourceBundleMessageSource i18nMessage;

	@Autowired
	protected ApplicationEventPublisher publisher;

	public String getLocalizedMessage(String key, Object[] replacers, User auth) {
		return i18nMessage.getMessage(key, replacers,
				getAuthenticatedUserPreferedLanguage(auth));
	}

	public String getLocalizedMessage(String key, User auth) {
		return i18nMessage.getMessage(key, null,
				getAuthenticatedUserPreferedLanguage(auth));
	}

	public Locale getAuthenticatedUserPreferedLanguage(User auth) {
		final String[] args = auth == null ? defaultServerLanguage.split("_")
				: auth.getPreferedLanguage().split("_");
		return new Locale(args[0], args[1]);
	}

	public String getAuthenticatedUserFullName(User auth) {
		return auth == null ? "!!!Anonymous" : auth.getFirstName() + " "
				+ auth.getLastName();
	}

}
