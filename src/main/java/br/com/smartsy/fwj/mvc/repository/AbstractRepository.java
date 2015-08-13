package br.com.smartsy.fwj.mvc.repository;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

import br.com.smartsy.fwj.exception.EntityExistsException;
import br.com.smartsy.fwj.exception.EntityNotFoundException;
import br.com.smartsy.fwj.exception.RepositoryException;
import br.com.smartsy.fwj.persistence.CustomQuery;
import br.com.smartsy.fwj.persistence.function.QueryFunction;
import br.com.smartsy.fwj.security.Encrypter;
import br.com.smartsy.fwj.util.GenericsUtil;
import br.com.smartsy.fwj.util.RandomicGenerator;
import br.com.smartsy.fwj.util.StringUtil;
import br.com.smartsys.fwj.annotation.Encrypt;
import br.com.smartsys.fwj.annotation.Random;

/**
 * System Abstact Repository
 * <p>Responsible for managing transactions between application and database
 * 
 * @author Vagner
 * 
 * @param <T>
 *            - The entity type
 */
public abstract class AbstractRepository<T> implements Repository<T> {

	protected static final Logger log = Logger.getLogger(AbstractRepository.class);

	protected final EntityManagerFactory emf;
	private static ThreadLocal<EntityManager> emThreadLocal = new ThreadLocal<EntityManager>();;
	private final Boolean isJta;

	public AbstractRepository(EntityManagerFactory factory) {
		this.isJta = factory.getProperties().get("javax.persistence.jtaDataSource") != null;
		this.emf = factory;
	}
	
	protected void setEntityManager(EntityManager em) {
		AbstractRepository.emThreadLocal.set(em);
	}

	/**
	 * Gets the current thread connection
	 * @return Entity Manager(local thread)
	 */
	protected EntityManager getEntityManager() {
		return AbstractRepository.emThreadLocal.get();
	}
	
	/**
	 * Gets the entity-manager factory
	 * @return EntityManagerFactory
	 */
	protected EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}
	
	/**
	 * Get the entity class type
	 * @return
	 */
	public Class<T> getEntityType(){
		return GenericsUtil.resolveGenericType(this.getClass());
	}
	
	/**
	 * Resolves the current entity name
	 */
	@Override
	public String getEntityName() {
		Class<T> clazz = getEntityType();
		String name = clazz.getAnnotation(Entity.class).name();
		if(!StringUtil.hasText(name))
			name = clazz.getSimpleName();
		return name;
	}
	
	/**
	 * Select the entity by its id
	 */
	@Override
	public T get(Serializable id) throws RepositoryException, EntityNotFoundException {
		Class<T> clazz = getEntityType();
		T entity = null;
		try {
			begin();
			entity = getEntityManager().find(clazz, id);
		} catch (Exception e) {
			String msg = "An unexpected error occured while trying to search the entity '" + clazz.getSimpleName() + "' by its ID: "+e.getMessage();
			log.error(msg, e);
			throw new RepositoryException(msg, e);
		} finally {
			close();
		}
		if (entity == null)
			throw new EntityNotFoundException("Couldn't find the entity '" + clazz.getSimpleName() + "' with id '" + id + "'");
		return entity;
	}

	/**
	 * List all the entities on the database, based on filters
	 */
	@Override
	public List<T> listAll(Integer firstResult, Integer maxResults) throws RepositoryException, EntityNotFoundException {
		Class<T> clazz = getEntityType();
		List<T> entities = null;
		//Prevent NPE
		firstResult = firstResult == null ? 0 : firstResult;
		maxResults = maxResults == null ? 0 : maxResults;
		try {
			begin();
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<T> criteria = builder.createQuery(clazz);
			criteria.select(criteria.from(clazz));
		    TypedQuery<T> query = getEntityManager().createQuery(criteria);
			if (firstResult > 0)
				query.setFirstResult(firstResult);
			if (maxResults > 0)
				query.setMaxResults(maxResults);
			entities = query.getResultList();
		} catch (Exception e) {
			String msg = "An unexpected error occured while trying to list the entity '" + clazz.getSimpleName() + "': "+e.getMessage();
			log.error(msg, e);
			throw new RepositoryException(msg, e);
		} finally {
			close();
		}
		if (entities == null || entities.size() == 0)
			throw new EntityNotFoundException("No records found");
		return entities;
	}

	@Override
	public List<T> listAll(Integer maxResults) throws RepositoryException, EntityNotFoundException {
		return listAll(0, maxResults);
	}

	@Override
	public List<T> listAll() throws RepositoryException, EntityNotFoundException {
		return listAll(0, 0);
	}
	
	/**
	 * Executes a custom query
	 * @param query
	 * @return List of the entities found
	 * @throws RepositoryException 
	 * @throws EntityNotFoundException 
	 */
	public List<T> customQuery(CustomQuery query) throws RepositoryException, EntityNotFoundException{
		Class<T> clazz = getEntityType();
		List<T> entities = null;
		try{
			begin();
			entities = query.perform(getEntityManager(), clazz);
		} catch(Exception e){
			String msg = "An unexpected error occured while trying to execute the custom query '"+query.getClass().getSimpleName()+"' for entity '" + clazz.getSimpleName() + "': "+e.getMessage();
			log.error(msg, e);
			throw new RepositoryException(msg, e);
		} finally {
			close();
		}
		if (entities == null || entities.size() == 0)
			throw new EntityNotFoundException("The custom query '"+query.getClass().getSimpleName()+"' for entity '" + clazz.getSimpleName() + "' didnt brought any results");
		return entities;
	}
	
	/**
	 * Performs a hql query execution
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> hqlQuery(String hql, Integer firstResult, Integer maxResults, boolean handleConnection) throws RepositoryException, EntityNotFoundException {
		if(!handleConnection && (!isEntityManagerOpen() || !isTransactionActive()))
			throw new IllegalStateException("The connection/transaction that is being handled by '"+getClass()+"' has been closed !");
		List<T> entities = null;
		try {
			if(handleConnection)
				begin();
			hql = preResolve(hql);
			Query query = getEntityManager().createQuery(hql,getEntityType());
			if (firstResult > 0)
				query.setFirstResult(firstResult);
			if (maxResults > 0)
				query.setMaxResults(maxResults);
			entities = query.getResultList();
		} catch (Exception e) {
			String msg = "An unexpected error occured while executing the HQL: '" + hql + "' with an row limit of '" + maxResults + "' and first result '" + firstResult + "': "+e.getMessage();
			log.error(msg, e);
			throw new RepositoryException(msg, e);
		} finally {
			if(handleConnection)
				close();
		}
		if (entities == null || entities.size() == 0)
			throw new EntityNotFoundException("The query execution didn't brought any results");
		return entities;
	}
	
	@Override
	public List<T> hqlQuery(String hql, Integer firstResult, Integer maxResults) throws RepositoryException, EntityNotFoundException {
		return hqlQuery(hql, firstResult, maxResults, true);
	}

	@Override
	public List<T> hqlQuery(String hql, Integer maxResults) throws RepositoryException, EntityNotFoundException {
		return hqlQuery(hql, 0, maxResults);
	}

	@Override
	public List<T> hqlQuery(String hql) throws RepositoryException, EntityNotFoundException {
		return hqlQuery(hql, 0, 1);
	}
	
	@Override
	public T hqlQuerySingleResult(String hql) throws RepositoryException, EntityNotFoundException {
		return hqlQuery(hql).get(0);
	}
	
	/**
	 * Saves the entity if its a new one
	 * Updates if already exist
	 * @throws EntityExistsException 
	 */
	@Override
	public void saveOrUpdate(T entity) throws RepositoryException, EntityExistsException {
		try {
			begin();
			
			mergeOrPersist(entity);
			
			commit();
		} 
		catch(ConstraintViolationException e){
			rollback();
			String msg = "The entity '" + getEntityName() + "' already exist on the database with constraint restrictions";
			log.error(msg, e);
			throw new EntityExistsException(msg, e);
		}
		catch (Exception e) {
			Class<T> clazz = getEntityType();
			rollback();
			String msg = "An unexpected error has occurred while trying to save/update the entity '"+clazz.getSimpleName()+"': "+e.getMessage();
			log.error(msg, e);
			throw new RepositoryException(msg, e);
		} 
		finally {
			close();
		}
	}
	
	/**
	 * Merge or persist the entity
	 * @param entity
	 */
	private void mergeOrPersist(T entity){
		//TODO: Implementar exceptions mais específicas
		try{
			Map<String, Object> postValues = preResolve(entity);
			T generated = getEntityManager().merge(entity);
			postResolve(entity,generated,postValues);
		}
		catch(RuntimeException e){
			//-->
			//TODO: Alterar para uso da interface do JPA
			//Solução temporária
			Throwable cause = e.getCause();
			if(cause != null && cause instanceof ConstraintViolationException){
				throw (ConstraintViolationException) cause;
			}
			//<--
			getEntityManager().persist(entity);
		}
	}
	
	/**
	 * Delete the entity
	 */
	@Override
	public void delete(T entity) throws RepositoryException {
		try{
			begin();
			if(!getEntityManager().contains(entity))
				entity = getEntityManager().merge(entity);
			getEntityManager().remove(entity);
			commit();
		}
		catch(Exception e){
			Class<T> clazz = getEntityType();
			rollback();
			String msg = "An unexpected error has occurred while trying to delete the entity '"+clazz.getSimpleName()+"': "+e.getMessage();
			log.error(msg, e);
			throw new RepositoryException(msg, e);
		}
		finally{
			close();
		}
	}
	
	/**
	 * Execute a HQL deletion
	 * @param hql
	 * @return
	 * @throws RepositoryException
	 */
	public int hqlDelete(String hql) throws RepositoryException{
		int rows = 0;
		try{
			begin();
			Query query = getEntityManager().createQuery(hql);
			rows = query.executeUpdate();
			commit();
		}
		catch(Exception e){
			rollback();
			String msg = "An unexpected error has occurred while trying to delete(hql): "+e.getMessage();
			log.error(msg, e);
			throw new RepositoryException(msg, e);
		}
		finally{
			close();
		}
		return rows;
	}
	
	/**
	 * Initialize connection
	 */
	protected void begin() {
		begin(false);
	}
	
	/**
	 * Initialize connection with transaction
	 */
	protected void begin(boolean transactional) {
		if (!isEntityManagerOpen() && (transactional && !isTransactionActive())) {
			try {
				EntityManager em = emf.createEntityManager();
				setEntityManager(em);
				if(transactional)
					em.getTransaction().begin();
			} catch (PersistenceException e) {
				log.warn("Couldn't open and begin the transaction: "+e.getMessage(), e);
			}
		}
	}
	
	/**
	 * Commit the current transaction
	 */
	protected void commit() {
		if (isEntityManagerOpen() && isTransactionActive()) {
			try {
				getEntityManager().getTransaction().commit();
				getEntityManager().clear();
			} catch (PersistenceException e) {
				log.warn("Couldn't commit the current transaction: "+e.getMessage(), e);
			}
		}
	}

	/**
	 * Rollback the current transaction
	 */
	protected void rollback() {
		if (isEntityManagerOpen() && isTransactionActive()) {
			try {
				getEntityManager().getTransaction().rollback();
				getEntityManager().clear();
			} catch (PersistenceException e) {
				log.warn("Couldn't rollback the current transaction: "+e.getMessage(), e);
			}
		}
	}

	/**
	 * Close the connection
	 */
	protected void close() {
		if (isEntityManagerOpen()) {
			try {
				if (isTransactionActive())
					rollback();
				getEntityManager().clear();
				getEntityManager().close();
			} catch (PersistenceException e) {
				log.warn("Couldn't close the entity-manager: "+e.getMessage(), e);
			}
		}
	}
	
	/**
	 * Resolve HQL custom functions
	 * @param hql
	 * @return
	 */
	//TODO: Implementar suporte a atributos
	private String preResolve(String hql){
		String finalHql = hql;
		if(StringUtil.hasText(hql)){
			for(QueryFunction function : QueryFunction.values()){
				int functionIndex = hql.indexOf(function.getFunctionIdentifier());
				if(functionIndex > -1){
					try{
						finalHql = function.getStrategy().handle(hql, functionIndex, getEntityType());
					}
					catch(Exception e){
						log.warn("Couldn't pre resolve the function '"+function.toString()+"' on '"+hql+"': "+e.getMessage());
					}
				}
			}
		}
		return finalHql;
	}
	
	/**
	 * Resolve entity custom annotations
	 * @param entity
	 * @return Auxiliar map containing values to be used after the transaction
	 */
	//TODO: Implementar suporte a atributos
	private Map<String, Object> preResolve(T entity){
		Map<String, Object> postValues = new HashMap<String, Object>();
		try{
			for(Method method : entity.getClass().getMethods()){
				//Retrieve the field type
				Class<?> returnType = method.getReturnType();
				
				//Retrieve the annotations
				Random random = method.getAnnotation(Random.class);
				Encrypt encrypt = method.getAnnotation(Encrypt.class);
				
				if(random != null){
					
					if(returnType.equals(String.class)){
						//Retrieve the setter method
						Method setter = retrieveSetterMethod(entity, method, returnType);
						
						//Get the actual value
						String value = (String) method.invoke(entity);
						
						//If there is no value, a new randomized one will be generated
						if(!StringUtil.hasText(value)){
							
							//Randomize a new value
							value = RandomicGenerator.generatePswd(random.minLength(), random.maxLength(), random.upperCaseChars(), random.numericChars(), random.specialChars());
							
							//Verifiy if should mantain the value on object
							if(random.mantainOnInsert()){
								postValues.put("random", value);
							}
						}
						
						//Sets the encrypted value
						setter.invoke(entity,value);
					}
				}
				
				if(encrypt != null){
					
					if(returnType.equals(String.class)){
						
						//Retrieve the setter method
						Method setter = retrieveSetterMethod(entity, method, returnType);
						
						//Get the value and encrypt it
						String value = (String) method.invoke(entity);
						String encrypted = value;
						
						//Check if its already encrypted
						if(!value.matches("^[a-f0-9]{32}$"))
							encrypted = new Encrypter(encrypt.value()).encrypt(value);
						
						//Sets the encrypted value
						setter.invoke(entity,encrypted);
					}
				}
				
			}
		}
		catch(Exception e){
			log.warn("Couldn't pre resolve the entity '"+getEntityType().getSimpleName()+"': "+e.getMessage());
		}
		return postValues;
	}
	
	/**
	 * Resolves the entity after the transaction
	 * @param entity
	 * @param generated
	 * @param postValues
	 */
	//TODO: Implementar suporte a atributos
	private void postResolve(T entity,T generated, Map<String, Object> postValues){
		try{
			for(Method method : entity.getClass().getMethods()){
				
				//Get the return type
				Class<?> returnType = method.getReturnType();
				
				//Resolving generated VERSION
				if(method.getAnnotation(Version.class) != null){
					//Retrieve the setter method
					Method setter = retrieveSetterMethod(entity, method, returnType);
					
					//Sets the version
					setter.invoke(entity,method.invoke(generated));
				}
				
				//Resolving generated ID
				if(method.getAnnotation(Id.class) != null){
					//Retrieve generated field value
					Object value = method.invoke(generated);
					
					//Check if its not null to set it properly
					if(value != null){
						//Retrieve the setter method
						Method setter = retrieveSetterMethod(entity, method, returnType);
						
						//Invoke the setter
						setter.invoke(entity, method.invoke(generated));
					}
					//If null, then inverse set it
					else{
						//Retrieve the setter method
						Method setter = retrieveSetterMethod(generated, method, returnType);
						
						//Invoke the setter
						setter.invoke(generated, method.invoke(entity));
					}
						
				}
				
				//Resolving mantained random value
				if(method.getAnnotation(Random.class) != null){
					Random random = method.getAnnotation(Random.class);
					if(random.mantainOnInsert()){
						String randomValue = (String) postValues.get("random");
						if(StringUtil.hasText(randomValue)){
							//Retrieve the setter method
							Method setter = retrieveSetterMethod(entity, method, returnType);
							
							//Invoke the setter
							setter.invoke(entity, randomValue);
						}
					}
				}
			}
		}
		catch(Exception e){
			log.warn("Couldn't post resolve the entity '"+getEntityType().getSimpleName()+"': "+e.getMessage());
		}
	}

	/**
	 * Retrieve entity setter method
	 * @param entity
	 * @param method
	 * @param returnType
	 * @return
	 * @throws NoSuchMethodException
	 */
	private Method retrieveSetterMethod(T entity, Method method, Class<?> returnType) throws NoSuchMethodException {
		Method setter = entity.getClass().getMethod(method.getName().replace("get","set"), returnType);
		//Grant acess
		if(!method.isAccessible())
			method.setAccessible(true);
		if(!setter.isAccessible())
			setter.setAccessible(true);
		return setter;
	}
	
	/**
	 * Identify if its JTA connection
	 * @return True or False
	 */
	protected Boolean isJta(){
		return isJta;
	}

	/**
	 * Check if the transaction is active
	 * @return True or False
	 */
	protected Boolean isTransactionActive() {
		return getEntityManager() != null && getEntityManager().getTransaction() != null && getEntityManager().getTransaction().isActive();
	}
	
	/**
	 * Check if entity manager is open
	 * @return True or False
	 */
	protected Boolean isEntityManagerOpen(){
		return getEntityManager() != null && getEntityManager().isOpen();
	}
	
}
