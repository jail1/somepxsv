package ro.activemall.photoxserver.enums;

/**
 * 
 * @author Badu
 *
 *         Enumeration of the possible user roles
 */
public enum UserRoles {
	ROLE_SUPER_ADMIN(1), ROLE_PHOTOGRAPHER(2), ROLE_PHOTOGRAPHERS_CLIENT(3);

	private int code;

	private UserRoles(int role) {
		code = role;
	}

	public int getIntValue() {
		return code;
	}
}
