package br.com.smartsys.fwj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.smartsy.fwj.security.Algorithm;

/**
 * Indicates that the value must me encrypted before being persisted
 * @author Vagner
 * 
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface Encrypt {

	Algorithm value() default Algorithm.MD5;
	
}
