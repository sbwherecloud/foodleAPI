package com.belanger.simon.apis;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


/** The object model for the data we are sending through endpoints */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Vote {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    @Persistent
    private String voteCreatorEmail;
    @Persistent
    private List<String> recipes;
    @Persistent
    private List<Integer> recipeVotes;
    @Persistent
    private List<String> votersEmail;
    @Persistent
    private long endTimeInMillis;
    
    private Contact voteCreatorInformation;

    public void setVoteCreatorEmail(String voteCreatorEmail){
    	this.voteCreatorEmail = voteCreatorEmail;
    }
    
    public String getVoteCreatorEmail(){
    	return voteCreatorEmail;
    }
    
    public void setVoteCreatorInformation(Contact voteCreatorInformation){
    	this.voteCreatorInformation = voteCreatorInformation;
    }
    
    public Contact getVoteCreatorInformation(){
    	return voteCreatorInformation;
    }    
    
    public void setEndTimeInMillis(long endTime){
    	this.endTimeInMillis = endTime;
    }
    
    public long getEndTimeInMillis(){
    	return endTimeInMillis;
    }
    
    public Key getKey() {
        return key;
    }

    public List<String> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<String> recipes) {

        this.recipes = new ArrayList<>();
        this.recipes.addAll(recipes);
    }
    
    public List<String> getVotersEmail() {
        return votersEmail;
    }

    public void setVotersEmail(List<String> votersEmail) {

        this.votersEmail = new ArrayList<>();
        this.votersEmail.addAll(votersEmail);
    }

    public List<Integer> getRecipeVotes() {
        return recipeVotes;
    }
    
    public void setRecipeVotes(List<Integer> votes){
    	this.recipeVotes.clear();
    	this.recipeVotes.addAll(votes);
    }

    public void initializeVotes() {
        recipeVotes = new ArrayList<>();
        for(int i = 0; i < recipes.size(); i++){
            recipeVotes.add(new Integer(0));
        }
    }
    
    public void registerVote(int index){
    	int oldVoteTotal = recipeVotes.get(index);
    	recipeVotes.remove(index);
    	recipeVotes.add(index, new Integer(oldVoteTotal + 1));
    }
}