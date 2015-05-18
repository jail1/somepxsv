package ro.activemall.photoxserver.events;

import org.springframework.context.ApplicationEvent;

import ro.activemall.photoxserver.entities.User;
import ro.activemall.photoxserver.enums.UserActivityTypes;

/**
 * 
 * @author Badu
 * 
 *         For separation of interests, we're using events to tell interested
 *         instances of a certain action
 *
 */
public class UserActionEvent extends ApplicationEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int type;
	private String extra;

	public UserActionEvent(User user, int type, String extra) {
		super(user);
		this.type = type;
		this.extra = extra;
	}

	public User getUser() {
		return (User) this.getSource();
	}

	public int getType() {
		return type;
	}

	public String getExtra() {
		return extra;
	}

	@Override
	public String toString() {
		return "UserActionEvent [type=" + UserActivityTypes.toString(type)
				+ ", extra=" + extra + "]";
	}
}
