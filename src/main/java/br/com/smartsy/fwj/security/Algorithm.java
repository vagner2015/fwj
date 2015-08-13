package br.com.smartsy.fwj.security;

import java.security.NoSuchAlgorithmException;

/**
 * ENUM containing algorithms for encrypting information
 * WARNING: The algorithm you choose, must be installed on the machine so it can work.
 * @author Vagner
 *
 */
public enum Algorithm {	
	MD2("MD2"),
	MD5("MD5"),
	SHA1("SHA-1"),
	SHA256("SHA-256"),
	SHA384("SHA-384"),
	SHA512("SHA-512");
	
	private String algorithm;
	
	public String algorithm() {
		return this.algorithm;
	}
	
	public Encrypter encrypter() throws NoSuchAlgorithmException{
		return new Encrypter(this);
	}
	
	private Algorithm(String algorithm) {
		this.algorithm = algorithm;
	}
}