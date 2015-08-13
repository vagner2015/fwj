package br.com.smartsy.fwj.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * A properties file utility, wich reads a value by its key
 * @author Vagner
 *
 */
public class PropertiesResolver {
	
	private Properties props = new Properties();

	/**
	 * Prepare the utility
	 * @param resource - The properties file name. If its on subfolder, specify it.
	 */
	public PropertiesResolver(String classpathResource) {
		resolveResource(classpathResource);
	}
	
	/**
	 * Prepare the utility
	 * @param resource - The properties file name. If its on subfolder, specify it.
	 */
	public PropertiesResolver(File systemResource) {
		resolveResource(systemResource);
	}
	
	/**
	 * Prepare the utility
	 * @param props - Already loaded properties object
	 */
	public PropertiesResolver(Properties props) {
		this.props = props;
	}
	
	private void resolveResource(String resource){
		if(!StringUtil.hasText(resource)) throw new IllegalArgumentException("The resorce must be specified");
		InputStream is = null;
		try{
			if(!resource.endsWith(".properties"))
				resource = resource.concat(".properties");
			is = getClassloader().getResourceAsStream(resource);
			props.load(is);
			is.close();
		}
		catch(Exception e){
			FileNotFoundException fnfe = new FileNotFoundException("The file '"+resource+"' does not exist on classpath"); 
			throw new RuntimeException(fnfe);
		}
	}
	
	private void resolveResource(File resource){
		if(resource == null || !resource.exists()) throw new IllegalArgumentException("The resorce must be specified");
		InputStream is = null;
		try{
			is = new FileInputStream(resource);
			props.load(is);
			is.close();
		}
		catch(Exception e){
			FileNotFoundException fnfe = new FileNotFoundException("The file '"+resource+"' does not exist on classpath"); 
			throw new RuntimeException(fnfe);
		}
	}
	
	private ClassLoader getClassloader(){
		return Thread.currentThread().getContextClassLoader();
	}
	
	/**
	 * Searches for the property with the specified key in this property list. If the key is not found in this property list, 
	 * the default property list, and its defaults, recursively, are then checked. The method returns null if the property is not found.
	 * @param key - The property key
	 * @return A String containing the value
	 */
	public String getProperty(String key){
		return props.getProperty(key);
	}
	
	/**
	 * Searches for the property with the specified key and its parameters in this property list. If the key is not found in this property list, 
	 * the default property list, and its defaults, recursively, are then checked. The method returns blank if the property is not found.
	 * @param key - The property key
	 * @param params - The parameters in ascending order.
	 * @return A String containing the value
	 */
	public String getProperty(String key,String...params){
		String value = getProperty(key);
		if(value == null) 
			return "";
		MessageFormat formatter = new MessageFormat("");
		formatter.applyPattern(getProperty(key));
		return formatter.format(params);
	}
	
	/**
	 * Get the properties
	 * @return properties
	 */
	public Properties getProperties() {
		return props;
	}
	
}
