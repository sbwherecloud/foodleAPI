package com.belanger.simon.apis;

/**
  * Add your first API methods in this class, or you may create another class. In that case, please
  * update your web.xml accordingly.
 **/

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import javax.inject.Named;
import javax.jdo.PersistenceManager;


/** An endpoint class we are exposing */
@Api(name = "myApi",
     version = "v1",
     scopes = {Constants.EMAIL_SCOPE},
     clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID},
     audiences = {Constants.ANDROID_AUDIENCE},
     namespace = @ApiNamespace(ownerDomain = "apis.simon.belanger.com",
                                ownerName = "apis.simon.belanger.com",
                                packagePath=""))
public class YourFirstAPI {
	
	private Sender gcmSender = new Sender(Constants.API_GCM_SERVER_KEY);

    @ApiMethod(name = "insertVote")
    public Vote insertVote(Vote vote) throws IOException {

        vote.initializeVotes();
        
        List<String> regIds = new ArrayList<String>();
        UserProfile user = null;
        for(String email : vote.getVotersEmail()){
        	user = getUserProfile(email);
        	regIds.add(user.getGcmRegistrationId());
        }
        
        vote.setVoteCreatorEmail(vote.getVoteCreatorInformation().getEmail());
        
        PersistenceManager mgr = getPersistenceManager();

        try {
            mgr.makePersistent(vote);
            
            Message message = new Message.Builder()
            	.delayWhileIdle(true)
	            .addData("voteId", Long.toString(vote.getKey().getId()))
	            .build();
            
            gcmSender.send(message, regIds, 3);
            
        } finally {
            mgr.close();
        }

        return vote;
    }

    @ApiMethod(name = "getVote")
    public Vote getVote(@Named("voteId") Long voteId) {
    	
        PersistenceManager mgr = getPersistenceManager();
        Vote vote = null;
        try {
            vote = mgr.getObjectById(Vote.class, voteId);
        } finally {
            mgr.close();
        }
        
        UserProfile contactInformation = getUserProfile(vote.getVoteCreatorEmail());
        vote.setVoteCreatorInformation(new Contact(contactInformation));
        
        return vote;
    }
    
    @ApiMethod(name = "updateVote")
    public Vote updateVote(@Named("voteId") Long voteId, IntegerListContainer indexes) {

        PersistenceManager mgr = getPersistenceManager();
        Vote vote = null;
        
        try {
            vote = mgr.getObjectById(Vote.class, voteId);
            for(Integer index : indexes.getIntegersList()){
            	vote.registerVote(index);
            }
            mgr.makePersistent(vote);
        } finally {
            mgr.close();
        }
        return vote;
    }
    
    @ApiMethod(name = "insertUserProfile")
    public UserProfile insertUserProfile(Contact userInformation) {

        PersistenceManager mgr = getPersistenceManager();
        
        Key key = KeyFactory.createKey(UserProfile.class.getSimpleName(), userInformation.getEmail());
        
        UserProfile userProfile = new UserProfile(userInformation);
        userProfile.setKey(key);

        try {
            mgr.makePersistent(userProfile);
        } finally {
            mgr.close();
        }

        return userProfile;
    }

    @ApiMethod(name = "updateGcmRegistrationId")
    public UserProfile updateGcmRegistrarionId(@Named("userEmail") String userEmail, 
    		@Named("gcmRegistrationId") String gcmRegistrationId) {

        PersistenceManager mgr = getPersistenceManager();
        UserProfile userProfile = null;
        
        Key k = KeyFactory.createKey(UserProfile.class.getSimpleName(), userEmail);
        
        try {
            userProfile = mgr.getObjectById(UserProfile.class, k);
            userProfile.setGcmRegistrationId(gcmRegistrationId);
            mgr.makePersistent(userProfile);
        } finally {
            mgr.close();
        }
        return userProfile;
    }
    
    @ApiMethod(name = "getUserProfile")
    public UserProfile getUserProfile(@Named("userEmail") String userEmail) {

        PersistenceManager mgr = getPersistenceManager();
        UserProfile userProfile = null;
        
        Key k = KeyFactory.createKey(UserProfile.class.getSimpleName(), userEmail);
        
        try {
            userProfile = mgr.getObjectById(UserProfile.class, k);
        } finally {
            mgr.close();
        }
        return userProfile;
    }

    @ApiMethod(name = "setContactRequest")
    public Contact setContactRequest(@Named("contactEmail") String contactEmail, 
    		Contact userProfile) throws IOException {

    	UserProfile contactProfile = null;
    	contactProfile = getUserProfile(contactEmail);
    	
        PersistenceManager mgr = getPersistenceManager();

        try {
        	Message message = new Message.Builder()
        	.delayWhileIdle(true)
            .addData("friendRequest", userProfile.getFirstName() + " " +
            		userProfile.getLastName())
            .addData("askerEmail", userProfile.getEmail())
            .build();
            
        	gcmSender.send(message, contactProfile.getGcmRegistrationId(), 3);
        } finally {
            mgr.close();
        }

        return userProfile;
    }

    @ApiMethod(name = "updateContactList")
    public Contacts updateContactList(@Named("askerEmail") String askerEmail, 
    		@Named("userEmail") String userEmail, @Named("contactRequestAccepted") boolean requestAccepted) throws IOException {

        
        UserProfile askerProfile = null;
        askerProfile = getUserProfile(askerEmail);
        
        UserProfile userProfile = null;
        userProfile = getUserProfile(userEmail);
        
        PersistenceManager mgr = getPersistenceManager();
        try {
            
            if(requestAccepted){
            	askerProfile.addContactEmail(userProfile.getEmail());
            	mgr.makePersistent(askerProfile);

            	userProfile.addContactEmail(askerProfile.getEmail());
            	mgr.makePersistent(userProfile);
            }
            else{
            	Message message = new Message.Builder()
            		.delayWhileIdle(true)
            		.addData("friendRequestAnswer", "false")
            		.build();
            	
            	gcmSender.send(message, askerProfile.getGcmRegistrationId(), 3);
            }
        } finally {
            mgr.close();
        }
        
        userProfile.getContactList().clear();
        for(String contactEmail : userProfile.getContactsEmail()){
        	userProfile.addContact(new Contact(getUserProfile(contactEmail)));
        }
        
        return new Contacts(userProfile);
    }
    
    @ApiMethod(name = "getContactList")
    public Contacts getContactList(@Named("userEmail") String userEmail) {
        UserProfile userProfile = getUserProfile(userEmail);
        
        userProfile.setContactList(new ArrayList<Contact>());;
        for(String contactEmail : userProfile.getContactsEmail()){
        	userProfile.addContact(new Contact(getUserProfile(contactEmail)));
        }
        
        return new Contacts(userProfile);
    }
    
    private static PersistenceManager getPersistenceManager() {
        return PMF.get().getPersistenceManager();
    }

}
