package com.example.joseph.queueunderflow.home;




import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.notifications.Notification;
import com.example.joseph.queueunderflow.notifications.NotificationPage;
import com.example.joseph.queueunderflow.notifications.NotificationsList;
import com.example.joseph.queueunderflow.notifications.NotificationsRecycler;
import com.example.joseph.queueunderflow.profile.ProfilePage;
import com.example.joseph.queueunderflow.search.SearchFragment;
import com.example.joseph.queueunderflow.search.SearchPage;
import com.example.joseph.queueunderflow.utils.FragmentChangeListener;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BasePage extends AppCompatActivity implements
        FragmentChangeListener {

    @BindView(R.id.containerFrag)
    RelativeLayout contaierFrag;



    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    private int badgesNum;

    private boolean inSearch = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_page);

        ButterKnife.bind(this);

        ParseQuery fetchUser = ParseInstallation.getQuery();

        Map<String,Object> params = new HashMap<String,Object>();

        params.put("theUser",ParseUser.getCurrentUser().getUsername());



        ParseCloud.callFunctionInBackground("getBadge", params, new FunctionCallback<Object>() {
            public void done(Object object, ParseException e) {
                if (e == null) {
                    badgesNum = (int) object;


                    if(badgesNum > 0){
                        BottomBarTab news = bottomBar.getTabWithId(R.id.tab_notifications);
                        news.setBadgeCount(badgesNum);
                    }

                } else {

                }
            }
        });





        bottomBar.selectTabAtPosition(0);

        final  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FeedPage newProfile = new FeedPage();
        ft.replace(R.id.containerFrag, newProfile);
        ft.commit();

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_search) {
                    final  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    SearchFragment newSearch = new SearchFragment();
                    ft.replace(R.id.containerFrag, newSearch);
                    ft.commit();
                    inSearch = true;
                }else if(tabId == R.id.tab_profile){
                    final  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ProfilePage newProfile = new ProfilePage();
                    ft.replace(R.id.containerFrag, newProfile);
                    ft.commit();
                    inSearch = false;
                } else if(tabId == R.id.tab_feed){
                    final  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    FeedPage newFeed = new FeedPage();
                    ft.replace(R.id.containerFrag, newFeed );
                    ft.commit();
                    inSearch = false;
                }else if(tabId == R.id.tab_notifications){
                    final  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    NotificationPage newsFeed = new NotificationPage();
                    ft.replace(R.id.containerFrag, newsFeed );
                    ft.commit();

                    inSearch = false;

                    if(badgesNum > 0){
                        Map<String,Object> params = new HashMap<String,Object>();

                        params.put("theUser",ParseUser.getCurrentUser().getUsername());



                        ParseCloud.callFunctionInBackground("resetBadge", params, new FunctionCallback<Object>() {
                            public void done(Object object, ParseException e) {
                                if (e == null) {

                                    badgesNum = 0;
                                    BottomBarTab news = bottomBar.getTabWithId(R.id.tab_notifications);
                                    news.setBadgeCount(badgesNum);

                                } else {

                                }
                            }
                        });
                    }



                }
            }
        });

        if (savedInstanceState == null) {
            Fragment newFragment = new FeedPage();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(newFragment,newFragment.toString());
            fragmentTransaction.addToBackStack(newFragment.toString());
            fragmentTransaction.commit();


        }


    }

    @Override
    public void onBackPressed() {

        if(inSearch){
            
        }

    }




    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerFrag, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }

    private class LoadBadges extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {









            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml


        }
    }



}
