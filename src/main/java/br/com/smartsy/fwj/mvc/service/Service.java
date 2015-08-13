package br.com.smartsy.fwj.mvc.service;

import java.io.Serializable;
import java.util.List;

import br.com.smartsy.fwj.exception.EntityExistsException;
import br.com.smartsy.fwj.exception.EntityNotFoundException;
import br.com.smartsy.fwj.exception.ServiceException;
import br.com.smartsy.fwj.persistence.CustomQuery;

/**
 * Service(Business Logic) specification
 * @author Vagner
 *
 * @param <T>
 *            - The entity type
 */
public interface Service<T> {

	/**
	 * Execute the add statement of the entity on the model
	 * @param entity - The entity to be added
	 * @throws ServiceException 
	 * @throws EntityExistsException - If the entity already exists
	 */
	public abstract void add(T entity) throws ServiceException, EntityExistsException;
	
	/**
	 * Execute the alter/update statement on the entity of the model
	 * @param entity - The entity to be modified
	 * @throws ServiceException 
	 */
	public abstract void alter(T entity) throws ServiceException;
	
	/**
	 * Execute the remove statement on the entity of the model
	 * @param entity - The entity to be removed
	 * @throws ServiceException 
	 */
	public abstract void delete(T entity) throws ServiceException;
	
	/**
	 * Execute the get statement using the identifier to find the entity on the model
	 * @param identifier
	 * @return The entity found
	 * @throws ServiceException 
	 */
	public abstract T get(Serializable identifier) throws ServiceException, EntityNotFoundException;
	
	/**
	 * Execute the list statement returning a collection of the current entity of the model with a limited number of results and a first entity index to begin collecting
	 * @return List of entities starting by a pre-defined first result index and limited by a maximum number of results
	 * @throws ServiceException 
	 */
	public abstract List<T> listAll(Integer firstResult,Integer maxResults) throws ServiceException, EntityNotFoundException;
	
	/**
	 * Execute the list statement returning a collection of the current entity of the model with a limited number of results
	 * @return List of entities limited by a maximum number of results
	 * @throws ServiceException 
	 */
	public abstract List<T> listAll(Integer maxResults) throws ServiceException, EntityNotFoundException;
	
	/**
	 * Execute the list statement returning a collection of the current entity of the model
	 * @return List of all existent entities
	 * @throws ServiceException 
	 */
	public abstract List<T> listAll() throws ServiceException, EntityNotFoundException;
	
	/**
	 * Execute the custom query returning a collection of the current entity of the model
	 * @return List of existent entities based on the custom query
	 * @throws ServiceException 
	 */
	public abstract List<T> customSearch(CustomQuery customQuery) throws ServiceException, EntityNotFoundException;
	
}
