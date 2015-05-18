package ro.activemall.photoxserver.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 * @author Badu
 * 
 *         Well, we handle HTTP Status 404 here...
 *
 */
public class RequestProcessingTimeInterceptor extends HandlerInterceptorAdapter {
	private static final Logger log = LoggerFactory
			.getLogger(RequestProcessingTimeInterceptor.class);
	protected String defaultServerLanguage = "en_US";

	/**
	 * public boolean preHandle(HttpServletRequest request, HttpServletResponse
	 * response, Object handler) throws Exception { //TODO : check if it works
	 * LocaleResolver localeResolver =
	 * RequestContextUtils.getLocaleResolver(request); if( localeResolver
	 * instanceof SessionLocaleResolver){
	 * log.info("localeResolver is  SessionLocaleResolver"); } if
	 * (localeResolver == null) { throw new IllegalStateException(
	 * "No LocaleResolver found: not in a DispatcherServlet request?"); }
	 * 
	 * if (request.getUserPrincipal() != null){
	 * log.info("Request is from a logged in user "
	 * +request.getUserPrincipal().getName()); }else{
	 * log.error("Request is anonymous"); }
	 * 
	 * Locale locale = new Locale("en", "US");
	 * 
	 * Authentication authentication =
	 * SecurityContextHolder.getContext().getAuthentication(); if
	 * (authentication != null){ User authenticatedUser = (User)
	 * authentication.getPrincipal(); if (authenticatedUser != null){ final
	 * String[] args = authenticatedUser.getPreferedLanguage().split("_");
	 * locale = new Locale(args[0], args[1]);
	 * log.info("Setting locale to "+authenticatedUser.getPreferedLanguage()); }
	 * }
	 * 
	 * localeResolver.setLocale(request, response, locale); return true; }
	 **/

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (response.getStatus() == HttpServletResponse.SC_NOT_FOUND) {
			// TODO : WIP
			String requestedURL = request.getRequestURL().toString();
			if (!requestedURL.contains("/error")) {
				log.info("404 requesting " + request.getRequestURL().toString());
			}
			if (request.getRequestURL().toString().contains(""/*
															 * service.
															 * storageFolderName
															 */)) {
				String[] fileNameParts = FilenameUtils.getBaseName(
						request.getRequestURL().toString()).split("_");
				if (fileNameParts.length == 2) {

				} else if (fileNameParts.length == 1) {

				}
			}
		} else {
			super.postHandle(request, response, handler, modelAndView);
		}
	}
}