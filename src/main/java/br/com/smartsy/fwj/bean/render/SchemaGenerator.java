package br.com.smartsy.fwj.bean.render;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlTransient;

import br.com.smartsy.fwj.bean.render.annotation.SchemaDescription;
import br.com.smartsy.fwj.util.StringUtil;


/**
 * Utility class for schema generation by reflection
 * @author Vagner
 *
 */
public class SchemaGenerator {
	
	private static SchemaGenerator instance;
	private static final String JAVA_PKG_PREFIX = "java";
	
	/**
	 * Get SchemaGenerator instance
	 * @return
	 */
	public static SchemaGenerator getInstance(){
		if(instance == null)
			instance = new SchemaGenerator();
		return instance;
	}
	
	/**
	 * This method generates a schema based on class instance
	 * OBS: It requires the instance in order to provide the custom feature of {@link Schematic} interface
	 * @param clazz - The class to be read
	 * @return Schema
	 */
	public Schema generate(Class<?> clazz){
		return generate(clazz, null);
	}
	
	/**
	 * This method generates a schema based on class instance
	 * OBS: It requires the instance in order to provide the custom feature of {@link Schematic} interface
	 * @param clazz - The class to be read
	 * @param clazz - The composition owner(some cases)
	 * @return Schema
	 */
	public Schema generate(Class<?> clazz, Class<?> owner){
		if(clazz == null) throw new IllegalStateException("The class cannot be null");
		//If implements Schematic interface, then use the customization
		if(Schematic.class.isAssignableFrom(clazz)){
			try{
				Method toSchema = clazz.getMethod(Schematic.METHOD);
				return (Schema) toSchema.invoke(null);
			}
			catch(Exception e){
				//In case of error, just continue with the code
				//As it will generate the default schema
			}
		}
		Schema schema = new Schema(clazz.getName());
		schema.setEntityName(clazz.getSimpleName().toLowerCase());
		schema.setCaption(genCaption(clazz.getSimpleName()));
		//Clazz scan
		while(clazz.getSuperclass() != null){
			//Breaks the iteration in case of java classes
			if(isJavaPkg(clazz))
				break;
			//Field scan
			for(Field field : clazz.getDeclaredFields()){
				Class<?> type = field.getType();
				//Should avoid final fields, xmlTransient and inverse relations
				if(Modifier.isFinal(field.getModifiers()) || field.getAnnotation(XmlTransient.class) != null || type.equals(owner))
					continue;
				Property property = new Property();
				//Check for java types and no primitives
				if(isJavaPkg(type)){
					//Collection field
					if(Collection.class.isAssignableFrom(type)){
						//Discover the generics type
						ParameterizedType generic = (ParameterizedType) field.getGenericType();
						Class<?> genericType = (Class<?>) generic.getActualTypeArguments()[0];
						
						//Recursively generate the child schema
						property.setChildSchema(generate(genericType, null));
					}
					//Array field
					else if(type.isArray()){
						//Discover the component type
						Class<?> componentType = type.getComponentType();
						
						//Recursively generate the child schema
						property.setChildSchema(generate(componentType, null));
					}
				}
				//Composed field
				else if(!type.isPrimitive()){
					//Make sure that its a camposition
					if(FieldType.getByField(field) == FieldType.COMPOSITION){
						//Recursively generate the child schema
						property.setChildSchema(generate(type, clazz));
					}
				}
				//Primitive and JavaLang fields will be filled here
				fillBasicProperties(property, field);
				schema.addProperty(property);
				
				//Sets the schema description property
				if(schema.getDescription() == null && field.getAnnotation(SchemaDescription.class) != null)
					schema.setDescription(property);
			}
			clazz = clazz.getSuperclass();
		}
		return schema;
	}
	
	/**
	 * Fills the basic values of the property
	 * @param property
	 * @param field
	 */
	private void fillBasicProperties(Property property, Field field){
		property.setType(FieldType.getByField(field));
		property.setCaption(genCaption(field.getName()));
		property.setName(field.getName());
		//Fill the values based on hibernate's Column annotation
		//But we could create our own, or even use the validation api
		Column column = field.getAnnotation(Column.class);
		if(column != null){
			property.setMaxlength(column.length());
			property.setRequired(!column.nullable());
			property.setPrecision(column.precision());
		}
		//Check for enumerated constant values
		if(FieldType.getByField(field) == FieldType.ENUMERATED){
			List<?> constants = Arrays.asList(field.getType().getEnumConstants());
			for (Object constant : constants) 
				property.addValue(constant.toString());
		}
	}
	
	/**
	 * Generates caption by field name.
	 * Ex: dateOfBirth = Date of Birth
	 * @param field
	 * @return
	 */
	public String genCaption(String value){
		char[] chars = value.toCharArray();
		String finalName = String.valueOf(chars[0]).toUpperCase();
		for(int index = 1;index < chars.length;index++){
			char c = chars[index];
			if(Character.isUpperCase(c))
				finalName += (" " + String.valueOf(c));
			else
				finalName += String.valueOf(c);
		}
		return finalName;
	}
	
	/**
	 * Check if its java package
	 * @param clazz
	 * @return
	 */
	private boolean isJavaPkg(Class<?> clazz){
		return StringUtil.hasText(clazz.getPackage()) && clazz.getPackage().getName().startsWith(JAVA_PKG_PREFIX);
	}
	
}