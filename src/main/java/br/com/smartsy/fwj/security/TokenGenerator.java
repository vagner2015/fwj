package br.com.smartsy.fwj.security;


/**
 * Interface that defines a token generator
 * @author Vagner
 *
 */
public interface TokenGenerator {

	/**
	 * Generates a randomic token based on pattern
	 * @return A token
	 */
	public Token generate();
	
	/**
	 * Generates a token by a pre-defined value
	 * @param value
	 * @return
	 */
	public Token generate(String value);
	
	/**
	 * Get the token generation pattern
	 * @return
	 */
	public String getPattern();

}
