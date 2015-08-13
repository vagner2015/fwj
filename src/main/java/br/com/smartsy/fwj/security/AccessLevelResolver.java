package br.com.smartsy.fwj.security;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import br.com.smartsys.fwj.annotation.Level;
import br.com.smartsys.fwj.annotation.LevelOverride;
import br.com.smartsys.fwj.annotation.MethodLevel;

/**
 * Utility made for accessLevel resolving
 * @author Vagner
 *
 */
public class AccessLevelResolver {
	
	/**
	 * Resolve the class accessLevel
	 * Iterates over all super classes interfaces
	 * @param clazz
	 * @return Access Level
	 */
	public static int resolve(Class<?> clazz){
		Class<?> current = clazz != null ? clazz : Object.class;
		Level level = null;
		while(level == null && !(current.equals(Object.class))){
			level = getAnnotation(current, Level.class);
			if(level == null)
				level = lookForInterfaces(current, Level.class);
			current = current.getSuperclass();
		}
		return getLevel(level);
	}

	/**
	 * Resolve the method accessLevel
	 * Iterates over all interfaces, and super class interfaces
	 * @param method
	 * @return Access Level
	 */
	public static int resolve(Method method){
		if(method == null)
			return getLevel(null);
		Level level = getAnnotation(method, Level.class);
		Class<?> clazz = method.getDeclaringClass();
		if(!clazz.isInterface())
			while(level == null){
				for(Class<?> iface : clazz.getInterfaces()){
					try {
						Method declaringMethod = iface.getMethod(method.getName(), method.getParameterTypes());
						Level ifaceLevel = getAnnotation(declaringMethod, Level.class);
						if(ifaceLevel != null){
							level = ifaceLevel;
							break;
						}
					} 
					catch (NoSuchMethodException | SecurityException e) {
						//Just continue the loop
					}
				}
				if(clazz.getSuperclass().equals(Object.class))
					break;
				clazz = clazz.getSuperclass();
			}
		return level == null ? resolve(clazz) : level.value();
	}
	
	/**
	 * Resolve the method accessLevel and respect level overrides on owner class
	 * Iterates over all interfaces, and super class interfaces
	 * @param owner - The high class owner
	 * @param method - The method being called
	 * @return Access Level
	 */
	public static int resolve(Class<?> owner, Method method){
		LevelOverride override = lookForAnnotation(owner, LevelOverride.class);
		if(override != null){
			String methodName = method.getName();
			for(MethodLevel methodLvl : override.value()){
				if(methodName.equals(methodLvl.methodName())){
					return methodLvl.level();
				}
			}
		}
		return resolve(method);
	}
	
	/**
	 * Scan class and its interfaces for the required annotation
	 * @param clazz
	 * @param annot
	 * @return The found annotation
	 */
	private static <A extends Annotation> A lookForAnnotation(Class<?> clazz, Class<A> annot){
		A found = null;
		if(!clazz.isInterface())
			found = getAnnotation(clazz, annot);
		return found == null ? lookForInterfaces(clazz, annot) : found;
	}
	
	/**
	 * Scan all class interfaces for the level annotation
	 * @param clazz
	 * @param annot
	 * @return The found annotation
	 */
	private static <A extends Annotation> A lookForInterfaces(Class<?> clazz, Class<A> annot){
		if(clazz != null){
			Class<?> ifaces[] = clazz.isInterface() ? new Class<?>[]{clazz} : clazz.getInterfaces();
			for(Class<?> iface : ifaces){
				A found = getAnnotation(iface, annot);
				if(found != null)
					return found;
			}
		}
		return null;
	}
	
	private static <A extends Annotation> A getAnnotation(AnnotatedElement element,Class<A> annot){
		return element.getAnnotation(annot);
	}
	
	/**
	 * Gets the level based on the annotation, or the default value
	 * @param level
	 * @return
	 */
	private static int getLevel(Level level) {
		return level != null ? level.value() : AccessLevel.NONE;
	}
	
}