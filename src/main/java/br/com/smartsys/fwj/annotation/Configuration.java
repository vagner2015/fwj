package br.com.smartsys.fwj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify key to system cofiguration
 * @author Vagner
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface Configuration {
	
	/**
	 * Configuration key
	 */
	String value();
	
}
