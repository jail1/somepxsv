package ro.activemall.photoxserver.configs;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import ro.activemall.photoxserver.interceptors.WebsocketChannelInterceptor;

/**
 * 
 * @author Badu Websocket configuration
 * 
 * @see http
 *      ://www.theotherian.com/2014/03/spring-boot-websockets-stomp-chat.html
 *      for future chat implementation
 */
@Configuration
@EnableWebSocketMessageBroker
public class ApplicationWebSocketConfig extends
		AbstractWebSocketMessageBrokerConfigurer {

	@Value("${application.websocket.topicname}")
	String MAIN_TOPIC_NAME;

	@Value("${application.websocket.applicationname}")
	String MAIN_APPLICATION_NAME;

	@Value("${application.websocket.notifiername}")
	String MAIN_NOTIFICATOR_NAME;

	private static Logger log = Logger
			.getLogger(ApplicationWebSocketConfig.class);

	// mounting an interceptor, in case we are interested in what's exchanged in
	// our websocket server
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.setInterceptors(new WebsocketChannelInterceptor());
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker(MAIN_TOPIC_NAME);
		config.setApplicationDestinationPrefixes(MAIN_APPLICATION_NAME);
		// log.info("Message broker configured");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// HttpSessionHandshakeInterceptor assures that the listener / publisher
		// has to be logged in
		registry.addEndpoint(MAIN_NOTIFICATOR_NAME).withSockJS()
				.setInterceptors(new HttpSessionHandshakeInterceptor())
				.setStreamBytesLimit(512 * 1024).setHeartbeatTime(60L * 1000L)
				.setDisconnectDelay(60L * 1000L).setHttpMessageCacheSize(1000)
				.setSessionCookieNeeded(true);
		// log.info("Message broker STOMP endpoint registered");
	}

	/**
	 * Configure the {@link org.springframework.messaging.MessageChannel} used
	 * for outgoing messages to WebSocket clients. By default the channel is
	 * backed by a thread pool of size 1. It is recommended to customize thread
	 * pool settings for production use.
	 */
	@Override
	public void configureClientOutboundChannel(ChannelRegistration registration) {
		registration.taskExecutor().corePoolSize(4).maxPoolSize(10);
	}
}