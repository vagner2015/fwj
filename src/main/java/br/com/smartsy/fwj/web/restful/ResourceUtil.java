package br.com.smartsy.fwj.web.restful;

import java.util.Date;

/**
 * Class with rest resources utilities
 * @author Vagner
 *
 */
public class ResourceUtil {

	/**
	 * Check if the resource has been modified since the specified date
	 * @param modifiable - The resource to check(must implement the {@link Modifiable} interface
	 * @param modifiedSince - The time to check against
	 * @return True if has been modified or False if not
	 */
	public static boolean isModifiedSince(Modifiable modifiable,Date modifiedSince){
		//If null, then avoid and flag modified
		if(modifiedSince == null)
			return true;
		return modifiable.getLastModifiedDate().after(modifiedSince);
	}
	
}
