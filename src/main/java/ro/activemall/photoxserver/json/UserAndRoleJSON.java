package ro.activemall.photoxserver.json;

import ro.activemall.photoxserver.entities.User;

/**
 * 
 * @author Badu
 *
 *         This kind of object is send by clients, usually the adminstrator when
 *         it's going to change a role for a user
 */
public class UserAndRoleJSON {
	private User user;
	private int role;
	private boolean isDowngrade = false;

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

	public boolean getDowngrade() {
		return isDowngrade;
	}

	public void setDowngrade(boolean isDowngrade) {
		this.isDowngrade = isDowngrade;
	}
}
