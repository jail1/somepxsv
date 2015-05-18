package ro.activemall.photoxserver.interceptors;

import org.apache.log4j.Logger;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

/**
 * 
 * @author Badu
 *
 *         In case we need to listen websocket activities
 */
public class WebsocketChannelInterceptor extends ChannelInterceptorAdapter {
	private static Logger log = Logger
			.getLogger(WebsocketChannelInterceptor.class);

	@SuppressWarnings("unused")
	private void dummy() {
		log.info("Dummy");
	}

	@Override
	public Message<?> postReceive(Message<?> message, MessageChannel channel) {
		// log.info("POST RECEIVE : "+message.toString() +
		// " "+channel.toString());
		return super.postReceive(message, channel);
	}

	@Override
	public void postSend(Message<?> message, MessageChannel channel,
			boolean sent) {
		super.postSend(message, channel, sent);
		// log.info("POST SEND : "+message.toString() + " "+
		// channel.toString());
	}

	@Override
	public boolean preReceive(MessageChannel channel) {
		// log.info("PRE RECEIVE : "+channel.toString());
		return super.preReceive(channel);
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		// log.info("PRE SEND : "+message.toString()+ " "+channel.toString());
		return super.preSend(message, channel);
	}

}
