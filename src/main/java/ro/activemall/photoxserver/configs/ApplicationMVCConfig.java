package ro.activemall.photoxserver.configs;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

/**
 * 
 * @author Badu
 *
 *         Configures the WebMVC component of the server,
 */
@Configuration
@EnableWebMvc
public class ApplicationMVCConfig extends WebMvcAutoConfigurationAdapter {

	private static Logger log = Logger.getLogger(ApplicationMVCConfig.class);

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// log.info("Registering view controllers...");
		/**
		 * these map the views to the request endpoint, so we can deliver a view
		 * when the request happens
		 */
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/").setViewName("loginpage");
		registry.addViewController("/login").setViewName("loginpage");
		registry.addViewController("/admin").setViewName("admin");
		registry.addViewController("/agency").setViewName("agency");
		registry.addViewController("/customer").setViewName("customer");
	}
}