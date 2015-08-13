package br.com.smartsy.fwj.persistence.function;

/**
 * Available query functions
 * @author Vagner
 *
 */
public enum QueryFunction {

	/**
	 * Encrypts the value on query
	 * <p><b>WARNING:</b></p> 
	 * <p><i>If it fail, the query will be replaced by an invalid one for system safety</i></p>
	 */
	ENCRYPT("#encrypt",new EncryptFunctionStrategy());
	
	private String functionIdentifier;
	private FunctionStrategy strategy;

	private QueryFunction(String functionIdentifier,FunctionStrategy strategy){
		this.functionIdentifier = functionIdentifier;
		this.strategy = strategy;
	}

	public String getFunctionIdentifier() {
		return functionIdentifier;
	}

	public FunctionStrategy getStrategy() {
		return strategy;
	}
	
	public int getIdentifierLenght(){
		return this.getFunctionIdentifier().length();
	}	
}