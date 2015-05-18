package ro.activemall.photoxserver.enums;

/**
 * 
 * @author Badu
 * 
 *         For auditing user actions - WIP
 */
public enum UserActivityTypes {
	LOGIN(0), LOGOUT(1), DOWNLOAD_FILE(2);

	private int type;

	private UserActivityTypes(int defType) {
		type = defType;
	}

	public int getIntValue() {
		return type;
	}

	public static String toString(int ofType) {
		switch (ofType) {
		case 0:
			return "LOGIN";
		case 1:
			return "LOGOUT";
		case 2:
			return "DOWNLOAD";
		}
		return "UNKNOWN";

	}
}
