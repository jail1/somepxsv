package ro.activemall.photoxserver.configs;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import ro.activemall.photoxserver.resolvers.DbTemplateResolver;
import ro.activemall.photoxserver.utils.thymeleafJoda.JodaTimeDialect;

import com.google.common.collect.Sets;

/**
 * 
 * @author Badu Thymeleaf configuration
 */
@Configuration
public class ApplicationThymeleafConfig {

	private static Logger log = Logger
			.getLogger(ApplicationThymeleafConfig.class);

	// default template engine configuration - it will take pages from templates
	// folder and will produce HTML5
	@Bean
	public TemplateResolver springThymeleafTemplateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setPrefix("classpath:/templates/");
		resolver.setSuffix(".html");
		resolver.setCacheable(false);
		resolver.setTemplateMode("HTML5");
		resolver.setCharacterEncoding("UTF-8");
		resolver.setOrder(1);
		return resolver;
	}

	// declaration of the database template resolver
	@Bean
	public DbTemplateResolver dbTemplateResolver() {
		DbTemplateResolver resolver = new DbTemplateResolver();
		resolver.setCacheable(false);
		resolver.setTemplateMode("HTML5");
		resolver.setCharacterEncoding("UTF-8");
		resolver.setOrder(2);
		return resolver;
	}

	// all together now...
	@Bean
	public SpringTemplateEngine thymeleafTemplateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolvers(Sets.newHashSet(
				springThymeleafTemplateResolver(), dbTemplateResolver()));
		// if we need Joda Thymeleaf - un comment dialect adding
		// with expresion:
		// th:text="${#joda.mediumDate(date)}"
		// via attributes
		// joda:mediumDate="${date}"
		// All expressions syntax ---
		// ${#joda.fullDate(date)}
		// ${#joda.fullDateTime(date)}
		// ${#joda.fullTime(date)}
		// ${#joda.longDate(date)}
		// ${#joda.longDateTime(date)}
		// ${#joda.longTime(date)}
		// ${#joda.mediumDate(date)}
		// ${#joda.mediumDateTime(date)}
		// ${#joda.mediumTime(date)}
		// ${#joda.shortDate(date)}
		// ${#joda.shortDateTime(date)}
		// ${#joda.shortTime(date)}
		// ${#joda.isoDateTime(date)}
		engine.addDialect(new JodaTimeDialect());
		return engine;
	}
}
