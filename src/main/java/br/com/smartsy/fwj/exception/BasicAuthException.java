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
@WebFault(name = "BasicAuthException")
public class BasicAuthException extends Exception {

	private static final long serialVersionUID = 519182019509086386L;
	
	private BasicFaultInfo fault;

	public BasicAuthException() {
		super();
	}

	public BasicAuthException(BasicFaultInfo fault) {
		super(fault.getMessage());
		this.fault = fault;
	}

	public BasicAuthException(String msg, BasicFaultInfo fault) {
		super(msg);
		this.fault = fault;
	}
	
	public BasicFaultInfo getFaultInfo(){
		return fault;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class BasicFaultInfo {

		@XmlAttribute
		private String message;

		private Date date;

		public BasicFaultInfo() {
			
		}
		
		public BasicFaultInfo(String message) {
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
