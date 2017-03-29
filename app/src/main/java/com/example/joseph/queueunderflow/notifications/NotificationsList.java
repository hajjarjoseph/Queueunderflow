package com.example.joseph.queueunderflow.notifications;

import java.util.ArrayList;

/**
 * Created by josep on 3/29/2017.
 */

public class NotificationsList {
    public ArrayList<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(ArrayList<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    private ArrayList<Notification> notificationList;

    public NotificationsList(){
        notificationList = new ArrayList<>();
    }

    public  NotificationsList(ArrayList<Notification> notificationList){
        this.notificationList = new ArrayList<>();
        for(int i=0;i<notificationList.size();i++){
            this.notificationList.add(notificationList.get(i));
        }
    }
}
