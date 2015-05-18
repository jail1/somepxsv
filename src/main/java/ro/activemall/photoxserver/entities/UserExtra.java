package ro.activemall.photoxserver.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "extra")
@Table(name = "photox_users_extra")
public class UserExtra extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "owner_id")
	private User owner;

	@Column(name = "logo")
	private String logo;

	@Column(name = "facebook_id")
	private String facebookId;

	@Column(name = "twitter_id")
	private String twitterId;

	@Column(name = "googleplus_id")
	private String googlePlusId;

	@JsonIgnore
	public User getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getTwitterId() {
		return twitterId;
	}

	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}

	public String getGooglePlusId() {
		return googlePlusId;
	}

	public void setGooglePlusId(String googlePlusId) {
		this.googlePlusId = googlePlusId;
	}

	@JsonIgnore
	@Transient
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Owner: ").append(getOwner().getUsername()).append("\n");
		builder.append("Logo: ").append(getLogo()).append("\n");
		builder.append("Facebook ID: ").append(getFacebookId()).append("\n");
		builder.append("Twitter ID: ").append(getTwitterId()).append("\n");
		builder.append("Google+ ID: ").append(getGooglePlusId()).append("\n");
		return builder.toString();
	}
}
