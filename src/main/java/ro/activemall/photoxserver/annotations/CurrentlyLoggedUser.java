package ro.activemall.photoxserver.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;

/**
 * 
 * @author Badu
 *
 *         Annotation declared in order to catch the currently logged in user on
 *         controllers so we can pass it as an argument for methods in services
 *         that need to know how is performing operation
 */
@Target({ ElementType.PARAMETER, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal
public @interface CurrentlyLoggedUser {

}