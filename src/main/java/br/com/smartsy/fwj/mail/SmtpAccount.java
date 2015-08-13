package br.com.smartsy.fwj.mail;

/**
 * Used to store smtp accounts
 * @author Vagner
 *
 */
public class SmtpAccount {

	private String host;
	private int port;
	private String username;
	private String password;

	public SmtpAccount() {
		// TODO Auto-generated constructor stub
	}

	public SmtpAccount(String host, int port, String username, String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}