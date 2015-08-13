package br.com.smartsy.fwj.web.restful;

import java.util.Collections;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;

import br.com.smartsy.fwj.exception.RestfulException;
import br.com.smartsy.fwj.web.response.HttpResponse;

/**
 * Abstract class to create REST clients
 * @author Vagner
 *
 */
public abstract class AsbtractRestfulClient {
	
	private String serviceUrl;
	private MediaType mediaType = MediaType.APPLICATION_JSON_TYPE;

	/**
	 * Creates a new instance, with a service URL
	 * @param serviceUrl
	 */
	public AsbtractRestfulClient(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	
	/**
	 * Connect to the service/resource URL
	 * @return
	 */
	protected WebClient prepareClient(){
		@SuppressWarnings("rawtypes")
		JSONProvider provider = new JSONProvider();
		provider.setDropRootElement(true);
		provider.setSupportUnwrapped(true);
		provider.setIgnoreNamespaces(true);
		return WebClient.create(serviceUrl,Collections.singletonList(provider));
	}
	
	protected void validateResponse(Response response) throws RestfulException{
		int status = response.getStatus();
		if (status >= 300) {
			String msg;
			Object entity = response.getEntity();
			if(entity instanceof HttpResponse){
				HttpResponse httpMsg = (HttpResponse) response.getEntity();
				msg = httpMsg.getMessage();
			}
			else
				msg = response.getStatusInfo().getReasonPhrase();
			throw new RestfulException(status, msg);
		}
	}
	
	/**
	 * Gets the service URL
	 * @return
	 */
	public String getServiceUrl() {
		return serviceUrl;
	}
	
	/**
	 * Gets the accepted media type
	 * @return
	 */
	public MediaType getMediaType() {
		return mediaType;
	}
	
	/**
	 * Sets the service URL
	 * @param serviceUrl
	 */
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	
	/**
	 * Sets the accepted media type
	 */
	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

}
