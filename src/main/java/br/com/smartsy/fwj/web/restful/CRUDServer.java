package br.com.smartsy.fwj.web.restful;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import br.com.smartsy.fwj.exception.EntityNotFoundException;
import br.com.smartsy.fwj.exception.ServiceException;
import br.com.smartsy.fwj.mvc.service.Service;
import br.com.smartsy.fwj.web.response.HttpResponse;

/**
 * Utility for crud operations using restful design
 * @author Vagner
 *
 * @param <T>
 */
public abstract class CRUDServer<T> {

	private Service<T> service;

	public CRUDServer(Service<T> service) {
		this.service = service;
	}

	/**
	 * Creates a new resource
	 * @param resource
	 * @return
	 */
	public Response create(UriInfo uriInfo,T resource) {
		try {
			//Create resource then return its URL
			service.add(resource);
			Serializable id = getNewResourceIdentifier(resource);
			URI uri = uriInfo.getAbsolutePathBuilder().path(id.toString()).build();
			return Response.created(uri).build();
		} 
		catch (ServiceException e) {
			return Response.status(Status.BAD_REQUEST).entity(new HttpResponse(e.getMessage())).build();
		}
		catch(Exception e){
			return Response.serverError().entity(new HttpResponse(e.getMessage())).build();
		}
	}
	
	/**
	 * Updates an already existent resource by its ID
	 * <p>Return 204(no entity on response, but success)
	 * <p>OBS: Must use the mergeResource method to set the new values
	 * @param id
	 * @param resource
	 * @return
	 */
	public Response update(Serializable id,T resource) {
		try {
			T existent = service.get(id);
			mergeResource(resource, existent);
			service.alter(existent);
			return Response.noContent().build();
		} 
		catch (ServiceException e) {
			return Response.status(Status.BAD_REQUEST).entity(new HttpResponse(e.getMessage())).build();
		}
		catch(EntityNotFoundException e){
			return Response.status(Status.NOT_FOUND).build();
		}
		catch(Exception e){
			return Response.serverError().entity(new HttpResponse(e.getMessage())).build();
		}
	}
	
	/**
	 * Deletes an existent entity by its ID
	 * <p>Return 204(no entity on response, but success)
	 * @param id
	 * @return
	 */
	public Response delete(Serializable id) {
		try {
			T existent = service.get(id);
			service.delete(existent);
			return Response.noContent().build();
		} 
		catch (ServiceException e) {
			return Response.status(Status.BAD_REQUEST).entity(new HttpResponse(e.getMessage())).build();
		}
		catch(EntityNotFoundException e){
			return Response.status(Status.NOT_FOUND).build();
		}
		catch(Exception e){
			return Response.serverError().entity(new HttpResponse(e.getMessage())).build();
		}
	}
	
	/**
	 * Gets an existent entity by its ID
	 * @param id
	 * @return
	 */
	public Response get(Serializable id) {
		try {
			return Response.ok().entity(service.get(id)).build();
		} 
		catch (ServiceException e) {
			return Response.status(Status.BAD_REQUEST).entity(new HttpResponse(e.getMessage())).build();
		}
		catch(EntityNotFoundException e){
			return Response.status(Status.NOT_FOUND).build();
		}
		catch(Exception e){
			return Response.serverError().entity(new HttpResponse(e.getMessage())).build();
		}
	}
	
	/**
	 * Lists the entity records and wrapps it
	 * @param id
	 * @return List
	 */
	public Response list() {
		try {
			return Response.ok().entity(wrapCollection(service.listAll())).build();
		} 
		catch (ServiceException e) {
			return Response.status(Status.BAD_REQUEST).entity(new HttpResponse(e.getMessage())).build();
		}
		catch(EntityNotFoundException e){
			return Response.ok().build();
		}
		catch(Exception e){
			return Response.serverError().entity(new HttpResponse(e.getMessage())).build();
		}
	}

	/**
	 * Gets the service being used
	 * 
	 * @return The service used
	 */
	@SuppressWarnings("unchecked")
	protected <E extends Service<T>> E getService() {
		return (E) this.service;
	}
	
	/**
	 * Wraps a collection into a customized class
	 * @param collection
	 * @return Wrapped collection
	 */
	protected abstract Object wrapCollection(List<T> collection);
	
	/**
	 * Called after creating the new resource
	 * <p>The method must return the new resource ID
	 * @param resource
	 * @return
	 */
	protected abstract Serializable getNewResourceIdentifier(T resource);
	
	/**
	 * Asbtract method that merges resources
	 * <p>Must be implemented in order to update the entity
	 * @param resource
	 * @param existent
	 */
	protected abstract void mergeResource(T resource,T existent);
}