package br.com.smartsy.fwj.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * Class designed to send email messages
 * <p>
 * <b>Warning:</b>In order to use it, Spring Context Support must be on
 * classpath
 * 
 * @author Vagner
 * 
 */
public class MailOutput {

	private static final Logger log = Logger.getLogger(MailOutput.class);

	private JavaMailSenderImpl sender;

	public MailOutput(JavaMailSenderImpl sender) {
		this.sender = sender;
	}

	public void setSender(JavaMailSenderImpl sender) {
		this.sender = sender;
	}

	public JavaMailSenderImpl getSender() {
		return sender;
	}

	/**
	 * Send several mail messages
	 * 
	 * @param messages
	 *            - The messages to be sent
	 */
	public void send(Message... messages) {
		final List<MimeMessage> mailing = new ArrayList<>();
		for (Message message : messages) {
			try{
				log.info("MAIL>Subject: " + message.getSubject());
				log.info("MAIL>Number of destinataries " + message.getDestinataries().size());
				log.info("MAIL>Number of copies " + message.getCopies().size());
				//Prepare HTML message
				MimeMessage mime = prepareMessage(message);
				// Add to the mailing list
				mailing.add(mime);
			}
			catch(MessagingException e){
				log.error("Could not send message: "+e.getMessage());
			}
		}
		log.info("MAIL>Number of mails to be sent: " + mailing.size());
		//Send and wait
		runAndWait(getSender(), mailing);
	}
	
	private MimeMessage prepareMessage(Message message) throws MessagingException {
		//Info
		String subject = message.getSubject();
		String body = message.getBody();
		String[] destinataries = message.getDestinatariesAdresses().toArray(new String[0]);
		String[] copies = message.getCopiesAdresses().toArray(new String[0]);
		
		//Prepare
		MimeMessage mime = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");
		helper.setSubject(subject);
		helper.setText(body, true);
		helper.setFrom(sender.getUsername());
		helper.setTo(destinataries);
		helper.setCc(copies);
		return mime;
	}
	
	/**
	 * Run and check time
	 * <p>Stops execution if it times out
	 * @param sender
	 * @param msgs
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void runAndWait(final JavaMailSenderImpl sender,final List<MimeMessage> msgs){
		Callable<Void> run = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				try{
					sender.send(msgs.toArray(new MimeMessage[0]));
				}
				catch(Exception e){
					System.out.println(e);
				}
				return null;
			}
		};
		RunnableFuture future = new FutureTask(run);
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(future);
		try {
			future.get(10, TimeUnit.SECONDS); // wait 10 seconds
		} 
		catch (Exception ex) {
			// timed out or another error. Try to stop the code if possible.
			future.cancel(true);
		}
		service.shutdown();
	}

}