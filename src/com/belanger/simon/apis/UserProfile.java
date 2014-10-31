package com.belanger.simon.apis;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class UserProfile {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    @Persistent
	private String email;
    @Persistent
	private String firstName;
    @Persistent
	private String lastName;
    @Persistent
    private String gcmRegistrationId;
    @Persistent
    private List<String> contactsEmail = new ArrayList<String>();
    
    private List<Contact> contactList = new ArrayList<Contact>();
    
    public UserProfile(Contact contactInformation){
    	this.email = contactInformation.getEmail();
    	this.firstName = contactInformation.getFirstName();
    	this.lastName = contactInformation.getLastName();
    }
    
    public void setKey(Key key){
    	this.key = key;
    }
    
    public Key getKey(){
    	return key;
    }
    
    public String getEmail(){
		return this.email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
    public String getFirstName(){
		return this.firstName;
	}
	
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	
    public String getLastName(){
		return this.lastName;
	}
	
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
    
    public void setGcmRegistrationId(String gcmRegistrationId){
    	this.gcmRegistrationId = gcmRegistrationId;
    }
    
    public String getGcmRegistrationId(){
    	return gcmRegistrationId;
    }

    public List<String> getContactsEmail(){
		return contactsEmail;
	}
	
	public void setContactsEmail(List<String> contacts){
		this.contactsEmail = contacts;
	}
	
	public void addContactEmail(String contactemail){
		if(contactsEmail.contains(contactemail)){
			contactsEmail.remove(contactemail);
		}
		contactsEmail.add(contactemail);
	}
	
	public List<Contact> getContactList(){
		return contactList;
	}
	
	public void setContactList(List<Contact> contactList){
		this.contactList = contactList;
	}
	
	public void addContact(Contact contact){
		if(contactList.contains(contact)){
			contactList.remove(contact);
		}
		contactList.add(contact);
	}
    
}
