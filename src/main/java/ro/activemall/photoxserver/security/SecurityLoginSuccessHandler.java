package ro.activemall.photoxserver.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import ro.activemall.photoxserver.entities.User;
import ro.activemall.photoxserver.enums.UserActivityTypes;
import ro.activemall.photoxserver.enums.UserRoles;
import ro.activemall.photoxserver.events.UserActionEvent;
//import ro.activemall.photoxserver.json.admin.JodaDateTimeObjectMapper;
import ro.activemall.photoxserver.services.UsersService;

/**
 * 
 * @author Badu
 * 
 *         In case of login success, the application uses this handler
 *
 */
@Component
public class SecurityLoginSuccessHandler implements
		AuthenticationSuccessHandler {
	private static Logger log = Logger
			.getLogger(SecurityLoginSuccessHandler.class);

	RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Autowired
	UsersService service;

	@Autowired
	ApplicationEventPublisher publisher;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		User authenticatedUser = null;
		if (authentication.getPrincipal() instanceof User) {
			authenticatedUser = (User) authentication.getPrincipal();
			// we are announcing user's login via websocket. It is handled
			// inside Angular, according to account type
			UserActionEvent event = new UserActionEvent(authenticatedUser,
					UserActivityTypes.LOGIN.getIntValue(), DateTime.now()
							.toString());
			publisher.publishEvent(event);
			// in case the user was created outside our system, we need to fix
			// the user role (usually it's a customer)
			if (authenticatedUser.getRole() == null) {
				service.fixUserWithNoRole(authenticatedUser, false);
			}
			service.saveUsersLastSeenDate(authenticatedUser);
		} else {
			log.error("Authentication NOT instanceof User");
		}
		handle(request, response, authentication);
		clearAuthenticationAttributes(request);

	}

	protected void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

	protected void handle(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException {

		String redirectUrl = "/login";
		final User authenticatedUser = (User) authentication.getPrincipal();
		if (authentication.getPrincipal() instanceof User) {
			if (authenticatedUser.getRole().getRole() == UserRoles.ROLE_PHOTOGRAPHERS_CLIENT
					.getIntValue()) {
				// TODO : get rid of the user=userId and make an API REST which
				// gets called by Angular apps when they start
				// in order to obtain user profile and other data
				redirectUrl = "/customer.html";
			} else if (authenticatedUser.getRole().getRole() == UserRoles.ROLE_PHOTOGRAPHER
					.getIntValue()) {
				redirectUrl = "/agency.html";
			} else if (authenticatedUser.getRole().getRole() == UserRoles.ROLE_SUPER_ADMIN
					.getIntValue()) {
				redirectUrl = "/admin.html";
			}
		}
		if (response.isCommitted()) {
			log.info("Response has already been committed. Unable to redirect to /home");
			return;
		}
		// log.info("[SecurityLoginSuccessHandler] -> "+redirectUrl);
		redirectStrategy.sendRedirect(request, response, redirectUrl);
	}

}
