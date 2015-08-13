package br.com.smartsy.fwj.i18n;

import java.util.ResourceBundle;

/**
 * Util class for retrieving messages from a multi language resource bundle
 * archive
 * 
 * @author Vagner
 * 
 */
public final class Messager {
	/**
	 * The resource bundle of messages
	 */
	private final ResourceBundle bundle;

	/**
	 * Constructs a {@link Messager} with a base name of {@link ResourceBundle}
	 * for retrieving messages
	 * 
	 * @param baseName
	 *            - The base name of the I18n messages
	 */
	public Messager(String baseName) {
		bundle = ResourceBundle.getBundle(baseName);
	}

	/**
	 * Retrieves a message from the resource bundle
	 * 
	 * @param key
	 *            - The key to the message
	 * @return Message
	 */
	public String getMessage(String key) {
		return bundle.getString(key);
	}
}