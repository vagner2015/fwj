package br.com.smartsy.fwj.persistence.function;

import java.security.NoSuchAlgorithmException;

/**
 * Strategy contract for query function handling
 * @author Vagner
 * @since 1.0
 */
public interface FunctionStrategy {

	/**
	 * Handles a query, resolving the specific function
	 * @param query - The query to be resolved
	 * @param functionIndex - The index of the function
	 * @param clazz - The entity type
	 * @return The resolved query
	 * @throws NoSuchAlgorithmException 
	 */
	public String handle(String query,int functionIndex,Class<?> clazz) throws Exception;
	
	/**
	 * Validates the function
	 * @param query - The query to be resolved
	 * @param functionIndex - The index of the function
	 * @param clazz - The entity type
	 * @return True if valid, False if not
	 */
	public boolean isValid(String query, int functionIndex, Class<?> clazz);
}
