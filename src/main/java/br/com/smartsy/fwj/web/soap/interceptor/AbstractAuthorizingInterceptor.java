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

import br.com.smartsy.fwj.security.AccessLevelResolver;
import br.com.smartsy.fwj.web.common.Endpoint;

/**
 * Abstract interceptor made for resource access authorization
 * @author Vagner
 *
 */
public abstract class AbstractAuthorizingInterceptor extends AbstractSoapInterceptor {
	
	private static final String SERVICE_INFO = "javax.xml.ws.wsdl.interface";
	private static final String OPERATION_INFO = "org.apache.cxf.service.model.MessageInfo";
	private final String basePackage;
	
	public AbstractAuthorizingInterceptor(String basePackage) {
		super(Phase.PRE_INVOKE);
		this.basePackage = basePackage;
	}

	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		try{
			Class<Endpoint> clazz = getServiceClass(message);
			Method operation = getServiceMethod(message, clazz);
			int level = AccessLevelResolver.resolve(clazz, operation);
			if(!shouldGrant(message, level))
				throw new Fault(new FaultInfoException("Permission Denied"));
		}
		catch(ClassNotFoundException e){
			throw new Fault(new FaultInfoException("Authorizing Problems " + e.getMessage()));
		}
	}
	
	/**
	 * Check if should grant the webservice access based 
	 * on the identified level of the requester
	 * @param message
	 * @param level
	 * @return
	 */
	protected abstract boolean shouldGrant(SoapMessage message,int level);
	
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
	
}