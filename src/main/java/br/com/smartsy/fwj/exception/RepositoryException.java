package br.com.smartsy.fwj.exception;

/**
 * Exception for persistence layer
 * @author Vagner
 *
 */
public class RepositoryException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public RepositoryException() {
		this(null);
	}
	
	public RepositoryException(String msg) {
		this(msg,null);
	}
	
	public RepositoryException(String msg, Throwable cause) {
		super(msg,cause);
	}

}
