package ro.activemall.photoxserver.exceptions;

/**
 * 
 * @author Badu
 *
 *         Our own triggered exception, which usually gets into Angular as a
 *         toast
 */
public class PhotoxException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String errorMessage;

	public PhotoxException(String message) {
		this.errorMessage = message;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
