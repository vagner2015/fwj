package br.com.smartsy.fwj.exception;

/**
 * Exception used to indicate problems on authentication
 * @author Vagner
 * 
 */
public class AuthenticationException extends Exception {

	private static final long serialVersionUID = 571746798149696954L;

	public AuthenticationException() {
		super();
	}

	public AuthenticationException(String msg) {
		super(msg);
	}

}
