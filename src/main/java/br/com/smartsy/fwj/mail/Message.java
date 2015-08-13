package br.com.smartsy.fwj.mail;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Bean made to store a mail message
 * @author Vagner
 *
 */
public class Message {

	private String subject;
	private String body;
	private Set<Recipient> destinataries = new HashSet<>();
	private Set<Recipient> copies = new HashSet<>();
	
	public String getSubject() {
		return subject;
	}
	
	public String getBody() {
		return body;
	}
	
	public Set<Recipient> getDestinataries() {
		return destinataries;
	}
	
	public Set<String> getDestinatariesAdresses() {
		Set<String> adresses = new HashSet<>();
		for(Recipient recipient : getDestinataries())
			adresses.add(recipient.getEmail());
		return adresses;
	}
	
	public Set<Recipient> getCopies() {
		return copies;
	}
	
	public Set<String> getCopiesAdresses() {
		Set<String> copies = new HashSet<>();
		for(Recipient recipient : getCopies())
			copies.add(recipient.getEmail());
		return copies;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public void addDestinatary(Recipient destinatary){
		this.destinataries.add(destinatary);
	}
	
	public void addDestinataries(List<Recipient> destinataries){
		this.destinataries.addAll(destinataries);
	}
	
	public void addCopy(Recipient copy){
		this.copies.add(copy);
	}
	
	public void addCopies(List<Recipient> copies){
		this.copies.addAll(copies);
	}
}