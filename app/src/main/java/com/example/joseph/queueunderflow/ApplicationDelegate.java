package com.example.joseph.queueunderflow;

/**
 * Created by josep on 2/6/2017.
 */
import android.app.Application;



import com.parse.Parse;

import com.parse.ParseInstallation;

/**
 * Created by joe on 6/5/2016.
 */
public class ApplicationDelegate extends Application {
    public static final String YOUR_APPLICATION_ID = "cmps253";
    public static final String YOUR_CLIENT_KEY = "ZalB9kxVSH39IRCvmSTmCWFBOwTDhWo6uqqsihUOO";



    @Override
    public void onCreate() {

        super.onCreate();


        /**
         * Gets the default {@link Tracker} for this {@link Application}.
         * @return tracker
         */


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(YOUR_APPLICATION_ID)
                .clientKey(YOUR_CLIENT_KEY)

                .server("https://queueunderflow.herokuapp.com/parse/") // The trailing slash is important.


                .build()
        );

        ParseInstallation.getCurrentInstallation().saveInBackground();




    }
}
