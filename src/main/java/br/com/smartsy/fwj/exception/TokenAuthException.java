package br.com.smartsy.fwj.exception;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.ws.WebFault;

/**
 * Exception used to indicate a fault on AuthenticationWS
 * @author Vagner
 * 
 */
@WebFault(name = "TokenAuthException")
public class TokenAuthException extends Exception {

	private static final long serialVersionUID = 519182019509086386L;
	
	private TokenFaultInfo fault;

	public TokenAuthException() {
		super();
	}

	public TokenAuthException(TokenFaultInfo fault) {
		super(fault.getMessage());
		this.fault = fault;
	}

	public TokenAuthException(String msg, TokenFaultInfo fault) {
		super(msg);
		this.fault = fault;
	}
	
	public TokenFaultInfo getFaultInfo(){
		return fault;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class TokenFaultInfo {

		@XmlAttribute
		private String message;

		private Date date;

		public TokenFaultInfo() {
			
		}
		
		public TokenFaultInfo(String message) {
			this.message = message;
			this.date = new Date();
		}

		public String getMessage() {
			return message;
		}

		public Date getDate() {
			return date;
		}
	}
}
