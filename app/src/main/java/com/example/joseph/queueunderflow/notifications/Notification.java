package com.example.joseph.queueunderflow.notifications;

/**
 * Created by josep on 3/29/2017.
 */

public class Notification {
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private String postId;
    private String data;

    public Notification(){
        postId = "";
        data = "";
    }

    public  Notification(String postId,String data){
        this.postId = postId;
        this.data = data;
    }
}
