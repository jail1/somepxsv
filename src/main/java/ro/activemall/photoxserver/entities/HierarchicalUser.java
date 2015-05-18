package ro.activemall.photoxserver.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ro.activemall.photoxserver.enums.RolesAsStrings;
import ro.activemall.photoxserver.enums.UserRoles;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "hierarchical_user")
@Table(name = "photox_users")
public class HierarchicalUser extends AbstractEntity {
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

	@Column(name = "created_at")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime createdAt;

	@Column(name = "last_seen")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime lastLoggedIn;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "phone")
	private String mobilePhone;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = HierarchicalUser.class)
	@Fetch(FetchMode.SUBSELECT)
	@JoinColumn(name = "parent_id", nullable = true)
	private Set<HierarchicalUser> referees;

	public Set<HierarchicalUser> getReferees() {
		return referees;
	}

	public void setReferees(Set<HierarchicalUser> referees) {
		this.referees = referees;
	}	
	
	public String getUsername() {
		return username;
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

	public DateTime getCreationDate() {
		return createdAt;
	}

	public void setCreationDate(DateTime creationDate) {
		this.createdAt = creationDate;
	}	
	
	public String getAccountType(){
		if (role.getRole() == UserRoles.ROLE_SUPER_ADMIN.getIntValue()) {
			return RolesAsStrings.ROLE_SUPER_ADMIN;			
		} else if (role.getRole() == UserRoles.ROLE_PHOTOGRAPHER.getIntValue()) {
			return RolesAsStrings.ROLE_PHOTOGRAPHER;			
		} else if (role.getRole() == UserRoles.ROLE_PHOTOGRAPHERS_CLIENT.getIntValue()) {
			return RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT;
		}
		return "unknown???";
	}
	
	public void setAccountType(String value){
		//just to satisfy JSON
	}
	/**
	 * 
	 */
	@Transient
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: ").append(getId()).append("\n");		
		sb.append("Username (unique email): ").append(getUsername()).append("\n");
		sb.append("FirstName: ").append(getFirstName()).append("\n");
		sb.append("LastName: ").append(getLastName()).append("\n");
		sb.append("Phone: ").append(getMobilePhone()).append("\n");		
		sb.append("Created at : ").append(getCreationDate()).append("\n");		
		sb.append("Last login date : ").append(getLastLoggedIn()).append("\n");
		return sb.toString();
	}
}