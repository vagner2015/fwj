package br.com.smartsy.fwj.exception;

/**
 * {@link RuntimeException} for handling errors while initializating classes, or objects
 * @author Vagner
 *
 */
public class InitializationException extends RuntimeException
{

	/**
	 * Generated SerialUID
	 */
	private static final long serialVersionUID = 4180247098051967002L;
	
	public InitializationException(String msg)
	{
		super(msg);
	}

	public InitializationException(Throwable cause)
	{
		super(cause);
	}
	
	public InitializationException(String msg,Throwable cause)
	{
		super(msg, cause);
	}
	
}