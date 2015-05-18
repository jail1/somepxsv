package ro.activemall.photoxserver.security;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import ro.activemall.photoxserver.entities.User;
import ro.activemall.photoxserver.exceptions.PhotoxException;
import ro.activemall.photoxserver.services.UsersService;

//import org.springframework.boot.actuate.metrics.CounterService;
/**
 * 
 * @author Badu
 * 
 *         Since we've using login with email or username, we need to provide
 *         the authenticated user by searching it. Usually this is the class
 *         that provides Spring Security customization
 *
 */
@Component
public class UserDetailServiceImpl implements UserDetailsService {
	private static Logger log = Logger.getLogger(UserDetailServiceImpl.class);

	@Autowired
	UsersService service;

	// @Autowired
	// Environment env;

	@Autowired
	LoginAttemptService loginAttemptService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	ResourceBundleMessageSource i18nMessage;

	@Value("${application.default.server.language}")
	String defaultServerLanguage;

	Locale defaultLocale;

	@PostConstruct
	public void setupLocale() {
		final String[] args = defaultServerLanguage.split("_");
		defaultLocale = new Locale(args[0], args[1]);
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		String ip = request.getRemoteAddr();
		if (loginAttemptService.isBlocked(ip)) {
			throw new PhotoxException(i18nMessage.getMessage(
					"error.login.service.blocked", new Object[] { ip },
					defaultLocale));
		}
		try {
			User domainUser = service.findByUsernameOrLoginName(username);
			if (domainUser != null) {
				log.info("user " + domainUser.getUsername() + " doing login");
				if (!domainUser.isEnabled()) {
					log.warn("Account NOT enabled");
					throw new PhotoxException(i18nMessage.getMessage(
							"error.login.account.notenabled",
							new Object[] { username }, defaultLocale));
				}
				return domainUser;
			} else {
				log.error("'" + username + "' user not found!");
				throw new UsernameNotFoundException("User not found!");
			}
		} catch (Exception e) {
			log.error("EXCEPTION", e);
			throw new RuntimeException(e);
		}
	}
}