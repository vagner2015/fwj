package br.com.smartsy.fwj.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.com.smartsy.fwj.util.StringUtil;

/**
 * An Easy to use {@link EntityManager} instance builder
 * @author Vagner
 *
 */
public class EntityManagerFactoryBuilder {
	private String unit;
	private Map<String, String> parameters = new HashMap<String,String>();
	
	public EntityManagerFactoryBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	public EntityManagerFactoryBuilder(Properties properties) {
		resolveProperties(properties);
	}
	
	/**
	 * Prepare the builder with pre-defined parameters
	 * @param properties
	 */
	private void resolveProperties(Properties properties) {
		forUnit(properties.getProperty("jpa.persistence_unit"));
		
		Set<Object> keySet = properties.keySet();
		keySet.remove("jpa.persistence_unit");
		keySet.remove("jta.datasource");
		
		for(Object keyValue : keySet){
			String key = String.valueOf(keyValue);
			addParam(key, properties.getProperty(key));
		}
	}

	/**
	 * Specify the persistence unit declared on "persistence.xml"
	 * @param unit
	 * @return The builder itself
	 */
	public EntityManagerFactoryBuilder forUnit(String unit){
		this.unit = unit;
		return this;
	}
	
	/**
	 * Add multiple parameters to overwrite the pre-defined ones "persistence.xml" file
	 * @param params - A {@link Map} containing the parameters
	 * @return The builder itself
	 */
	public EntityManagerFactoryBuilder addParams(Map<String, String> params){
		if(params != null)
			this.parameters.putAll(params);
		return this;
	}
	
	/**
	 * Add parameter to overwrite the pre-defined one on "persistence.xml" file
	 * @param key - A String key to the param. Ex: "hibernate.show_sql"
	 * @param value - The value for it's key. Ex: "true"
	 * @return The builder itself
	 */
	public EntityManagerFactoryBuilder addParam(String key,String value){
		if(StringUtil.hasText(key) && StringUtil.hasText(value))
			this.parameters.put(key, value);
		return this;
	}
	
	/**
	 * Build an instance with the parametized details
	 * @return An {@link EntityManagerFactory} instance
	 */
	public EntityManagerFactory build(){
		if(parameters.size() > 0)
			return Persistence.createEntityManagerFactory(unit, parameters);
		return Persistence.createEntityManagerFactory(unit);
	}	
}
