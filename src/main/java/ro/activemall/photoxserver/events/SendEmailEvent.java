package ro.activemall.photoxserver.events;

import org.springframework.context.ApplicationEvent;

import ro.activemall.photoxserver.entities.User;

/**
 * 
 * @author Badu
 * 
 *         For separation of interests, we're using events to tell interested
 *         instances of a certain action
 *
 */
public class SendEmailEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int TEST = -1;

	public static final int REGISTER = 0;

	public static final int CHANGE_PASSWORD = 2;
	public static final int NEWSLETTER = 3;
	public static final int FORGOT_PASSWORD = 4;

	private int type;
	private Object extra;

	public SendEmailEvent(User user, int type) {
		super(user);
		this.type = type;
	}

	public User getUser() {
		return (User) this.getSource();
	}

	public int getType() {
		return type;
	}

	public Object getExtra() {
		return extra;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}

	public static String toString(int declType) {
		switch (declType) {
		case SendEmailEvent.TEST:
			return "TEST";
		case SendEmailEvent.REGISTER:
			return "REGISTER";
		case SendEmailEvent.CHANGE_PASSWORD:
			return "CHANGE_PASSWORD";
		case SendEmailEvent.FORGOT_PASSWORD:
			return "FORGOT_PASSWORD";
		case SendEmailEvent.NEWSLETTER:// nefolosit inca
			return "NEWSLETTER";
		}
		return "UNKNOWN";
	}
}
