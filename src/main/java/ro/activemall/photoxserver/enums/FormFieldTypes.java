package ro.activemall.photoxserver.enums;

public enum FormFieldTypes {
	NO_TYPE(0), TEXT(1), SELECT(2), CHECK(3);

	private int type;

	private FormFieldTypes(int defType) {
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
			return "TEXT";
		case 2:
			return "SELECT";
		case 3:
			return "CHECK";
		}
		return "UNKNOWN";

	}
}
