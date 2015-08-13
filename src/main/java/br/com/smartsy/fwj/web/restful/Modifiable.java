package br.com.smartsy.fwj.web.restful;

import java.util.Date;

/**
 * Interface for rest resources that handle isModifiedSince header param
 * <p> 
 * The implementor should inform if the resource has been modified
 * since the specified date.
 * </p>
 * @author Vagner
 *
 */
public interface Modifiable {

	/**
	 * Should return the date of the last modification
	 * @return last modified date
	 */
	public Date getLastModifiedDate();
	
}
