package ro.activemall.photoxserver.enums;

/**
 * 
 * @author Badu
 *
 *         Resource types
 */
public enum InvoicesTypes {
	DRAFT(0), ORDER(1), INVOICED(2), INVOICE_PAYED(3), ORDER_PROCESSED(4), ORDER_DELIVERED(
			5);

	private int type;

	private InvoicesTypes(int defType) {
		type = defType;
	}

	public int getIntValue() {
		return type;
	}

	public static String toString(int ofType) {
		switch (ofType) {
		case 0:
			return "DRAFT";
		case 1:
			return "ORDER";
		case 2:
			return "INVOICED";
		case 3:
			return "INVOICE PAYED";
		case 4:
			return "ORDER PROCESSED";
		case 5:
			return "ORDER DELIVERED";
		}
		return "UNKNOWN";

	}
}
