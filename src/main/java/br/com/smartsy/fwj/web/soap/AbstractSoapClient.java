package br.com.smartsy.fwj.web.soap;

import java.net.MalformedURLException;
import java.util.Map;

import br.com.smartsy.fwj.util.GenericsUtil;
import br.com.smartsy.fwj.web.common.Endpoint;

/**
 * Abstract Webservice Client
 * @author Vagner
 *
 * @param <T> - The endpoint type
 */
public abstract class AbstractSoapClient<T extends Endpoint> {

	protected EndpointFactory factory;
	private Class<T> clazz;
	
	public AbstractSoapClient(String url,String namespace,String serviceName) throws MalformedURLException {
		this.factory = new EndpointFactory(url, namespace, serviceName);
		this.clazz = GenericsUtil.resolveGenericType(this.getClass());
	}
	
	/**
	 * Get endpoint for the service
	 * @return
	 */
	public T getEndpoint(){
		return factory.createEndpoint(clazz, null);
	}
	
	/**
	 * Get endpoint with headers
	 * @param headerParams
	 * @return
	 */
	public T getEndpoint(Map<String,String> headerParams){
		return factory.createEndpoint(clazz, headerParams);
	}
	
	/**
	 * Set header on the port
	 * @param endpoint
	 * @param headerParams
	 */
	public void setHeader(T endpoint, Map<String,String> headerParams){
		EndpointFactory.setHttpHeaders(endpoint, headerParams);
	}
	
}
