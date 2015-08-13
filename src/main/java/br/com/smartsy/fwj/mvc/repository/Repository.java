package br.com.smartsy.fwj.mvc.repository;

import java.io.Serializable;
import java.util.List;

import br.com.smartsy.fwj.exception.EntityExistsException;
import br.com.smartsy.fwj.exception.EntityNotFoundException;
import br.com.smartsy.fwj.exception.RepositoryException;
import br.com.smartsy.fwj.persistence.CustomQuery;

/**
 * DAO(Direct Access Object) specification
 * @author Vagner
 *
 * @param <T>
 *            - The entity type
 */
public interface Repository<T> {
	
	/**
	 * Resolves the entity name
	 * @return The entity name
	 * @author Vagner
	 */
	public String getEntityName();

	/**
	 * Retrieves an entity by its id
	 * 
	 * @param id
	 *            - identifier of the current entity
	 * @return An entity found by the identifier
	 * @throws RepositoryException
	 *             - If an error occurs during the operation
	 * @throws EntityNotFoundException
	 * 			   - If the entity cannot be found
	 * @author Vagner
	 */
	public abstract T get(Serializable id) throws RepositoryException, EntityNotFoundException;

	/**
	 * Identify all the occurencies of the current entity on the database,
	 * limited by a first result index and the max results to be collected
	 * 
	 * @param firstResult
	 *            - The first row index
	 * @param maxResults
	 *            - Maximum number of rows to be collected
	 * @return List of values of the current entity
	 * @throws RepositoryException
	 *             - If an error occurs during the operation
	 * @throws EntityNotFoundException
	 * 			   - If the entity cannot be found            
	 * @author Vagner
	 */
	public abstract List<T> listAll(Integer firstResult, Integer maxResults) throws RepositoryException, EntityNotFoundException;

	/**
	 * Identify all the occurencies of the current entity on the database,
	 * limited a max number of results to be collected
	 * 
	 * @param maxResults
	 *            - Maximum number of rows to be collected
	 * @return List of values of the current entity
	 * @throws RepositoryException
	 *             - If an error occurs during the operation
	 * @throws EntityNotFoundException
	 * 			   - If the entity cannot be found
	 * @author Vagner
	 */
	public abstract List<T> listAll(Integer maxResults) throws RepositoryException, EntityNotFoundException;

	/**
	 * Collect all the occurencies of the entity on the database
	 * 
	 * @return List of values of the current entity
	 * @throws RepositoryException
	 *             - If an error occurs during the operation
	 * @throws EntityNotFoundException
	 * 			   - If the entity cannot be found
	 * @author Vagner
	 */
	public abstract List<T> listAll() throws RepositoryException, EntityNotFoundException;
	
	/**
	 * Executes a custom query
	 * @param query
	 * @return List of the entities found
	 * @throws RepositoryException 
	 * @throws EntityNotFoundException 
	 */
	public abstract List<T> customQuery(CustomQuery query) throws RepositoryException, EntityNotFoundException;
	
	/**
	 * Execute hql query on the database
	 * 
	 * @param hql
	 *            - The query to be executed
	 * @param firstResult
	 *            - The first row index
	 * @param maxResults
	 *            - Maximum number of rows to be collected
	 * @return The hql query result
	 * @throws RepositoryException
	 *             - If an error occurs during the operation
	 * @throws EntityNotFoundException
	 * 			   - If the entity cannot be found
	 * @author Vagner
	 */
	public abstract List<T> hqlQuery(String hql, Integer firstResult, Integer maxResults, boolean handleConnection) throws RepositoryException, EntityNotFoundException;
	
	/**
	 * Execute hql query on the database
	 * 
	 * @param hql
	 *            - The query to be executed
	 * @param firstResult
	 *            - The first row index
	 * @param maxResults
	 *            - Maximum number of rows to be collected
	 * @return The hql query result
	 * @throws RepositoryException
	 *             - If an error occurs during the operation
	 * @throws EntityNotFoundException
	 * 			   - If the entity cannot be found
	 * @author Vagner
	 */
	public abstract List<T> hqlQuery(String hql, Integer firstResult, Integer maxResults) throws RepositoryException, EntityNotFoundException;

	/**
	 * Execute hql query on the database
	 * 
	 * @param hql
	 *            - The query to be executed
	 * @param maxResults
	 *            - Maximum number of rows to be collected
	 * @return The hql query result
	 * @throws RepositoryException
	 *             - If an error occurs during the operation
	 * @throws EntityNotFoundException
	 * 			   - If the entity cannot be found
	 * @author Vagner
	 */
	public abstract List<T> hqlQuery(String hql, Integer maxResults) throws RepositoryException, EntityNotFoundException;

	/**
	 * Execute hql query on the database
	 * 
	 * @param hql
	 *            - The query to be executed
	 * @return The hql query result
	 * @throws RepositoryException
	 *             - If an error occurs during the operation
	 * @author Vagner
	 */
	public abstract List<T> hqlQuery(String hql) throws RepositoryException, EntityNotFoundException;
	
	/**
	 * Execute the hql query on the database returning the first row
	 * 
	 * @param hql
	 *            - The query to be executed
	 * @return The result first row
	 * @throws RepositoryException
	 *             - If an error occurs during the operation
	 * @throws EntityNotFoundException
	 * 			   - If the entity cannot be found
	 * @author Vagner
	 */
	public abstract T hqlQuerySingleResult(String hql) throws RepositoryException, EntityNotFoundException;
	
	/**
	 * Save or Update the current entity on the database
	 * @param entity - The entity to be persisted 
	 * @throws RepositoryException - If something wrong happens
	 * @throws EntityExistsException - If the entity already exist
	 */
	public abstract void saveOrUpdate(T entity) throws RepositoryException, EntityExistsException;
	
	/**
	 * Remove the entity from the database
	 * @param entity - The entity to be removed
	 * @throws RepositoryException - If something wrong happens
	 */
	public abstract void delete(T entity) throws RepositoryException;


}