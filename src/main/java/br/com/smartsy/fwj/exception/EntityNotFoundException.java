package br.com.smartsy.fwj.exception;

/**
 * Exception responsible for indicating that
 * no results were found by the query
 * @author Vagner
 *
 */
public class EntityNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public EntityNotFoundException() {
		this(null);
	}
	
	public EntityNotFoundException(String msg) {
		this(msg,null);
	}
	
	public EntityNotFoundException(String msg, Throwable cause) {
		super(msg,cause);
	}
	
}
