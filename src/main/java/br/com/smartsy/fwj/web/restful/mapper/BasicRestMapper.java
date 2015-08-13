package br.com.smartsy.fwj.web.restful.mapper;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.com.smartsy.fwj.exception.AuthenticationException;
import br.com.smartsy.fwj.exception.AuthorizationException;
import br.com.smartsy.fwj.exception.EntityExistsException;
import br.com.smartsy.fwj.exception.EntityNotFoundException;
import br.com.smartsy.fwj.web.response.HttpResponse;

/**
 * Maps general exceptions to jaxrs responses
 * @author Vagner
 *
 */
@Provider
@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
public class BasicRestMapper implements ExceptionMapper<Throwable> {
	
	@Override
	public Response toResponse(Throwable throwable) {
		//Invalid Credentials(forbidden)
		if(throwable instanceof AuthenticationException)
			return build(Status.FORBIDDEN, throwable.getMessage());
		
		//Permission Denied(unauthorized)
		if(throwable instanceof AuthorizationException)
			return build(Status.UNAUTHORIZED, throwable.getMessage());
		
		//Entity doesn't exist(not_found)
		if(throwable instanceof EntityNotFoundException)
			return build(Status.NOT_FOUND, throwable.getMessage());
		
		//Entity already exist(conflict)
		if(throwable instanceof EntityExistsException)
			return build(Status.CONFLICT, throwable.getMessage());
		
		//Unexpected error(server_error)
		return build(Status.INTERNAL_SERVER_ERROR, throwable.getMessage());
	}
	
	private Response build(Status status, String msg){
		return Response.status(status).entity(new HttpResponse(msg)).build();
	}

}