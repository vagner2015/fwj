package br.com.smartsy.fwj.web.soap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Basic authentication header
 * @author Vagner
 *
 */
@XmlRootElement(name="BasicAuth")
@XmlAccessorType(XmlAccessType.NONE)
public class BasicAuthHeader {
	
	@XmlTransient
	public static final String TAG_HEADER = "BasicAuth";
	@XmlTransient
	public static final String TAG_USERNAME = "username";
	@XmlTransient
	public static final String TAG_PASSWORD = "password";

	@XmlElement(name="username",required=true,type=String.class)
	private String username;
	
	@XmlElement(name="password",required=true,type=String.class)
	private String password;
	
	public BasicAuthHeader() {
		
	}
	
	public BasicAuthHeader(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
