package ro.activemall.photoxserver.entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ro.activemall.photoxserver.enums.UserRoles;

/**
 * 
 * @author Badu
 *
 *         Entity for storying user role inside our database
 */
@Entity(name = "role")
@Table(name = "photox_user_roles")
public class Role extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	private int role;

	public Role() {
	}

	public Role(int role) {
		this.role = role;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		String roleString = (this.role == UserRoles.ROLE_SUPER_ADMIN
				.getIntValue() ? "SUPER ADMIN"
				: (this.role == UserRoles.ROLE_PHOTOGRAPHER.getIntValue() ? "PHOTOGRAPHER"
						: (this.role == UserRoles.ROLE_PHOTOGRAPHERS_CLIENT
								.getIntValue() ? "END CLIENT" : "UNKNOWN")));
		sb.append("ROLE : " + roleString).append("\n");
		return sb.toString();
	}

}
