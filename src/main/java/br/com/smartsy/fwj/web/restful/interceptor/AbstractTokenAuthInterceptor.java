package br.com.smartsy.fwj.web.restful.interceptor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

import br.com.smartsy.fwj.exception.AuthenticationException;
import br.com.smartsy.fwj.security.AccessLevel;
import br.com.smartsy.fwj.security.AccessLevelResolver;
import br.com.smartsy.fwj.util.CastUtil;
import br.com.smartsy.fwj.util.StringUtil;

/**
 * Class for token based authentication abstraction
 * @author Vagner
 *
 */
public abstract class AbstractTokenAuthInterceptor extends AbstractPhaseInterceptor<Message>{
	
	private static final String KEY_RESOURCE_CLASS = "root.resource.class";
	private static final String KEY_RESOURCE_METHOD = "org.apache.cxf.resource.method";
	
	protected static final AuthenticationException ERROR_BLANK_TOKEN_HEADER = new AuthenticationException("An access token must be declared on the http header");
	protected static final AuthenticationException ERROR_BLANK_TOKEN_QUERY = new AuthenticationException("An access token must be declared on the query string");
	protected static final AuthenticationException ERROR_INVALID_TOKEN = new AuthenticationException("Invalid access token");

	public AbstractTokenAuthInterceptor() {
		super(Phase.PRE_INVOKE);
	}
	
	@Override
	public void handleMessage(Message message) throws Fault {
		//In case of free access, should avoid authentication
		if(isFreeAccess(message)) 
			return;
		HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
		String token = getToken(message,request);
		try {
			validateToken(token,request.getMethod());
		} 
		catch (AuthenticationException e) {
			//Rest response
//			Response response = Response.status(Status.FORBIDDEN).entity(new HttpResponse(e.getMessage())).build();
//			message.getExchange().put(Response.class, response);
			throw new Fault(e);
		}
	}
	
	/**
	 * Validates the token
	 * @param token - Access Token
	 * @param method - The HTTP Method
	 * @throws AuthenticationException
	 */
	protected abstract void validateToken(String token,String method) throws AuthenticationException;
	
	/**
	 * Gets the token header parameter name
	 * @return Parameter name
	 */
	protected abstract String getTokenHeaderName();
	
	/**
	 * Gets the token query string parameter name
	 * @return Parameter name
	 */
	protected abstract String getTokenQueryName();
	
	private String getToken(Message message,HttpServletRequest request) {
		String token = "";
		Map<String, List<String>> headers = CastUtil.cast((Map<?, ?>) message.get(Message.PROTOCOL_HEADERS));
		List<String> tokenHeader = headers.get(getTokenHeaderName());
		if(tokenHeader != null && tokenHeader.size() > 0)
			token = tokenHeader.get(0);
		if(!StringUtil.hasText(token)){
			token = request.getParameter(getTokenQueryName());
		}
		return token;
	}
	
	/**
	 * Check if the target destination has free access
	 * @param message
	 * @return True/False
	 */
	private boolean isFreeAccess(Message message){
		return AccessLevelResolver.resolve(getResourceClass(message), getResourceMethod(message)) == AccessLevel.NONE;
	}
	
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