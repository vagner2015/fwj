package br.com.smartsy.fwj.security;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A token representation - Used for unique values.
 * OBS: Use the {@link TokenGenerator} class to randomize a token creation.
 * @author Vagner
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD) 
@Embeddable
@Access(AccessType.PROPERTY)
public class Token implements Serializable {

	private static final long serialVersionUID = 6767128112648618397L;

	private String value;
	private String pattern;

	public Token() {
		this("",".*");
	}

	public Token(String value,String pattern) {
		setValue(value);
		setPattern(pattern);
	}

	public String getValue() {
		return String.valueOf(value);
	}

	@Transient
	public void setValue(String value) {
		this.value = String.valueOf(value);
	}
	
	@Transient
	public String getPattern() {
		return pattern;
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	/**
	 * Validate if the token is valid, considering its pattern
	 * @return
	 */
	@Transient
	public boolean isValid(){
		return getValue().matches(pattern);
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
