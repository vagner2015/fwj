package br.com.smartsy.fwj.web.common;

/**
 * Webservice client interface 
 * @author Vagner
 *
 */
public interface Client {
	
	/**
	 * Get target address
	 */
	public String getAddress();
	
	/**
	 * Set target address
	 * @param address
	 */
	public void setAddress(String address);
	
}
