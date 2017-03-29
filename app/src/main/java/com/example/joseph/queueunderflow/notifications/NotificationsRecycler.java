package com.example.joseph.queueunderflow.notifications;

/**
 * Created by josep on 3/29/2017.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.comments.CommentsList;
import com.example.joseph.queueunderflow.reputation.ReputationFactory;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.basicpost.BasicPost;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.cardpage.CardPage;
import com.example.joseph.queueunderflow.headquarters.skills.Skill;
import com.example.joseph.queueunderflow.home.BasePage;
import com.example.joseph.queueunderflow.reputation.ReputationFactory;
import com.example.joseph.queueunderflow.search.SearchPage;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by josep on 3/26/2017.
 */

public class NotificationsRecycler extends RecyclerView.Adapter<com.example.joseph.queueunderflow.notifications.NotificationsRecycler.PhotoHolder> {

    private NotificationsList notificationsList;


    private Context context;

    private ProgressBar loadingBar;
    private ImageView doneLoading;
    private Dialog loadingDialog;
    private ReputationFactory reputationGiver;


    public NotificationsRecycler(Context context, NotificationsList notificationsList) {

        this.context = context;
        this.notificationsList = notificationsList;


    }


    @Override
    public com.example.joseph.queueunderflow.notifications.NotificationsRecycler.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_notifications, parent, false);
        return new com.example.joseph.queueunderflow.notifications.NotificationsRecycler.PhotoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final com.example.joseph.queueunderflow.notifications.NotificationsRecycler.PhotoHolder holder, final int position) {



      Notification n = notificationsList.getNotificationList().get(position);

        holder.bodyNoti.setText(n.getData());


    }


    @Override
    public int getItemCount() {
        return notificationsList.getNotificationList().size();
    }

    public static class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        //2

        TextView bodyNoti;




        //3
        private static final String PHOTO_KEY = "PHOTO";

        //4
        public PhotoHolder(View v) {
            super(v);



            bodyNoti = (TextView) v.findViewById(R.id.bodyNoti);



            v.setOnClickListener(this);


        }

        //click method
        @Override
        public void onClick(View v) {

        }





    }




    public void createAlert(String message) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.customdialog);

        TextView alertMessage = (TextView) dialog.findViewById(R.id.alertMessage);
        Button okayBtn = (Button) dialog.findViewById(R.id.okayBtn);

        alertMessage.setText(message);
        okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }




    public String nametoDrawable(String skillName) {
        String skillImgName = "drawable/";
        skillName = skillName.toLowerCase();
        skillImgName += skillName;
        skillImgName = skillImgName.replaceAll("\\++", "plus");

        Log.d(com.example.joseph.queueunderflow.QuestRecycler.class.getSimpleName(), "el ossa eno : " + skillImgName);

        return skillImgName;
    }

    public void createLoading() {
        loadingDialog = new Dialog(context);
        loadingDialog.setCancelable(false);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingDialog.setContentView(R.layout.postingloading);
        loadingDialog.setCancelable(false);

        loadingBar = (ProgressBar) loadingDialog.findViewById(R.id.loadingBar);


        loadingBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#32BEA6"), PorterDuff.Mode.SRC_IN);

        doneLoading = (ImageView) loadingDialog.findViewById(R.id.doneLoading);


        loadingDialog.show();
    }


}