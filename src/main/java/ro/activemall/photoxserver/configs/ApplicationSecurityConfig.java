package ro.activemall.photoxserver.configs;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ro.activemall.photoxserver.enums.RolesAsStrings;
import ro.activemall.photoxserver.security.SecurityLoginSuccessHandler;
import ro.activemall.photoxserver.security.SecurityLogoutSuccessHandler;
import ro.activemall.photoxserver.security.UserDetailServiceImpl;

/**
 * 
 * @author Badu
 * 
 *         Configuration for server security
 *
 */
@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

	private static Logger log = Logger
			.getLogger(ApplicationSecurityConfig.class);

	@Autowired
	UserDetailServiceImpl userDetailsService;

	@Autowired
	SecurityLogoutSuccessHandler logoutSuccessHandler;

	@Autowired
	SecurityLoginSuccessHandler loginSuccessHandler;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(
				passwordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// these endpoints will be ignored by the web security
		web.ignoring().antMatchers("/public/**", "/images/**", "/css/**",
				"/fonts/**", "/js/**", "/*.html");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				// any matchers of the private REST API will be secured (the
				// rest API sould be secured by itself, just in case
				// someone ignores the REST declaration convention)
				.antMatchers("/private/**")
				.hasAnyAuthority(RolesAsStrings.ROLE_SUPER_ADMIN,
						RolesAsStrings.ROLE_PHOTOGRAPHER,
						RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT)
				.antMatchers("/public/**", "/images/**", "/css/**",
						"/fonts/**", "/js/**", "/*.html")
				.permitAll()
				.and()
				.formLogin()
				.usernameParameter("photox_username")
				.passwordParameter("photox_password")
				.loginPage("/login")
				.failureUrl("/login?error=bad_credentials")
				// the success handler of the login operation
				.successHandler(loginSuccessHandler).permitAll().and().logout()
				.invalidateHttpSession(true)
				.logoutUrl("/login?logout=by_request")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.deleteCookies("JSESSIONID").logoutSuccessUrl("/")
				.logoutSuccessHandler(logoutSuccessHandler).permitAll();
		// DISABLED : for some weird reason, you cannot re-login after you
		// logout
		/**
		 * http.sessionManagement().maximumSessions(1)
		 * .expiredUrl("/login?logout=expired")
		 * .maxSessionsPreventsLogin(true).and()
		 * .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		 * .invalidSessionUrl("/");
		 **/
		// TODO : later, make angular work with CSRF and activate this back
		// disable csrf, otherwise angular will fail placing calls to secured
		// methods
		http.csrf().disable();
		// log.info("Configured http security");
	}
}