package com.example.joseph.queueunderflow.reputation;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by josep on 3/26/2017.
 */

public class ReputationFactory {
    private String mSelectedUser;
    private int reputationPoints;

    public ReputationFactory(){
        mSelectedUser = "";
        reputationPoints = 0;
    }

    public ReputationFactory(String selectedUser,int rep){
        mSelectedUser = selectedUser;
        reputationPoints = rep;
    }


    public void giveReputation(){

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("myUsername",mSelectedUser);
        params.put("reputation",reputationPoints);


        ParseCloud.callFunctionInBackground("incrementRep", params, new FunctionCallback<Object>() {
            public void done(Object object, ParseException e) {
                if (e == null) {

                } else {

                }
            }
        });
    }

    public void giveReputation(String selectedUser,int repPoints){

        this.mSelectedUser = selectedUser;
        this.reputationPoints = repPoints;

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("myUsername",mSelectedUser);
        params.put("reputation",reputationPoints);


        ParseCloud.callFunctionInBackground("incrementRep", params, new FunctionCallback<Object>() {
            public void done(Object object, ParseException e) {
                if (e == null) {

                } else {

                }
            }
        });
    }



    public String getmSelectedUser() {
        return mSelectedUser;
    }

    public void setmSelectedUser(String mSelectedUser) {
        this.mSelectedUser = mSelectedUser;
    }

    public int getReputationPoints() {
        return reputationPoints;
    }

    public void setReputationPoints(int reputationPoints) {
        this.reputationPoints = reputationPoints;
    }





}
