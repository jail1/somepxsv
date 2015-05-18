package ro.activemall.photoxserver.enums;

/**
 * 
 * @author Badu
 *
 *         Resource types
 */
public enum ResourcesTypes {
	ROOT(0), CLIENT_FOLDER(1), EVENT_FOLDER(2), FILE(3);

	private int type;

	private ResourcesTypes(int defType) {
		type = defType;
	}

	public int getIntValue() {
		return type;
	}

	public static String toString(int ofType) {
		switch (ofType) {
		case 0:
			return "ROOT";
		case 1:
			return "CLIENT FOLDER";
		case 2:
			return "EVENT FOLDER";
		case 3:
			return "FILE";
		}
		return "UNKNOWN";

	}
}
