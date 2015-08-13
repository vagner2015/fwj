package br.com.smartsy.fwj.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Enum responsible for providing the strategies 
 * for multitenancy database connection
 * @author Vagner
 *
 */
public enum MultitenancyStrategy {
	/**
	 * Filter results with tenant_id
	 */
	FILTER(new MultiTenancyStrategy() {

		@Override
		public Session getSession(SessionFactory factory, String tenantId) {
			Session session = factory.openSession();
			session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
			return session;
		}
	}),
	/**
	 * Discriminator on each table
	 */
	DISCRIMINATOR(new MultiTenancyStrategy() {

		@Override
		public Session getSession(SessionFactory factory, String tenantId) {
			return factory.withOptions().tenantIdentifier(tenantId).openSession();
		}
	});

	private MultiTenancyStrategy strategy;
	
	public MultiTenancyStrategy getStrategy() {
		return strategy;
	}

	private MultitenancyStrategy(MultiTenancyStrategy mtStrategy) {
		this.strategy = mtStrategy;
	}
	
	public interface MultiTenancyStrategy {
		Session getSession(SessionFactory factory, String tenantId);
	}
}
