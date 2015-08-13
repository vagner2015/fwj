package br.com.smartsy.fwj.web.common;


/**
 * Token secure interface
 * @author Vagner
 *
 */
public interface TokenSecureClient extends Client {

	/**
	 * Sets the auth token
	 * @param token
	 */
	public abstract void setAuthToken(String token);
	
}
