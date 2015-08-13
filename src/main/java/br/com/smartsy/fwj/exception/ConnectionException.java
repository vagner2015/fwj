package br.com.smartsy.fwj.exception;

/**
 * Exception used to indicate problems while connecting
 * @author Vagner
 * 
 */
public class ConnectionException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ConnectionException() {
		this(null);
	}
	
	public ConnectionException(String msg) {
		this(msg,null);
	}
	
	public ConnectionException(String msg, Throwable cause) {
		super(msg,cause);
	}

}
