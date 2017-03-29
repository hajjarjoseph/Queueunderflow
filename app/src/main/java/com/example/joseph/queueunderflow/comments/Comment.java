package com.example.joseph.queueunderflow.comments;

import java.io.Serializable;

/**
 * Created by josep on 3/27/2017.
 */

public class Comment implements Serializable{
    public String getmUser() {
        return mUser;
    }

    public void setmUser(String mUser) {
        this.mUser = mUser;
    }

    public String getmBody() {
        return mBody;
    }

    public void setmBody(String mBody) {
        this.mBody = mBody;
    }

    private String mUser;
    private String mBody;

    public Comment(){
        mUser = "";
        mBody = "";
    }

   public Comment(String user,String body){
        mUser = user;
        mBody = body;
    }
}
