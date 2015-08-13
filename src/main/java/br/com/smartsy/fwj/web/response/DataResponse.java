package br.com.smartsy.fwj.web.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.tika.config.TikaConfig;

/**
 * Object for data responses encapsulation
 * @author Vagner
 *
 */
@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataResponse implements Serializable {

	private static final long serialVersionUID = 8012459454747554718L;

	@XmlElement(name="message", required=false, defaultValue="")
	private String message;
	
	@XmlElement(name="description", required=false, defaultValue="")
	private String description;
	
	@XmlElement(name="contentType", required=false, defaultValue="")
	private String contentType;
	
	@XmlElement(name="data", required=false, defaultValue="")
	private byte[] data;

	public DataResponse() {
		// TODO Auto-generated constructor stub
	}

	public DataResponse(String message, String description, String contentType, byte[] data) {
		this.message = message;
		this.description = description;
		this.contentType = contentType;
		this.data = data;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return description;
	}

	public String getContentType() {
		return contentType;
	}

	public byte[] getData() {
		return data;
	}
	
	public String getExtension(){
		String mime = getContentType();
		if(mime.contains("x+"))
			mime = mime.replaceAll("x\\+", "");
		TikaConfig config = TikaConfig.getDefaultConfig();
		try{
			return config.getMimeRepository().forName(mime).getExtension();
		}
		catch(Exception e){
			return "";
		}
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}