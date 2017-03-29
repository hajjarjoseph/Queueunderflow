package com.example.joseph.queueunderflow.notifications;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.joseph.queueunderflow.QuestRecycler;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.basicpost.basicquestion.imagequestion.ImageQuestion;
import com.example.joseph.queueunderflow.comments.Comment;
import com.example.joseph.queueunderflow.comments.CommentsList;
import com.example.joseph.queueunderflow.comments.CommentsRecycler;
import com.example.joseph.queueunderflow.home.BasePage;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationPage extends Fragment {

    private LinearLayoutManager mLinearLayoutManager;
    private NotificationsRecycler mAdapter;

    @BindView(R.id.notilv)
    RecyclerView notilv;

    Context context;

    public NotificationPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_notification_page, container, false);
        ButterKnife.bind(this, view);

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        notilv.setLayoutManager(mLinearLayoutManager);

        context = getContext();

        new LoadNoti().execute();


        return view;


    }


    private class LoadNoti extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {



            ParseQuery fetchUser = new ParseQuery("_User");
            fetchUser.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

            fetchUser.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(java.util.List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        for (ParseObject userData : objects) {



                            ArrayList<HashMap<String,String>> notiListArr = (ArrayList<HashMap<String,String>>) userData.get("notifications");
                            ArrayList<Notification> theList = new ArrayList<Notification>();
                            HashMap<String, String> notiList = new HashMap<String, String>();
                            if(notiListArr.size()>0) {

                                for(int i=0;i<notiListArr.size();i++){
                                    notiList = notiListArr.get(i);
                                    Map.Entry<String,String> entry=notiList.entrySet().iterator().next();
                                    Notification n = new Notification(entry.getKey(),entry.getValue());
                                    theList.add(n);

                                }
                            }

                            NotificationsList finalNoti = new NotificationsList(theList);
                            mAdapter = new NotificationsRecycler(context,finalNoti);
                            notilv.setAdapter(mAdapter);
                        }

                    }
                }

            });


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml


        }
    }





}
