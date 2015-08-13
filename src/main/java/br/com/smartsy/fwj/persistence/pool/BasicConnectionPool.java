package br.com.smartsy.fwj.persistence.pool;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.smartsy.fwj.exception.ConnectionException;
import br.com.smartsy.fwj.hibernate.MultitenancyStrategy;
import br.com.smartsy.fwj.persistence.EntityManagerFactoryBuilder;
import br.com.smartsy.fwj.util.PropertiesResolver;
import br.com.smartsy.fwj.util.StringUtil;

/**
 * A connection pool for storing JPA-{@link EntityManagerFactory} instances
 * 
 * @author Vagner
 * @since 1.0
 * 
 */
public class BasicConnectionPool {

	private static final Logger log = Logger.getLogger(BasicConnectionPool.class);
	
	private static final String DEFAULT_CONNECTION = "default";
	private static final MultitenancyStrategy DEFAULT_STRATEGY = MultitenancyStrategy.FILTER;
	private final Map<String, EntityManagerFactory> holder;
	private static BasicConnectionPool instance;
	private MultitenancyStrategy multitenancy;

	private BasicConnectionPool(MultitenancyStrategy multitenancy) {
		this.holder = new HashMap<>();
		setMultitenancy(multitenancy);
	}

	/**
	 * Get a singleton instance of the pool.
	 * <p>
	 * Default multi-tenancy strategy may be used, wich is Multitenancy.FILTER
	 * 
	 * @return ConnectionPool instance
	 */
	public static BasicConnectionPool getInstance() {
		return getInstance(DEFAULT_STRATEGY);
	}

	/**
	 * Get a singleton instance of the pool.
	 * <p>
	 * Must specify a multi-tenancy strategy
	 * 
	 * @return ConnectionPool instance
	 */
	public static BasicConnectionPool getInstance(MultitenancyStrategy multitenancy) {
		if (instance == null)
			instance = new BasicConnectionPool(multitenancy);
		else
			instance.setMultitenancy(multitenancy);
		return instance;
	}
	
	public MultitenancyStrategy getMultitenancy() {
		return multitenancy;
	}

	public void setMultitenancy(MultitenancyStrategy strategy) {
		this.multitenancy = strategy;
	}

	/**
	 * Add a default unique connection to the pool
	 * 
	 * @param name
	 *            - A unique name for the connection(Warning: If you specify an
	 *            already existing name, the previous one will be replaced)
	 * @param persistenceUnit
	 *            - The persistence unit name
	 * @param parameters
	 *            - Special parameters for overwriting the "persistence.xml"
	 * @throws ConnectionException
	 *             - If something wrong occurs while trying to open the
	 *             connection
	 */
	public EntityManagerFactory addDefaultConnection(String persistenceUnit, Map<String, String> parameters) throws ConnectionException {
		return addConnection(DEFAULT_CONNECTION, persistenceUnit, parameters);
	}

	/**
	 * Add a default unique connection to the pool based on datasourceFile
	 * 
	 * @param name
	 *            - A unique name for the connection(Warning: If you specify an
	 *            already existing name, the previous one will be replaced)
	 * @param datasourceFile
	 *            - The path to the file on classpath.
	 *            <p>
	 *            Ex: datasource.properties
	 *            <p>
	 *            database/config.properties
	 * @throws ConnectionException
	 *             - If something wrong occurs while trying to open the
	 *             connection
	 */
	public EntityManagerFactory addDefaultConnection(String datasourceFile) throws ConnectionException {
		return addConnection(DEFAULT_CONNECTION, datasourceFile);
	}

	/**
	 * Add connection to the pool based on unit and parameters
	 * 
	 * @param name
	 *            - A unique name for the connection(Warning: If you specify an
	 *            already existing name, the previous one will be replaced)
	 * @param persistenceUnit
	 *            - The persistence unit name
	 * @param parameters
	 *            - Special parameters for overwriting the "persistence.xml"
	 * @throws ConnectionException
	 *             - If something wrong occurs while trying to open the
	 *             connection
	 */
	public EntityManagerFactory addConnection(String name, String persistenceUnit, Map<String, String> parameters) throws ConnectionException {
		EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder().forUnit(persistenceUnit).addParams(parameters);
		return addConnection(name, builder);
	}

	/**
	 * Add connection to the pool based on datasourceFile
	 * 
	 * @param name
	 *            - A unique name for the connection(Warning: If you specify an
	 *            already existing name, the previous one will be replaced)
	 * @param datasourceFile
	 *            - The path to the file on classpath.
	 *            <p>
	 *            Ex: datasource.properties
	 *            <p>
	 *            database/config.properties
	 * @throws ConnectionException
	 *             - If something wrong occurs while trying to open the
	 *             connection
	 */
	public EntityManagerFactory addConnection(String name, String datasourceFile) throws ConnectionException {
		PropertiesResolver resolver = new PropertiesResolver(datasourceFile);
		EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(resolver.getProperties());
		return addConnection(name, builder);
	}

	private EntityManagerFactory addConnection(String name, EntityManagerFactoryBuilder builder) throws ConnectionException {
		EntityManagerFactory factory = null;
		if (StringUtil.hasText(name)) {
			if (holder.get(name) != null)
				releaseConnection(name);
			factory = builder.build();
			testConnection(factory);
			holder.put(name, factory);
		}
		return factory;
	}
	
	/**
	 * Verify if the default connection exists
	 * @return True if the connection exists, False if not
	 */
	public boolean hasDefaultConnection(){
		return hasConnection(DEFAULT_CONNECTION);
	}
	
	/**
	 * Verify if the connection exists
	 * @param name of the pool
	 * @return True if the connection exists, False if not
	 */
	public boolean hasConnection(String name){
		return getEntityManagerFactory(name) != null;
	}

	/**
	 * Release connection on the pool
	 * 
	 * @param name
	 *            - The unique name specified for the connection
	 */
	public void releaseConnection(String name) {
		EntityManagerFactory connection = holder.get(name);
		if (connection != null && connection.isOpen()) {
			connection.close();
			holder.remove(name);
		}
	}

	/**
	 * Releases all active connections on pool
	 */
	public void clear() {
		for (String key : holder.keySet())
			releaseConnection(key);
		holder.clear();
	}

	/**
	 * @see getEntityManagerFactory(String name)
	 * @return EntityManagerFactory
	 */
	public EntityManagerFactory getDefaultEntityManagerFactory() {
		return getEntityManagerFactory(DEFAULT_CONNECTION);
	}

	/**
	 * Get the {@link EntityManagerFactory}(Connection) from pool
	 * 
	 * @param name
	 *            - The unique name specified for the connection
	 * @return EntityManagerFactory
	 */
	public EntityManagerFactory getEntityManagerFactory(String name) {
		return holder.get(name);
	}

	/**
	 * @return EntityManager
	 * @throws ConnectionException 
	 */
	public EntityManager getDefaultEntityManager() throws ConnectionException {
		return getDefaultEntityManager(null);
	}
	
	/**
	 * @return EntityManager
	 * @throws ConnectionException 
	 */
	public EntityManager getDefaultEntityManager(Map<String,String> params) throws ConnectionException {
		return getEntityManager(DEFAULT_CONNECTION,params);
	}

	/**
	 * Get an {@link EntityManager} from pool
	 * 
	 * @param name
	 *            - The unique name specified for the connection
	 * @return JPA EntityManager
	 * @throws ConnectionException 
	 */
	public EntityManager getEntityManager(String name) throws ConnectionException {
		return getEntityManager(name,null);
	}
	
	/**
	 * Get an {@link EntityManager} from pool
	 * 
	 * @param name
	 *            - The unique name specified for the connection
	 * @param params
	 * 			  - Parameters to override pre-defined settings
	 * @return JPA EntityManager
	 * @throws ConnectionException 
	 */
	public EntityManager getEntityManager(String name,Map<String,String> params) throws ConnectionException {
		EntityManager manager = null;
		EntityManagerFactory connection = getEntityManagerFactory(name);
		if (connection != null && connection.isOpen())
			manager = connection.createEntityManager(params);
		testConnection(manager);
		return manager;
	}

	/**
	 * @see getHibernateSessionFactory(String name)
	 * @return SessionFactory
	 */
	public SessionFactory getDefaultHibernateSessionFactory() {
		return getHibernateSessionFactory(DEFAULT_CONNECTION);
	}

	/**
	 * Get a Hibernate {@link SessionFactory} from pool
	 * 
	 * @param name
	 *            - The unique name specified for the connection
	 * @return Hibernate sessionFactory
	 */
	public SessionFactory getHibernateSessionFactory(String name) {
		EntityManagerFactory factory = getEntityManagerFactory(name);
		return factory.unwrap(SessionFactory.class);
	}

	/**
	 * @see getHibernateSession(String name)
	 * @return Session
	 */
	public Session getDefaultHibernateSession() {
		return getHibernateSession(DEFAULT_CONNECTION);
	}

	/**
	 * @see getHibernateSession(String name,String tenantId)
	 * @return Session
	 */
	public Session getDefaultHibernateSession(String tenantId) {
		return getHibernateSession(DEFAULT_CONNECTION, tenantId);
	}

	/**
	 * Get a Hibernate {@link Session} from pool
	 * 
	 * @param name
	 *            - The unique name specified for the connection
	 * @return Hibernate Session
	 */
	public Session getHibernateSession(String name) {
		SessionFactory factory = getHibernateSessionFactory(name);
		return factory.withOptions().openSession();
	}

	/**
	 * Get Hibernate {@link Session} from pool on multi tenancy context
	 * 
	 * @param name
	 *            - The unique name specified for the connection
	 * @param tenantIdentifier
	 *            - The tenant_id
	 * @return Hibernate Session
	 */
	public Session getHibernateSession(String name, String tenantId) {
		return getMultitenancy().getStrategy().getSession(getHibernateSessionFactory(name), tenantId);
	}

	/**
	 * Test the database connection
	 * 
	 * @param factory
	 *            - The entity manager factory
	 * @throws ConnectionException
	 *             - If could not connect
	 */
	private void testConnection(EntityManagerFactory factory) throws ConnectionException {
		testConnection(factory.createEntityManager());
	}
	
	/**
	 * Test the database connection
	 * 
	 * @param factory
	 *            - The entity manager factory
	 * @throws ConnectionException
	 *             - If could not connect
	 * @author Vagner 
	 */
	private void testConnection(EntityManager manager) throws ConnectionException {
		try {
			manager.getTransaction().begin();
			manager.getTransaction().rollback();
		}
		catch(IllegalStateException e){
			/**
			 * JTA Datasource detected
			 * OBS: If an error occurs while connection, app server will 
			 *      never deploy the artifact so testConnection() wont be called.
			 *      It means that the exception can be ignored as IllegalStateException
			 *      is only throw when using a JTA datasource
			 */
			log.debug("Connection Test - JTA datasource being used, so cant get transaction");
		}
		catch (Exception e) {
			//Connection Error
			throw new ConnectionException("Could not connect to the database: " + e.getMessage(), e);
		}
		finally{
			if(manager != null && manager.isOpen())
				manager.close();
		}
	}
}