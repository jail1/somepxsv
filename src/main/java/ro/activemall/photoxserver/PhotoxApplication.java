package ro.activemall.photoxserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * @author Badu Main Spring Boot Application
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = { "ro.activemall.photoxserver" })
public class PhotoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoxApplication.class, args);
	}
}
