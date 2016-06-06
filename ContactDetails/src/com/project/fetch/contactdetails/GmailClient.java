/*
 * This class inherit javax.mail.Authenticator class to implement 
 * smtp.gmail.com client
 * 
 * @author Aditi Kulkarni
 */
package com.project.fetch.contactdetails;

import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.util.Log;

public class GmailClient extends javax.mail.Authenticator{

	private String user,password,from,port,sport,host,subject,body;
	private String[] to;
	private boolean auth,debug;
	private Multipart multipart;
		
	public GmailClient(String userName, String pwd)
	{
		host = "smtp.gmail.com";
		port = "465"; // this is default smtp port
		sport = "465"; // default socket factory port
		
		user = ""; // this will save username for gmail client authentication
		password = ""; // this will save password for gmail client authentication
		from = ""; // Email address of sender
		subject = ""; // Subject line for email
		body = ""; // Email body text will be saved in this variable
		
		debug = false; // it will if debug mode is enabled or not
		auth = true; //It will indicate whether smtp authentication is enabled or not
		
		multipart = new MimeMultipart();
		
		//This will add extra needed properties for multipart code
		
		MailcapCommandMap mailCapCmdMp = (MailcapCommandMap)CommandMap.getDefaultCommandMap();
		mailCapCmdMp.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		mailCapCmdMp.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
		mailCapCmdMp.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
		mailCapCmdMp.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
		mailCapCmdMp.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822"); 
	    CommandMap.setDefaultCommandMap(mailCapCmdMp); 
	    
	    this.user = userName;
		this.password = pwd;
		
	}
		
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String[] getTo() {
		return to;
	}

	public void setTo(String[] to) {
		this.to = to;
	}

	public boolean isAuth() {
		return auth;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public Multipart getMultipart() {
		return multipart;
	}

	public void setMultipart(Multipart multipart) {
		this.multipart = multipart;
	}

	/*
	 * //This constructor will e use to set all default values	
	 * (non-Javadoc)
	 * @see javax.mail.Authenticator#getPasswordAuthentication()
	 */

	  @Override 
	  public PasswordAuthentication getPasswordAuthentication() { 
	    return new PasswordAuthentication(user, password); 
	  }
	  
	/*
	 * Following method will send email
	 * @param no parameters
	 * @return boolean
	 */
	public boolean sendEmail() 
	{
		
		Properties props = setProperties();
		
		if(!user.equals("") && !password.equals("") && to.length > 0)
		{
			try{
			Session session = Session.getInstance(props,new GmailClient("kartiki.aditi@gmail.com","Ad!t!@1986"));
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			
			InternetAddress[] addressTo = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++)
			{ 
			      addressTo[i] = new InternetAddress(to[i]); 
			} 
			message.setRecipients(MimeMessage.RecipientType.TO, addressTo);
			
			message.setSubject(subject);
			message.setSentDate(new Date());
			
			//This will set message body text
			BodyPart messageBody = new MimeBodyPart();
			messageBody.setText(body);
			multipart.addBodyPart(messageBody);
			
			//this will put part in message body
			message.setContent(multipart);
			
			//Send Email
			Transport.send(message);
		}
		catch(Exception e)
		{
			Log.d("GmailClient:sendEmail()","Exception occured");
			e.printStackTrace();
			return false;
		}
			
			return true;
		}
		else
		{
			Log.d("SendEmail","In else part");
			return false;
		}
		
	}
	
	/*
	 * Following method set properties for email client
	 * @return java.util.Propeties
	 */
	private Properties setProperties()
	{
		Properties props = new Properties();
		props.put("mail.smtp.host",host);
		
		if(debug)
		{
			props.put("mail.debug","true");
		}
		
		if(auth)
		{
			props.put("mail.smtp.auth", "true");
		}
		
		props.put("mail.smtp.port",port);
		props.put("mail.smtp.socketFactory.port", sport);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		
		return props;
	}
	
}
