package br.com.smartsy.fwj.security;

/**
 * Interface made to be used by entities 
 * with encryptable fields
 * @author Vagner
 *
 */
public interface Encryptable {
	
	public void enableEncryption();
	
	public void disableEncryption();

	public boolean shouldEncrypt();
	
}
