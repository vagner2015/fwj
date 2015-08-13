package br.com.smartsy.fwj.mvc.resource;

import java.io.Serializable;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import br.com.smartsy.fwj.persistence.CustomQuery;

public interface Resource<T> {

	/**
	 * Creates a new resource
	 * @param resource
	 * @return
	 */
	public abstract Response create(UriInfo uriInfo, T resource);

	/**
	 * Updates an already existent resource by its ID
	 * <p>Return 204(no entity on response, but success)
	 * <p>OBS: Must use the mergeResource method to set the new values
	 * @param id
	 * @param resource
	 * @return
	 */
	public abstract Response update(Serializable id, T resource);

	/**
	 * Deletes an existent entity by its ID
	 * <p>Return 204(no entity on response, but success)
	 * @param id
	 * @return
	 */
	public abstract Response delete(Serializable id);

	/**
	 * Gets an existent entity by its ID
	 * @param id
	 * @return
	 */
	public abstract Response get(Serializable id);

	/**
	 * Lists the entity records and wrapps it
	 * @param id
	 * @return List
	 */
	public abstract Response search(CustomQuery customQuery);
	
	/**
	 * Lists all the entity records and wrapps it
	 * @param id
	 * @return List
	 */
	public abstract Response listAll();
	
	/**
	 * Lists all the entity records, paginate and wrapps it
	 * @param id
	 * @param offset
	 * @param length
	 * @return List
	 */
	public abstract Response listAll(Integer offset, Integer length);
	
	/**
	 * Generates the entity schema
	 * @return Schema
	 */
	public abstract Response getSchema();

}