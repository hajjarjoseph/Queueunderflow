package com.example.joseph.queueunderflow.comments;

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

public class CommentsRecycler extends RecyclerView.Adapter<com.example.joseph.queueunderflow.comments.CommentsRecycler.PhotoHolder> {

    private CommentsList commentsList;


    private Context context;

    private ProgressBar loadingBar;
    private ImageView doneLoading;
    private Dialog loadingDialog;
    private ReputationFactory reputationGiver;


    public CommentsRecycler(Context context, CommentsList commentsList) {

        this.context = context;
        this.commentsList = commentsList;


    }


    @Override
    public com.example.joseph.queueunderflow.comments.CommentsRecycler.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commentslayout, parent, false);
        return new com.example.joseph.queueunderflow.comments.CommentsRecycler.PhotoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final com.example.joseph.queueunderflow.comments.CommentsRecycler.PhotoHolder holder, final int position) {



        holder.userCmt.setText("#" + commentsList.getCommentsL().get(position).getmUser());
        holder.bodyCmt.setText(commentsList.getCommentsL().get(position).getmBody());


    }


    @Override
    public int getItemCount() {
        return commentsList.getCommentsL().size();
    }

    public static class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        //2
        TextView userCmt;
        TextView bodyCmt;




        //3
        private static final String PHOTO_KEY = "PHOTO";

        //4
        public PhotoHolder(View v) {
            super(v);


            userCmt = (TextView) v.findViewById(R.id.userCmt);
            bodyCmt = (TextView) v.findViewById(R.id.bodyCmt);



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