package br.com.smartsy.fwj.bean.render;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class for entitiy fields description
 * 
 * @author Vagner
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Schema implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The entity
	 * Ex: org.tempuri.Example
	 */
	@XmlElement(name="entity",required=false)
	private String entity;
	
	/**
	 * The entity simple name
	 * Ex: org.tempuri.Example = Example
	 */
	@XmlElement(name="entityName",required=false)
	private String entityName;
	
	/**
	 * The schema caption
	 */
	@XmlElement(name="caption",required=false)
	private String caption;
	
	/**
	 * The schema property wich describes it
	 */
	@XmlElement(name="description",required=false)
	private Property description;
	
	/**
	 * The schema properties
	 */
	@XmlElement(name="properties",required=false)
	private List<Property> properties = new ArrayList<Property>();
	
	public Schema() {
		// TODO Auto-generated constructor stub
	}
	
	public Schema(String entity) {
		this.entity = entity;
	}

	public String getEntity() {
		return entity;
	}
	
	public String getEntityName() {
		return entityName;
	}

	public String getCaption() {
		return caption;
	}
	
	public Property getDescription() {
		return description;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}
	
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public void setDescription(Property description) {
		this.description = description;
	}

	public void addProperty(Property property){
		if(property != null)
			this.properties.add(property);
	}

}