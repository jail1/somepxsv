package ro.activemall.photoxserver.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import ro.activemall.photoxserver.exceptions.PhotoxException;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "user_log_activity")
@Table(name = "photox_users_logs")
public class UserLogActivity extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "user_id")
	private User targetUser;

	@Column(name = "event_date", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime eventDate;

	@Column(name = "type", nullable = false)
	private int type;

	@Column(name = "extra_info", length = 65534)
	private String extraInformation;

	@PrePersist
	public void setCurrentEventDate() {
		eventDate = new DateTime().toDateTime(DateTimeZone.UTC);
		if (targetUser == null) {
			throw new PhotoxException(
					"Activity log doesn't have a target user!");
		}
	}

	public User getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(User targetUser) {
		this.targetUser = targetUser;
	}

	public DateTime getEventDate() {
		return eventDate;
	}

	public void setEventDate(DateTime eventDate) {
		this.eventDate = eventDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getExtraInformation() {
		return extraInformation;
	}

	public void setExtraInformation(String extraInformation) {
		this.extraInformation = extraInformation;
	}

	@Override
	public String toString() {
		return "UserLogActivity [id=" + id + ", targetUser=" + targetUser
				+ ", eventDate=" + eventDate + ", type=" + type
				+ ", extraInformation=" + extraInformation + "]";
	}

}
