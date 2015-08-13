package br.com.smartsy.fwj.mail;

/**
 * Bean that stores a mail recipient(from/to)
 * @author Vagner
 *
 */
public class Recipient {

	private String name;
	private String email;

	public Recipient() {
		this("","");
	}
	
	public Recipient(String email) {
		this("",email);
	}

	public Recipient(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}