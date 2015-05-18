package ro.activemall.photoxserver.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ro.activemall.photoxserver.enums.RolesAsStrings;
import ro.activemall.photoxserver.enums.UserRoles;
import ro.activemall.photoxserver.json.GrantedAuthorityJSON;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "user")
@Table(name = "photox_users")
public class User extends AbstractEntity implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@OneToOne(mappedBy = "user", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	private Role role;

	@Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
			+ "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
			+ "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "_InvalidUsername_")
	@Column(unique = true, name = "username", nullable = false)
	private String username;

	@Column(name = "pwd")
	private String password;

	@Column(name = "created_at")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime createdAt;

	@Column(name = "modified_at", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime modifiedDate;

	@Column(name = "last_seen")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime lastLoggedIn;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "phone")
	private String mobilePhone;

	// a string that is sent in email message to activate the account
	@Column(name = "activation_string")
	private String activationString;

	// a string that is sent in email message to re-activate account (lost
	// password)
	@Column(name = "retrieval_code")
	private String retrievalCode;

	// TODO : this should be by default false
	@Column(name = "account_enabled", columnDefinition = "BIT", length = 1)
	@Basic
	private boolean accountEnabled = true;

	@Column(name = "prefered_language")
	private String preferedLanguage;

	@Column(name = "login_name", length = 60)
	private String loginName;
	
	public User() {
		modifiedDate = createdAt = new DateTime();
	}

	@PreUpdate
	protected void onUpdate() {
		// TODO : extract the part in front of "@" character to make the
		// login_name field
		if (loginName == null) {
			loginName = username;
		}
		modifiedDate = new DateTime().toDateTime(DateTimeZone.UTC);
	}

	// in order to change password and allow the password encoder to work
	// Regexp says : 
	//Be between 8 and 40 characters long
	//Contain at least one digit
	//Contain at least one lower case character
	//Contain at least one upper case character
	//Contain at least on special character from [ @ # $ % ! . ]
	@Transient
	@Pattern(regexp = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})", message = "_InvalidPassword_")
	// TODO : check if pattern works on this property
	private String changePasswordTo;

	@Transient
	public String getChangePasswordTo() {
		return this.changePasswordTo;
	}

	@Transient
	public void setChangePasswordTo(String value) {
		this.changePasswordTo = value;
	}

	// end password changer

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		role.setUser(this);
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		this.loginName = username;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonIgnore
	public void setPassword(String password) {
		this.password = password;
	}

	public DateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(DateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public DateTime getLastLoggedIn() {
		return lastLoggedIn;
	}

	public void setLastLoggedIn(DateTime lastLoggedIn) {
		this.lastLoggedIn = lastLoggedIn;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getActivationString() {
		return activationString;
	}

	public void setActivationString(String activationString) {
		this.activationString = activationString;
	}

	public String getRetrievalCode() {
		return retrievalCode;
	}

	public void setRetrievalCode(String retrievalCode) {
		this.retrievalCode = retrievalCode;
	}

	public boolean getAccountEnabled() {
		return accountEnabled;
	}

	public void setAccountEnabled(boolean accountEnabled) {
		this.accountEnabled = accountEnabled;
	}

	public String getPreferedLanguage() {
		return preferedLanguage;
	}

	public void setPreferedLanguage(String preferedLanguage) {
		this.preferedLanguage = preferedLanguage;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public DateTime getCreationDate() {
		return createdAt;
	}

	public void setCreationDate(DateTime creationDate) {
		this.createdAt = creationDate;
	}

	@JsonIgnore
	@Transient
	public boolean isAccountNonExpired() {
		return accountEnabled;
	}

	@JsonIgnore
	@Transient
	public boolean isAccountNonLocked() {
		return accountEnabled;
	}

	@JsonIgnore
	@Transient
	public boolean isCredentialsNonExpired() {
		return accountEnabled;
	}

	@JsonIgnore
	@Transient
	public boolean isEnabled() {
		return accountEnabled;
	}

	/**
	 * UserDetails implementations
	 */
	/**
	 * Retrieves a collection of {@link GrantedAuthority} based on a numerical
	 * role
	 * 
	 * @param role
	 *            the numerical role
	 * @return a collection of {@link GrantedAuthority

	 */
	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authList = null;
		if (role != null) {
			authList = getGrantedAuthorities(getRoles(role.getRole()));
		}
		return authList;
	}

	// Because we're working with users, Json is trying to deserialize this and
	// we don't need it
	// Also, it requires an implementation, since GrantedAuthority is an
	// interface
	@Transient
	public void setAuthorities(List<GrantedAuthorityJSON> value) {
	}

	/**
	 * Converts a numerical role to an equivalent list of roles
	 * 
	 * @param role
	 *            the numerical role
	 * @return list of roles as as a list of {@link String}
	 */
	@Transient
	public List<String> getRoles(Integer role) {
		List<String> roles = new ArrayList<String>();
		if (role == UserRoles.ROLE_SUPER_ADMIN.getIntValue()) {
			roles.add(RolesAsStrings.ROLE_SUPER_ADMIN);
			roles.add(RolesAsStrings.ROLE_PHOTOGRAPHER);
			roles.add(RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT);
		} else if (role == UserRoles.ROLE_PHOTOGRAPHER.getIntValue()) {
			roles.add(RolesAsStrings.ROLE_PHOTOGRAPHER);
			roles.add(RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT);
		} else if (role == UserRoles.ROLE_PHOTOGRAPHERS_CLIENT.getIntValue()) {
			roles.add(RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT);
		}
		return roles;
	}

	/**
	 * Wraps {@link String} roles to {@link SimpleGrantedAuthority} objects
	 * 
	 * @param roles
	 *            {@link String} of roles
	 * @return list of granted authorities
	 */
	@Transient
	public static List<GrantedAuthority> getGrantedAuthorities(
			List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}

	/**
	 * 
	 */
	@Transient
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: ").append(getId()).append("\n");
		if (getRole() != null) {
			sb.append("ROLE: ").append(getRole().toString()).append("\n");
		} else {
			sb.append("ERROR!!! NO ROLE");
		}
		sb.append("Username (unique email): ").append(getUsername())
				.append("\n");
		sb.append("Enabled : ").append(getAccountEnabled()).append("\n");
		sb.append("Activation string : ").append(getActivationString())
				.append("\n");
		sb.append("FirstName: ").append(getFirstName()).append("\n");
		sb.append("LastName: ").append(getLastName()).append("\n");
		sb.append("Phone: ").append(getMobilePhone()).append("\n");
		sb.append("Password : ").append(getPassword()).append("\n");
		sb.append("Retrieval code : ").append(getRetrievalCode()).append("\n");
		sb.append("Created at : ").append(getCreationDate()).append("\n");
		sb.append("Modified at : ").append(getModifiedDate()).append("\n");
		sb.append("Last login date : ").append(getLastLoggedIn()).append("\n");
		return sb.toString();
	}
}