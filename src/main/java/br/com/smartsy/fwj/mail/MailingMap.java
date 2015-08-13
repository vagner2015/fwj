package br.com.smartsy.fwj.mail;

import java.util.HashMap;

/**
 * Wrapper class to hold Mailings on HashMap
 * <p>Helps organizing mailing per functionality(key)
 * @author Vagner
 *
 */
public class MailingMap extends HashMap<String,Mailing> {
	
	/**
	 * UID
	 */
	private static final long serialVersionUID = 497860925663663199L;

	public void put(String key,SmtpAccount smtp,Message...messages){
		Mailing mailing = get(key);
		if(mailing == null)
			mailing = new Mailing();
		mailing.setSender(smtp);
		mailing.addMessages(messages);
		this.put(key, mailing);
	}
	
	@Override
	public Mailing put(String key, Mailing value) {
		Mailing old = super.put(key, value);
		if(value != null && value.getSender() == null)
			if(old != null && value.getSender() != null)
				value.setSender(old.getSender());
		return old;
	}
	
}