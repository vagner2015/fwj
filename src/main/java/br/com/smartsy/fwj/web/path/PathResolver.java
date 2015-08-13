package br.com.smartsy.fwj.web.path;

public interface PathResolver {

	/**
	 * Resolve the create method path
	 * @return
	 */
	public abstract String resolveCreate();

	/**
	 * Resolve the update method path
	 * @param id
	 * @return
	 */
	public abstract String resolveUpdate(Long id);

	/**
	 * Resolve the delete method path
	 * @param id
	 * @return
	 */
	public abstract String resolveDelete(Long id);
	
	/**
	 * Resolve the get method path
	 * @param id
	 * @return
	 */
	public abstract String resolveGet();

	/**
	 * Resolve the get method path
	 * @param id
	 * @return
	 */
	public abstract String resolveGet(Long id);

	/**
	 * Resolve the list method path
	 * @param id
	 * @return
	 */
	public abstract String resolveList();

}