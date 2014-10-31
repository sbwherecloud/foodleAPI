package com.belanger.simon.apis;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Contacts {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent
	private List<String> contactsEmail;
	@Persistent
    private UserProfile userProfile;
	
	private List<Contact> contactList = new ArrayList<Contact>();

	public Contacts(UserProfile userProfile){
		this.contactList = userProfile.getContactList();
	}
	
	public Key getKey(){
		return key;
	}
	
	public void setKey(Key key){
		this.key = key;
	}
	
	public List<String> getContacts(){
		return contactsEmail;
	}
	
	public void setContacts(List<String> contacts){
		this.contactsEmail = contacts;
	}
	
	public void addContact(String contact){
		if(contactsEmail.contains(contact)){
			contactsEmail.remove(contact);
		}
		contactsEmail.add(contact);
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
