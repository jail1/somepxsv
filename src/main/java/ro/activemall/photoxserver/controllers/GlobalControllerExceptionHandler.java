package ro.activemall.photoxserver.controllers;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.thymeleaf.exceptions.TemplateInputException;

import ro.activemall.photoxserver.entities.User;
import ro.activemall.photoxserver.exceptions.PhotoxException;

/**
 * 
 * @author Badu
 * 
 *         A ControllerAdvice class to handle various http statuses
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static Logger log = Logger.getLogger(GlobalControllerExceptionHandler.class);

	@Autowired
	MessageSource messageSource;

	public GlobalControllerExceptionHandler() {
		super();
	}

	@PostConstruct
	public void setup() {
		// log.info("================================");
		// log.info("ExceptionHandlerController SETUP");
		// log.info("================================");
	}

	public User getAuthenticatedUser() {
		User result = null;
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext != null) {
			Authentication authentication = securityContext.getAuthentication();
			if (authentication != null) {
				if (authentication.getPrincipal() instanceof User) {
					result = (User) authentication.getPrincipal();
				}
			} else {
				log.warn("[getAuthenticatedUser()] : NOT AUTHENTICATED.");
			}
		} else {
			log.warn("[getAuthenticatedUser()] : SECURITY CONTEXT IS NULL.");
		}
		if (result == null) {
			log.warn("[getAuthenticatedUser()] : authenticatedPrincipal is NULL");
		}
		return result;
	}

	// Commented as was declared below with @Override
	// Handles @Valid annotations across application
	/**
	 * @ExceptionHandler
	 * @ResponseBody
	 * @ResponseStatus(value = HttpStatus.BAD_REQUEST) public ExamoException
	 *                       handleException( MethodArgumentNotValidException
	 *                       exception) { User auth = getAuthenticatedUser();
	 *                       Locale currentLocale; if (auth != null) { String[]
	 *                       args = auth.getPreferedLanguage().split("_");
	 *                       currentLocale = new Locale(args[0], args[1]); }
	 *                       else {
	 *                       log.info("Authenticated user is null. Anonymous request?"
	 *                       ); currentLocale = new Locale("en", "US"); }
	 *                       BindingResult result =
	 *                       exception.getBindingResult(); List<FieldError>
	 *                       fieldErrors = result.getFieldErrors(); String
	 *                       localizedErrorMessage = ""; for (FieldError
	 *                       fieldError : fieldErrors) { // E.G. //
	 *                       NotEmpty.category.name=Category name cannot be
	 *                       empty. // Length.category.name=The maximum length
	 *                       of category name is {1} // characters. // where
	 *                       Category class has // @NotEmpty // @Length(max =
	 *                       254) // above property named 'name'
	 *                       log.error("GLOBAL VALIDATION ERROR : " +
	 *                       messageSource.getMessage(fieldError,
	 *                       currentLocale)); localizedErrorMessage +=
	 *                       messageSource.getMessage(fieldError, currentLocale)
	 *                       + (localizedErrorMessage.equals("") ? "" : "\n"); }
	 *                       return new ExamoException(localizedErrorMessage); }
	 **/
	@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation")
	// HTTP Status 409
	@ExceptionHandler(DataIntegrityViolationException.class)
	public void conflict() {
		// Nothing to do
		// TODO : do something
	}

	// Specify the name of a specific view that will be used to display the
	// error:
	@ExceptionHandler({ SQLException.class, DataAccessException.class })
	public String databaseError() {
		// Nothing to do. Returns the logical view name of an error page, passed
		// to
		// the view-resolver(s) in usual way.
		// Note that the exception is _not_ available to this view (it is not
		// added to
		// the model) but see "Extending ExceptionHandlerExceptionResolver"
		// below.
		// TODO : do something
		return "databaseError";
	}

	@ExceptionHandler(PhotoxException.class)
	public PhotoxException handleExamoException(PhotoxException ex) {
		return ex;
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public UsernameNotFoundException handleUsernameNotFoundException(
			UsernameNotFoundException ex) {
		return ex;
	}

	@ExceptionHandler(TemplateInputException.class)
	public PhotoxException handleTemplateInputException(TemplateInputException ex) {
		return new PhotoxException(ex.getMessage());
	}	
	
	/**
	 * TODO : check it out from
	 * http://www.baeldung.com/2013/01/31/exception-handling
	 * -for-rest-with-spring-3-2/
	 */
	// API

	// HTTP Status 400

	@ExceptionHandler({ ConstraintViolationException.class })	
	//Declaring ResponseBody means that the message will be converted to JSON
	//we're using a trick here, by having the messages defined as keys for angular 
	//@see User entity, _InvalidUsername_ and _InvalidPassword_ will get translated into angular
	//for each language
	@ResponseBody
	public PhotoxException handleBadRequest( final ConstraintViolationException ex, final WebRequest request) {
		PhotoxException phex = null;
		for (ConstraintViolation constraint : ex.getConstraintViolations()){
			//log.info("Message : "+constraint.getMessage());
			phex = new PhotoxException(constraint.getMessage());
		}
        return phex;
	}

	// Commented as it's declared above
	/**
	 * @ExceptionHandler( { DataIntegrityViolationException.class }) public
	 *                    ResponseEntity<Object> handleBadRequest( final
	 *                    DataIntegrityViolationException ex, final WebRequest
	 *                    request) { final String bodyOfResponse =
	 *                    "This should be application specific"; return
	 *                    handleExceptionInternal(ex, bodyOfResponse, new
	 *                    HttpHeaders(), HttpStatus.BAD_REQUEST, request); }
	 **/
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			final HttpMessageNotReadableException ex,
			final HttpHeaders headers, final HttpStatus status,
			final WebRequest request) {
		final String bodyOfResponse = "This should be application specific";
		// ex.getCause() instanceof JsonMappingException, JsonParseException //
		// for additional information later on
		return handleExceptionInternal(ex, bodyOfResponse, headers,
				HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			final MethodArgumentNotValidException exception,
			final HttpHeaders headers, final HttpStatus status,
			final WebRequest request) {
		User auth = getAuthenticatedUser();
		Locale currentLocale;
		if (auth != null) {
			String[] args = auth.getPreferedLanguage().split("_");
			currentLocale = new Locale(args[0], args[1]);
		} else {
			log.info("Authenticated user is null. Anonymous request?");
			currentLocale = new Locale("en", "US");
		}
		BindingResult result = exception.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		String localizedErrorMessage = "";
		for (FieldError fieldError : fieldErrors) {
			// E.G.
			// NotEmpty.category.name=Category name cannot be empty.
			// Length.category.name=The maximum length of category name is {1}
			// characters.
			// where Category class has
			// @NotEmpty
			// @Length(max = 254)
			// above property named 'name'
			log.error("GLOBAL VALIDATION ERROR : "
					+ messageSource.getMessage(fieldError, currentLocale));
			localizedErrorMessage += messageSource.getMessage(fieldError,
					currentLocale)
					+ (localizedErrorMessage.equals("") ? "" : "\n");
		}

		return handleExceptionInternal(null, new PhotoxException(localizedErrorMessage), headers, HttpStatus.OK, request);
	}

	// HTTP Status 403

	// HTTP Status 404

	@ExceptionHandler(value = { EntityNotFoundException.class })
	// , MyResourceNotFoundException.class
	protected ResponseEntity<Object> handleNotFound(final RuntimeException ex,
			final WebRequest request) {
		final String bodyOfResponse = "[403-404] This should be application specific";
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(),
				HttpStatus.NOT_FOUND, request);
	}

	// HTTP Status 409
	// commented as it was declared above
	/**
	 * @ExceptionHandler( { InvalidDataAccessApiUsageException.class,
	 *                    DataAccessException.class }) protected
	 *                    ResponseEntity<Object> handleConflict(final
	 *                    RuntimeException ex, final WebRequest request) { final
	 *                    String bodyOfResponse =
	 *                    "This should be application specific"; return
	 *                    handleExceptionInternal(ex, bodyOfResponse, new
	 *                    HttpHeaders(), HttpStatus.CONFLICT, request); }
	 **/
	// 412

	// HTTP Status 500
	@ExceptionHandler({ NullPointerException.class,
			IllegalArgumentException.class, IllegalStateException.class })
	public ResponseEntity<Object> handleInternal(final RuntimeException ex,
			final WebRequest request) {
		logger.error("500 Status Code", ex);
		final String bodyOfResponse = "[500] This should be application specific";
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(),
				HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

}
