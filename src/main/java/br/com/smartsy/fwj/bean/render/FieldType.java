package br.com.smartsy.fwj.bean.render;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.joda.time.DateTime;

import br.com.smartsy.fwj.bean.render.annotation.SchemaSecret;

/**
 * Field types for entity schema generation
 * @author Vagner
 *
 */
public enum FieldType {
	/**
	 * Text
	 */
	TEXT,
	/**
	 * Number
	 */
	NUMBER,
	/**
	 * Boolean
	 */
	BOOLEAN,
	/**
	 * Enumerated
	 */
	ENUMERATED,
	/**
	 * Date
	 */
	DATE,
	/**
	 * DateTime
	 */
	DATETIME,
	/**
	 * Secret
	 */
	SECRET,
	/**
	 * Composition(1 : 1)
	 */
	COMPOSITION,
	/**
	 * List(1 : N)
	 */
	LIST,
	/*
	 * Resource/File/Stream
	 */
	RESOURCE;
	
	/**
	 * Get field type by reflection
	 * @param field
	 * @return The Field Type
	 * TODO: Should use directly the field type name
	 */
	public static FieldType getByField(Field field){
		Class<?> type = field.getType();
		
		//ENUMERATED TYPE
		if(type.isEnum())
			return ENUMERATED;
		
		//SECRET TYPE
		if(field.getAnnotation(SchemaSecret.class) != null)
			return SECRET;
		
		//TEXT TYPE
		if(type.equals(String.class) || type.toString().equals("char"))
			return TEXT;
		
		//BOOLEAN TYPE
		if(type.equals(Boolean.class) || type.toString().equals("boolean"))
			return BOOLEAN;
		
		//DATE TYPE
		if(type.equals(Date.class) || type.equals(Calendar.class) || type.equals(DateTime.class))
			return DATE;
		
		//RESOURCE TYPE
		if(field.getType().isArray() && field.getType().getComponentType().toString().equals("byte"))
			return RESOURCE;
		
		//LIST 1:N
		if(Collection.class.isAssignableFrom(field.getType()) || field.getType().isArray())
			return LIST;
		
		//NUMBER TYPE
		if(type.isPrimitive() || type.getPackage().equals(Number.class.getPackage()))
			return NUMBER;
		
		//COMPOSITION 1:1
		return COMPOSITION;
	}
}