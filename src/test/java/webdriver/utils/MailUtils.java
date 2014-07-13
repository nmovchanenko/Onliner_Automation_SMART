package webdriver.utils;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import webdriver.BaseEntity;
import webdriver.Logger;
import webdriver.PropertiesResourceManager;

/** 
 * Work with mail.
 */
public class MailUtils extends BaseEntity{

	private String host, username, password;
	private Properties properties = new Properties();
	private MAIL_PROTOCOLS protocol;
	private final long timeToWaitMail = 120000;
	private Store store;

	/** construct mail connector
	 * @param host host
	 * @param username username
	 * @param password password
	 * @param protocol protocol of mail server
	 * @throws IOException 
	 */
	public MailUtils(String host, String username, String password) {
		this.host = host;
	    this.username = username;
	    this.password = password;
	    readConfig(host);
	    store = connect();
	}

	/**
	 * Return connected store
	 * @return Store
	 */
	public Store getStoreConnected() {
		if (store.isConnected()) {
			return store;
		}
		store = connect();
		return store;
	}
	
	
	/**
	 * account in format ivashko@mail.ru
	 * @param account
	 * @param password
	 * @throws IOException
	 */
	public MailUtils(String account,String password){
		this(account.split("@")[1], account, password);
	}

	/** Construct mail connector.
	 * @param host host
	 * @param username username
	 * @param password password
	 * @param protocol protocol of mail server
	 * @param properties properties
	 * @throws IOException 
	 */
	public MailUtils(String host, String username, String password, String fileName){
		this.host = host;
	    this.username = username;
	    this.password = password;
	    readConfig(host,fileName);
	    store = connect();
	}

	/** available protocols
	 */
	public enum MAIL_PROTOCOLS{
		POP3("pop3"), SMTP("smtp"), IMAP("imap"), IMAPS("imaps");

		private String protocol;

		/** constructor
		 * @param name mail protocol name
		 */
		MAIL_PROTOCOLS(String name){
			protocol = name;
		}

		@Override
		public String toString() {
			return protocol;
		}
	}

	/**
	 * @param folderName name of folder in mailbox
	 * @param permissions permissions for access to folder(user Folder.READ_ONLY and e.i.)
	 * @param folder folder
	 * @return messages
	 */
	public Message[] getMessages(Folder folder,int permission){
		// Get folder
		Message[] messages = null;
    	try {
    		folder.open(permission);
			// Get directory
			messages = folder.getMessages();
		} catch (MessagingException e) {
			formatLogMsg("Impossible to get messages: " + e.getMessage());
			e.printStackTrace();
		}
	    return messages;
	}

	/**
	 * @param folderName name of folder in mailbox
	 * @param permissions permissions for access to folder(user Folder.READ_ONLY and e.i.)
	 * @param folder folder
	 * @return messages
	 */
	public Message[] getMessages(Folder folder){
		return getMessages(folder,Folder.READ_WRITE);
	}
	
	
	/**
	 * @param folderName name of folder in mailbox
	 * @param permissions permissions for access to folder(user Folder.READ_ONLY and e.i.)
	 * @param folder folder
	 * @return messages
	 */
	public Message[] getMessages(String folderName){
		try {
			return getMessages(getStoreConnected().getFolder(folderName),Folder.READ_WRITE);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** Get link from the letter
	 * @param subject subject
	 * @return link
	 */
	public String getMessageContent(String folderName, String subject){
		try {
			Multipart part = (Multipart) waitForLetter(folderName,subject).getContent();
			return (String) part.getBodyPart(0).getContent();
			//link = CommonFunctions.regexGetMatchGroup((String) part2.getBodyPart(0).getContent(), "(http://smail.railsware.*)\"", 1);
		} catch (IOException e) {
			formatLogMsg("It is impossible to get conetent of message: " + e.getMessage());
		} catch (MessagingException e) {
			formatLogMsg("It is impossible to get content of message: " + e.getMessage());
		} catch (NullPointerException e) {
			return "There were no mails for account activation or reset password in 2 minutes!";
		}
		return null;
	}

	/** Get link from the letter
	 * @param subject subject
	 * @param text text
	 * @return link
	 */
	public String getMessageContent(String folderName, String subject, String text){
		try {
			Multipart part = (Multipart) waitForLetter(folderName,subject, text).getContent();
			return (String) part.getBodyPart(0).getContent();
			//link = CommonFunctions.regexGetMatchGroup((String) part2.getBodyPart(0).getContent(), "(http://smail.railsware.*)\"", 1);
		} catch (IOException e) {
			formatLogMsg("It is impossible to get conetent of message: " + e.getMessage());
		} catch (MessagingException e) {
			formatLogMsg("It is impossible to get content of message: " + e.getMessage());
		}
		return null;
	}

	/** wait for letter with necessary subject is present in mailbox
	 * @param subject subject of letter
	 * @return message
	 * @throws MessagingException MessagingException
	 */
	public Message waitForLetter(String folderName, String subject) throws MessagingException{
		Message[] messages = null;
		// waiting
		long start = System.currentTimeMillis();
		do{
			Folder folder = getStoreConnected().getFolder(folderName);
			messages = getMessages(folder);
			for (Message m : messages) {
				try {
					if(m.getSubject().contains(subject)){
						return m;
					}
				} catch (MessagingException e) {
					formatLogMsg("It is impossible to get subject of message: " + e.getMessage());
				}
			}
			try {
				folder.close(false);
			} catch (Exception e) {
				formatLogMsg(e.getMessage());
				e.printStackTrace();
			}
		}while((start + timeToWaitMail) >= System.currentTimeMillis());
		formatLogMsg(String.format("Mailbox not contains letter with subject '%1$s'. There was waiting: %2$s mills", subject, timeToWaitMail));
		return null;
	}

	/** wait for letter with necessary subject and address is present in mailbox
	 * @param subject subject of letter
	 * @param text text that message contains
	 * @return message
	 * @throws MessagingException MessagingException
	 */
	public Message waitForLetter(String folderName, String subject, String text) throws MessagingException{
		Message[] messages = null;
		// waiting
		long start = System.currentTimeMillis();
		do{
			Folder folder = getStoreConnected().getFolder(folderName);
			messages = getMessages(folder);
			for (Message m : messages) {
				try {
					String content = (String) ((Multipart) m.getContent()).getBodyPart(0).getContent();
					if(m.getSubject().contains(subject) && content.contains(text)){
						return m;
					}
				} catch (Exception e) {
					formatLogMsg("It is impossible to get subject of message: " + e.getMessage());
				}
			}
			try {
				folder.close(false);
			} catch (Exception e) {
				formatLogMsg(e.getMessage());
				e.printStackTrace();
			}
		}while((start + timeToWaitMail) >= System.currentTimeMillis());
		formatLogMsg(String.format("Mailbox not contains letter with subject '%1$s'. There was waiting: %2$s mills", subject, timeToWaitMail));
		return null;
	}

	/** by default folder "INBOX" and permissions Folder.READ_ONLY
	 * @param folder folder
	 * @return messages
	 */
	public Message[] getMessages(){
		return getMessages("INBOX");
	}

	/** connect to mailbox
	 * @return Store
	 */
	private Store connect(){
		for(int i = 0; i <= 10; i++){
			// Get session
			properties.setProperty("mail.store.protocol", protocol.toString());
			Session session = Session.getDefaultInstance(properties, null);
		    // Get the store
		    try {
		    	store = session.getStore(protocol.toString());
		    	store.connect(host, username, password);
		    	break;
		    } catch (NoSuchProviderException e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
		    	e.printStackTrace();
			} catch (MessagingException e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		return store;
	}
	
	/** Remove all the message from box
	 * @param folderName name of folder(for example "INBOX")
	 */
	public String deleteAllMessages(String folderName){
		//check if connection is established
		try{
			Folder inbox = getStoreConnected().getFolder(folderName);
			inbox.open(Folder.READ_WRITE);
			//geting all the messages from a folder
			Message[] messages = inbox.getMessages();
			for(Message message:messages) {
				message.setFlag(Flags.Flag.DELETED, true);
			}
			inbox.close(true);
			return "All messages were deleted successfull from " + username;
		}catch(MessagingException e){
			return "Messaging exception: " + e.getMessage();
		}
	}

	/**
	 * Is Client connected
	 * @return
	 */
	public Boolean isConnected(){
		return store.isConnected();
	}
	
	/** close store
	 */
	public void closeStore(){
		try {
			store.close();
		} catch (Exception e) {
			formatLogMsg(e.getMessage());
		}
	}
	
	/**
	 * Read config
	 * @param host
	 */
	private void readConfig(String host,String fileName){
		PropertiesResourceManager pm = new PropertiesResourceManager(fileName);
		String prop = pm.getProperty(host);
		this.host = prop.split(";")[0];
		this.protocol = MAIL_PROTOCOLS.valueOf(prop.split(";")[1].toUpperCase());
	}

	/**
	 * Read config
	 * @param host
	 */
	public void readConfig(String host){
		readConfig(host,"mail.properties");
	}
	
	@Override
	protected String formatLogMsg(String message) {
		return String.format("%1$s '%2$s' %3$s %4$s", "Mail Utils", this.host, Logger.LOG_DELIMITER, message);
	}

	/**
	 * Delete message that match the sunject and body
	 * @param folderName
	 * @param subject
	 * @param body
	 */
	public void deleteMessageThatContainsInfo(String folderName, String subject, String body) {
		try {
			waitForLetter(folderName,subject,body).setFlag(Flags.Flag.DELETED, true);
		} catch (MessagingException e) {
			formatLogMsg(e.getMessage());
		}
	}
	
	/**
	 * Delete message that match the sunject and body
	 * @param folderName
	 * @param subject
	 */
	public void deleteMessageThatContainsInfo(String folderName, String subject) {
		try {
			waitForLetter(folderName,subject).setFlag(Flags.Flag.DELETED, true);
		} catch (MessagingException e) {
			formatLogMsg(e.getMessage());
		}
	}
	
}
