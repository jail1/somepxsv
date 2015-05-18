package ro.activemall.photoxserver.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import ro.activemall.photoxserver.entities.User;
import ro.activemall.photoxserver.enums.UserActivityTypes;
import ro.activemall.photoxserver.events.UserActionEvent;

/**
 * 
 * @author Badu
 *
 *         In case of user logout, we have a handler similar to login event, so
 *         we can announce the logout over the websocket
 */
@Component
public class SecurityLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
	private static Logger log = Logger
			.getLogger(SecurityLogoutSuccessHandler.class);

	@Autowired
	ApplicationEventPublisher publisher;

	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		if (authentication != null) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
			User user = ((User) token.getPrincipal());
			UserActionEvent event = new UserActionEvent(user,
					UserActivityTypes.LOGOUT.getIntValue(), DateTime.now()
							.toString());
			publisher.publishEvent(event);
			log.info(user.getUsername() + " has logged out");
		} else {
			log.error("Authentication is null : can't say who logged out!");
		}
		super.onLogoutSuccess(request, response, authentication);
	}
}
