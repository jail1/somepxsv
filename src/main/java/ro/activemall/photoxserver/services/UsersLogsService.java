package ro.activemall.photoxserver.services;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import ro.activemall.photoxserver.entities.UserLogActivity;
import ro.activemall.photoxserver.enums.UserActivityTypes;
import ro.activemall.photoxserver.events.UserActionEvent;
import ro.activemall.photoxserver.repositories.UsersLoggedActivitiesRepository;

@Component
public class UsersLogsService implements ApplicationListener<UserActionEvent> {
	private static Logger logger = Logger.getLogger(UsersLogsService.class);

	@Autowired
	UsersLoggedActivitiesRepository repository;

	@Autowired
	ResourceBundleMessageSource i18nMessage;

	@Autowired
	SimpMessagingTemplate template;

	@Value("${application.websocket.topicname}")
	String MAIN_TOPIC_NAME;

	@Value("${application.websocket.notifiername}")
	String MAIN_NOTIFICATOR_NAME;

	@Value("${application.default.server.language}")
	String defaultServerLanguage;

	Locale defaultLocale;

	@PostConstruct
	public void setupLocale() {
		final String[] args = defaultServerLanguage.split("_");
		defaultLocale = new Locale(args[0], args[1]);
	}

	@Override
	public void onApplicationEvent(UserActionEvent event) {
		UserLogActivity entity = new UserLogActivity();
		entity.setTargetUser(event.getUser());
		entity.setType(event.getType());
		if (event.getExtra() != null) {
			entity.setExtraInformation(event.getExtra());
		}
		// logger.info("-------Event-------");
		// logger.info(entity.toString());
		// logger.info("-------------------");
		try {
			repository.save(entity);
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}
		if (entity.getType() == UserActivityTypes.LOGIN.getIntValue()) {
			template.convertAndSend(
					MAIN_TOPIC_NAME + MAIN_NOTIFICATOR_NAME,
					"{\"event\":\""
							+ i18nMessage.getMessage(
									"info.websocket.user.login", new Object[] {
											event.getUser().getFirstName(),
											event.getUser().getLastName() },
									defaultLocale) + "\"}");
		} else if (entity.getType() == UserActivityTypes.LOGOUT.getIntValue()) {
			template.convertAndSend(
					MAIN_TOPIC_NAME + MAIN_NOTIFICATOR_NAME,
					"{\"event\":\""
							+ i18nMessage.getMessage(
									"info.websocket.user.logout", new Object[] {
											event.getUser().getFirstName(),
											event.getUser().getLastName() },
									defaultLocale) + "\"}");
		}

	}

}
