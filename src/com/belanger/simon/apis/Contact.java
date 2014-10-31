package com.belanger.simon.apis;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Contact {
	
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
	private Contacts contacts;
	
	public Contact(UserProfile userProfile){
		this.email = userProfile.getEmail();
		this.firstName = userProfile.getFirstName();
		this.lastName = userProfile.getLastName();
	}

    public void setKey(Key key){
    	this.key = key;
    }
    
    public Key getKey(){
    	return key;
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
	
	public String getEmail(){
		return this.email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
}
