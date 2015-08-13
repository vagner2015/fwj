package br.com.smartsy.fwj.exception;

/**
 * Exception used to indicate problems on authorization
 * @author Vagner
 * 
 */
public class AuthorizationException extends Exception {

	private static final long serialVersionUID = 571746798149696954L;

	public AuthorizationException() {
		super();
	}

	public AuthorizationException(String msg) {
		super(msg);
	}

}
