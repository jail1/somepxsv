package ro.activemall.photoxserver.enums;

/**
 * 
 * @author Badu
 *
 *         Resource types
 */
public enum TemplateTypes {
	NO_TYPE(0), INVITE(1), CONTRACT(2), OTHER(3);

	private int type;

	private TemplateTypes(int defType) {
		type = defType;
	}

	public int getIntValue() {
		return type;
	}

	public static String toString(int ofType) {
		switch (ofType) {
		case 0:
			return "NO TYPE";
		case 1:
			return "INVITE";
		case 2:
			return "CONTRACT";
		case 3:
			return "OTHER";
		}
		return "UNKNOWN";

	}
}
