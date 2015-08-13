package br.com.smartsy.fwj.util;

import java.lang.reflect.ParameterizedType;

/**
 * Utility for java generics
 * @author Vagner
 *
 */
public class GenericsUtil {

	/**
	 * Resolve the parameterized type of the owner class
	 * @param owner - The class that owns the generic type
	 * @return A class type
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> resolveGenericType(Class<?> owner){
		return (Class<T>) ((ParameterizedType) owner.getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
}
