package br.com.smartsy.fwj.bean.render;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.smartsy.fwj.util.StringUtil;

/**
 * Class for schema properties
 * 
 * @author Vagner
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Property implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Field type
	 * Ex: text,email,numeric, list...
	 */
	@XmlElement(name="type",required=false)
	private FieldType type = FieldType.TEXT;
	
	/**
	 * Field name
	 */
	@XmlElement(name="name",required=false)
	private String name = "[property_name]";
	
	/**
	 * Field caption for label
	 */
	@XmlElement(name="caption",required=false)
	private String caption = "[property_caption]";
	
	/**
	 * Field value maxlength
	 */
	@XmlElement(name="maxlength",required=false)
	private int maxlength = 255;
	
	/**
	 * Field value maxlength
	 */
	@XmlElement(name="precision",required=false)
	private int precision = 0;
	
	/**
	 * Field nullable or not
	 */
	@XmlElement(name="required",required=false)
	private boolean required = true;
	
	/**
	 * Regular expression for pattern validation
	 */
	@XmlElement(name="regex",required=false)
	private String regex = "";
	
	/**
	 * Values for special cases
	 */
	@XmlElement(name="values",required=false)
	private List<String> values = new ArrayList<String>();
	
	/**
	 * Child schema for multivalued type
	 */
	@XmlElement(name="childSchema",required=false)
	private Schema childSchema = null;

	public FieldType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getCaption() {
		return caption;
	}

	public int getMaxlength() {
		return maxlength;
	}

	public int getPrecision() {
		return precision;
	}

	public boolean isRequired() {
		return required;
	}

	public String getRegex() {
		return regex;
	}
	
	public List<String> getValues() {
		return values;
	}

	public Schema getChildSchema() {
		return childSchema;
	}

	public void setType(FieldType type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public void setMaxlength(int maxlength) {
		this.maxlength = maxlength;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}
	
	public void setValues(List<String> values) {
		this.values = values;
	}
	
	public void addValue(String value){
		if(StringUtil.hasText(value))
			this.values.add(value);
	}

	public void setChildSchema(Schema childSchema) {
		this.childSchema = childSchema;
	}

}