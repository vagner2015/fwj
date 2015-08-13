package br.com.smartsy.fwj.web.restful.interceptor;

import java.lang.reflect.Method;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import br.com.smartsy.fwj.exception.AuthorizationException;
import br.com.smartsy.fwj.security.AccessLevelResolver;

/**
 * Abstract interceptor made for resource access authorization
 * @author Vagner
 *
 */
public abstract class AbstractAuthorizingInterceptor extends AbstractPhaseInterceptor<Message>{
	
	@Autowired
	private ApplicationContext context;
	
	private static final String KEY_RESOURCE_CLASS = "root.resource.class";
	private static final String KEY_RESOURCE_METHOD = "org.apache.cxf.resource.method";

	public AbstractAuthorizingInterceptor() {
		super(Phase.PRE_INVOKE);
	}
	
	@Override
	public void handleMessage(Message message) throws Fault {
		try{
			if(!shouldGrant(message, AccessLevelResolver.resolve(getResourceClass(message), getResourceMethod(message))))
				throw new AuthorizationException("Permission Denied");
		}
		catch(Exception e){
			//Rest response
//			Response response = Response.status(Status.UNAUTHORIZED).entity(new HttpResponse(getExceptionMessage(e))).build();
//			message.getExchange().put(Response.class, response);
			if(e instanceof Fault){
				throw (Fault) e;
			}
			throw new Fault(e);
		}
	}
	
//	private String getExceptionMessage(Throwable throwable){
//		if(throwable instanceof Fault)
//			return throwable.getCause().getMessage();
//		return throwable.getMessage();
//	}
	
	/**
	 * Check if should grant access for the identified level of access
	 * @param message
	 * @param level
	 * @return True/False
	 */
	protected abstract boolean shouldGrant(Message message,int level);
	
	/**
	 * Recover the target resource class
	 * @param message
	 * @return Target Resource Class
	 */
	private Class<?> getResourceClass(Message message){
		Class<?> clazz = null;
		Object value = message.getExchange().get(KEY_RESOURCE_CLASS);
		if(value != null)
			if(value instanceof ClassResourceInfo)
				clazz = ((ClassResourceInfo)value).getResourceClass();
		return  clazz;
	}
	
	/**
	 * Recover the target method of the resource class
	 * @param message
	 * @return Target Method
	 */
	private Method getResourceMethod(Message message){
		Object method = message.getExchange().getInMessage().getContextualProperty(KEY_RESOURCE_METHOD);
		if(method != null)
			return (Method)method;
		return null;
	}
}