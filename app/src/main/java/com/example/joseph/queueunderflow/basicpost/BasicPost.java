package com.example.joseph.queueunderflow.basicpost;

import android.net.Uri;

import com.example.joseph.queueunderflow.comments.CommentsList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by josep on 2/28/2017.
 */
abstract public class BasicPost implements Serializable {
    private String qOwner;
    private String qDescription;
    private String postId;


    public String getProUrl() {
        return proUrl;
    }

    public void setProUrl(String proUrl) {
        this.proUrl = proUrl;
    }

    private String proUrl;


    public CommentsList getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(CommentsList commentsList) {
        this.commentsList = commentsList;
    }

    private CommentsList commentsList;


    private int votes;
    private int upVotes;

    public int getUpVotes() {
        return upVotes;
    }


    private int downVotes;
    private ArrayList<String> voters;

    public ArrayList<String> getUpVoters() {
        return upVoters;
    }

    public void setUpVoters(ArrayList<String> upVoters) {
        this.upVoters = new ArrayList<>();
        for(int i=0;i<upVoters.size();i++){
            this.upVoters.add(upVoters.get(i));
        }
    }

    public ArrayList<String> getDownVoters() {
        return downVoters;
    }

    public void setDownVoters(ArrayList<String> downVoters) {
        this.downVoters = new ArrayList<>();
        for(int i=0;i<downVoters.size();i++){
            this.downVoters.add(downVoters.get(i));
        }
    }

    private ArrayList<String> upVoters;

    private ArrayList<String> downVoters;

    private boolean edited;


    private Date postDate;




    //Default Constructor
    public BasicPost(){
        qOwner = "";
        qDescription = "";
        postId = "";
    }

    public BasicPost(String qOwner,String qDescription,String postId,Date postDate){
        this.qOwner = qOwner;
        this.qDescription = qDescription;
        this.postId = postId;
        this.postDate = postDate;
        this.edited = false;
        this.votes = 0;
        this.proUrl = "";
    }

    public BasicPost(String qOwner,String qDescription,String postId,Date postDate,ArrayList<String>voters){
        this.qOwner = qOwner;
        this.qDescription = qDescription;
        this.postId = postId;
        this.postDate = postDate;
        this.edited = false;
        this.votes = 0;
        this.proUrl = "";
        this.voters = new ArrayList<>();
        for(int i=0;i<voters.size();i++){
            this.voters.add(voters.get(i));
        }
    }


    /********************************************************Getters And Setters ********************************************************/
    public String getqOwner() {
        return qOwner;
    }

    public void setqOwner(String qOwner) {
        this.qOwner = qOwner;
    }



    public String getqDescription() {
        return qDescription;
    }

    public void setqDescription(String qDescription) {
        this.qDescription = qDescription;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }


    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }

    public ArrayList<String> getVoters() {
        return voters;
    }

    public void setVoters(ArrayList<String> voters) {
        this.voters = new ArrayList<>();
        for(int i=0;i<voters.size();i++){
            this.voters.add(voters.get(i));
        }
    }


    /********************************************************Getters And Setters ********************************************************/



}
