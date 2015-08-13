package br.com.smartsy.fwj.persistence.function;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import br.com.smartsy.fwj.security.Encrypter;
import br.com.smartsy.fwj.util.StringUtil;
import br.com.smartsys.fwj.annotation.Encrypt;

/**
 * Strategy to encrypt values on query
 * @author Vagner
 * @since 1.0
 */
public class EncryptFunctionStrategy implements FunctionStrategy {
	
	private Encrypt annotation;

	@Override
	public String handle(String query, int functionIndex, Class<?> clazz) throws Exception {
		try{
			if(!isValid(query, functionIndex, clazz))
				return query;
			
			
			int firstParentheseIndex = functionIndex + QueryFunction.ENCRYPT.getIdentifierLenght();
			int index = firstParentheseIndex + 1;
			String value = "";
			String character = query.substring(index,index + 1);
			while(!character.equals(")")){
				value += character;
				character = query.substring(++index,index + 1);
			}
			int lastParentheseIndex = index;
			
			String encrypted = new Encrypter(annotation.value()).encrypt(value);
			
			String firstPart = query.substring(0,functionIndex);
			String secondPart = query.substring(lastParentheseIndex + 1);
			
			return firstPart + encrypted + secondPart;
		}
		catch(Exception e){
			return "[ENCRYPTION_FAILED] - For system safety, the query has been replaced";
		}
	}
	
	/**
	 * How to check the validity:
	 * <p>Is the clazz a null pointer ?
	 * <p>Does the class have a {@link Encrypt} field or method ? 
	 * <p>Query has text ?
	 * <p>Is the functionIndex less than the query length minus function identifier length minus the double parentheses(2 length) minus the minimum value accepted(1 length)
	 */
	public boolean isValid(String query, int functionIndex, Class<?> clazz){
		if(clazz != null)
			annotation = getAnnotation(clazz);
		return annotation != null && StringUtil.hasText(query) && functionIndex >= 0 && functionIndex < (query.length() - (QueryFunction.ENCRYPT.getIdentifierLenght()+3));
	}
	
	private Encrypt getAnnotation(Class<?> clazz){
		for(Method method : clazz.getMethods()){
			Encrypt annotation = method.getAnnotation(Encrypt.class);
			if(annotation != null)
				return annotation;
		}
		for(Field field : clazz.getFields()){
			Encrypt annotation = field.getAnnotation(Encrypt.class);
			if(annotation != null)
				return annotation;
		}
		return null;
	}
	
}