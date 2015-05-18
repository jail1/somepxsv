package ro.activemall.photoxserver.configs;

import java.util.Locale;
import java.util.Properties;

import javax.servlet.MultipartConfigElement;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import ro.activemall.photoxserver.exceptions.LoggingHandlerExceptionResolver;
import ro.activemall.photoxserver.filters.GZIPFilter;
import ro.activemall.photoxserver.filters.SimpleCORSFilter;
import ro.activemall.photoxserver.interceptors.RequestProcessingTimeInterceptor;
import ro.activemall.photoxserver.json.JodaDateTimeObjectMapper;
import ro.activemall.photoxserver.utils.memory.MemoryWarningSystem;

/**
 * 
 * @author Badu
 *
 *         Other configurations needed - see comments below
 */
@Configuration
@EnableAsync
@EnableScheduling
public class ApplicationOtherConfigs {

	private static Logger log = Logger.getLogger(ApplicationOtherConfigs.class);

	// @Autowired
	// private Environment env;

	// logs server side crashes, for debugging purposes
	@Bean
	protected LoggingHandlerExceptionResolver loggingHandler() {
		return new LoggingHandlerExceptionResolver();
	}

	// handler of the 404 messages, so we can track what's missing (in some
	// cases we can re-generate the missing data)
	@Bean
	public RequestProcessingTimeInterceptor getRequestProcessingTimeInterceptor() {
		return new RequestProcessingTimeInterceptor();
	}

	@Bean
	public MappedInterceptor requestProcessingTimeInterceptor() {
		return new MappedInterceptor(null,
				getRequestProcessingTimeInterceptor());
	}

	// CORS filter, to allow calls to come from any other place
	// in case we decide to serve Angular from Nginx and leave this server as it
	// is
	@Bean
	public SimpleCORSFilter corsFilter() {
		return new SimpleCORSFilter();
	}

	// compresses responses provided to browser - in case we need to deliver big
	// data quickly
	@Bean
	public GZIPFilter gzipFilter() {
		return new GZIPFilter();
	}

	// server side internationalized messages and their location
	@Bean(name = "messageSource")
	public ResourceBundleMessageSource geti18nMessageSource() {
		ResourceBundleMessageSource result = new ResourceBundleMessageSource();
		result.setBasename("messages/errors");
		return result;
	}

	@Bean(name = "localeResolver")
	public SessionLocaleResolver sessionLocaleResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(new Locale("en", "US"));
		return localeResolver;
	}

	// for file uploading, we configure the maximum file size and request size
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(Long.MAX_VALUE);
		factory.setMaxRequestSize(Long.MAX_VALUE);
		// TODO : check threshold out
		// factory.setFileSizeThreshold(???);
		// TODO : might need to setup the setLocation as well
		return factory.createMultipartConfig();
	}

	// same as above
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		// log.info("DispatcherContext multipartResolver.");
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(Long.MAX_VALUE);
		return multipartResolver;
	}

	// a memory utility bean, that is watching the memory allocation - for
	// debugging, fine tuning and tweaking
	@Bean
	public MemoryWarningSystem getMemoryWarningSystem() {
		MemoryWarningSystem result = new MemoryWarningSystem();
		result.setPercentageUsageThreshold(0.7d);
		return result;
	}

	@Value("${mail.smtp.host}")
	String smtpHost;
	@Value("${mail.smtp.port}")
	String smtpPort;
	@Value("${mail.smtp.protocol}")
	String smtpProtocol;
	@Value("${mail.smtp.username}")
	String smtpUser;
	@Value("${mail.smtp.password}")
	String smtpPass;
	@Value("${mail.smtp.auth}")
	String smtpAuth;
	@Value("${mail.smtp.starttls.enable}")
	String startTLS;
	@Value("${mail.smtp.quitwait}")
	String smtpQuitWait;

	// email sending
	@Bean
	public JavaMailSenderImpl javaMailSenderImpl() {
		JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
		mailSenderImpl.setHost(smtpHost);
		mailSenderImpl.setPort(Integer.valueOf(smtpPort));
		mailSenderImpl.setProtocol(smtpProtocol);
		mailSenderImpl.setUsername(smtpUser);
		mailSenderImpl.setPassword(smtpPass);
		Properties javaMailProps = new Properties();
		javaMailProps.put("mail.smtp.auth", smtpAuth);
		javaMailProps.put("mail.smtp.starttls.enable", startTLS);
		javaMailProps.put("mail.smtp.quitwait", smtpQuitWait);
		mailSenderImpl.setJavaMailProperties(javaMailProps);
		// log.info("Mail service BEAN ok");
		return mailSenderImpl;
	}
	//JSON message convertor
	@Bean
	public HttpMessageConverters customConverters()
	{
		// log.info("Custom converters (json)");
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setPrefixJson(false);
		//Attention : Javascript is messing up because of this pretty print
		//converter.setPrettyPrint(true);
		JodaDateTimeObjectMapper mapper = new JodaDateTimeObjectMapper();
		converter.setObjectMapper(mapper);
		return new HttpMessageConverters(converter);
	}
}
