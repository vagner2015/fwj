package br.com.smartsy.fwj.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

/**
 * Class responsible data encryption
 * @author Vagner
 * 
 */
public final class Encrypter 
{
	private MessageDigest digest;
	private Algorithm type;
	private static final Algorithm defaultType = Algorithm.MD5;
	
	private static Logger log = Logger.getLogger(Encrypter.class.getName());
	
	/**
	 * Initialize the encryption system
	 * @param type - {@link Algorithm} to be used(e.g: MD5, SHA, ...)
	 */
	public Encrypter(Algorithm type) throws NoSuchAlgorithmException
	{
		log.debug("Initializing the encrypter");
		setType(type);
		try
		{
			digest = MessageDigest.getInstance(type.algorithm());
		}
		catch(NoSuchAlgorithmException e)
		{
			log.warn("The "+type.algorithm()+" algorithm is not available on current system...setting a default type - "+defaultType.algorithm(), e);
			digest = MessageDigest.getInstance(defaultType.algorithm());
		}
	}
	
	/**
	 * Defines the algorithm type
	 * @param type - {@link Algorithm} to be used(e.g: MD5, SHA, ...)
	 */
	private void setType(Algorithm type) 
	{
		if(type == null){
			log.warn("The algorithm cannot be null...setting a default type - "+defaultType.algorithm());
			type = defaultType;
		}
		this.type = type;
		log.debug("Established '"+type.algorithm().toUpperCase()+"' for the encryption");
	}

	/**
	 * Encrypts a value
	 * @param value - The value to be encrypted
	 * @return Encrypted value
	 */
	public String encrypt(String value){
		log.debug("Initializing the encryption, using '"+type.algorithm()+"' as algorithm");
		if(digest == null){
			log.warn("The encryption digest method has not been initialized. Using the default pattern.");
			setType(defaultType);
		}
		digest.update(value.getBytes());
		byte info[] = digest.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < info.length; i++) 
        	sb.append(Integer.toString((info[i] & 0xff) + 0x100, 16).substring(1));
        String encrypted = sb.toString();
        log.debug("The value has been encrypted");
        return encrypted;
	}
	
}