package br.com.smartsy.fwj.web.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Object for http responses encapsulation
 * @author Vagner
 *
 */
@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.FIELD)
public class HttpResponse implements Serializable{

	private static final long serialVersionUID = -5023900654918295556L;
	
	@XmlElement(name="message",required=false,defaultValue="nothing")
	private String message;
	
	public HttpResponse() {
		
	}

	public HttpResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
