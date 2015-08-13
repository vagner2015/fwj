package br.com.smartsy.fwj.web.soap.interceptor;

import java.lang.reflect.Method;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.frontend.FaultInfoException;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.model.MessageInfo;

import br.com.smartsy.fwj.exception.TokenAuthException;
import br.com.smartsy.fwj.security.AccessLevel;
import br.com.smartsy.fwj.security.AccessLevelResolver;
import br.com.smartsy.fwj.web.common.Endpoint;
import br.com.smartsy.fwj.web.soap.SoapHeader;
import br.com.smartsy.fwj.web.soap.TokenAuthHeader;

/**
 * Class for token based authentication abstraction
 * @author Vagner
 *
 */
public abstract class AbstractTokenAuthInterceptor extends AbstractSoapInterceptor {
	
	protected static final Fault FAULT_BLANK_TOKEN = new Fault(new RuntimeException("An access token must be declared on the soap envelope header"));
	protected static final Fault FAULT_INVALID_TOKEN = new Fault(new RuntimeException("Invalid access token"));
	
	private static final String SERVICE_INFO = "javax.xml.ws.wsdl.interface";
	private static final String OPERATION_INFO = "org.apache.cxf.service.model.MessageInfo";
	private final String basePackage;
	
	public AbstractTokenAuthInterceptor(String basePackage) {
		super(Phase.PRE_INVOKE);
		this.basePackage = basePackage;
	}
	
	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		//In case of free access, should avoid authentication
		if(isFreeAccess(message)) 
			return;
		String token = getToken(message);
		try {
			validateToken(token);
		} 
		catch (TokenAuthException e) {
			throw new Fault(e);
		}
	}
	
	/**
	 * Verify if the token is valid
	 * @param token
	 * @throws TokenAuthException
	 */
	protected abstract void validateToken(String token) throws TokenAuthException;
	
	/**
	 * Verifiy if the destination has free access
	 * @param message
	 * @return True/False
	 */
	private boolean isFreeAccess(SoapMessage message){
		try{
			Class<Endpoint> clazz = getServiceClass(message);
			Method operation = getServiceMethod(message, clazz);
			return AccessLevelResolver.resolve(clazz, operation) == AccessLevel.NONE;
		}
		catch(ClassNotFoundException e){
			throw new Fault(new FaultInfoException("Authentication Problems " + e.getMessage()));
		}
	}
	
	/**
	 * Recovers the target webservice implementation class
	 * @param soapMsg
	 * @return Class
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private Class<Endpoint> getServiceClass(SoapMessage soapMsg) throws ClassNotFoundException{
		Message inMsg = soapMsg.getExchange().getInMessage();
		QName serviceInfo = (QName) inMsg.get(SERVICE_INFO);
		return (Class<Endpoint>) Class.forName(basePackage + "." + serviceInfo.getLocalPart());
	}
	
	/**
	 * Recovers the target webservice method being called
	 * @param soapMsg
	 * @param endpointClazz
	 * @return Method
	 */
	private Method getServiceMethod(SoapMessage soapMsg, Class<Endpoint> endpointClazz){
		Message inMsg = soapMsg.getExchange().getInMessage();
		MessageInfo operationInfo = (MessageInfo) inMsg.get(OPERATION_INFO);
		String operation = operationInfo.getOperation().getInputName();
		for(Method method : endpointClazz.getMethods())
			if(method.getName().equals(operation))
				return method;
		return null;
	}
	
	private String getToken(SoapMessage message) {
	    SoapHeader header = new SoapHeader(message);
	    TokenAuthHeader authHeader = header.getTokenAuthHeader();
	    if(authHeader != null)
	    	return authHeader.getToken();
	    return null;
	}
	
}