package com.example.joseph.queueunderflow.alerts;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.basicpost.BasicPost;
import com.example.joseph.queueunderflow.basicpost.basicanswer.BasicAnswer;
import com.example.joseph.queueunderflow.basicpost.basicanswer.imageanswer.ImageAnswer;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.basicpost.basicquestion.imagequestion.ImageQuestion;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

/**
 * Created by josep on 4/4/2017.
 */

public class OthersPostAlert {
    private Dialog dialog;
    private Context c;
    private ProgressBar loadingBar;
    private ImageView doneLoading;
    private Dialog loadingDialog;

    public  OthersPostAlert(Context c, final BasicPost post){
        this.c = c;
         dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.setContentView(R.layout.optiondialog);

        RelativeLayout flagLayout = (RelativeLayout) dialog.findViewById(R.id.flagPost);
        RelativeLayout subscribeLayout = (RelativeLayout) dialog.findViewById(R.id.subscribePost);

        if(post instanceof BasicAnswer || post instanceof ImageAnswer){
            subscribeLayout.setVisibility(View.GONE);
        }

        flagLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                confirmationAlert(post);
            }
        });

        subscribeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                createLoading();

                ParseQuery findUser = new ParseQuery("_User");
                findUser.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                findUser.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(java.util.List<ParseObject> objects, ParseException e) {

                        if(e == null){
                            for(ParseObject userData:objects ){

                                userData.add("subscribedPosts",post.getPostId());
                                userData.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        ParseQuery findPost = new ParseQuery("Questions");
                                        findPost.whereEqualTo("objectId",post.getPostId());
                                        findPost.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(java.util.List<ParseObject> objects, ParseException e) {

                                                if(e == null){
                                                    for(ParseObject userData:objects ){

                                                        ArrayList<String> subsUsers = (ArrayList<String>) userData.get("subcribedUsers");
                                                        subsUsers.add(ParseUser.getCurrentUser().getUsername());
                                                        userData.put("subcribedUsers",subsUsers);

                                                        userData.saveInBackground(new SaveCallback() {
                                                            @Override
                                                            public void done(ParseException e) {
                                                                if(e == null){
                                                                    loadingBar.setVisibility(View.INVISIBLE);
                                                                    doneLoading.setVisibility(View.VISIBLE);
                                                                    new Handler().postDelayed(new Runnable() {
                                                                        @Override
                                                                        public void run() {

                                                                            loadingDialog.dismiss();

                                                                        }
                                                                    }, 2000);


                                                                }else {

                                                                }
                                                            }
                                                        });



                                                    }


                                                }

                                            }

                                        });
                                    }
                                });





                            }


                        }

                    }

                });


            }
        });




    }

    public void show(){
        dialog.show();
    }

    public void thankYouMessage(){
        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.thankyoumessage);

        dialog.show();
    }

    public void confirmationAlert(final BasicPost post){
        final Dialog dialog = new Dialog(c);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.setContentView(R.layout.answerdialog);

        final TextView answerMsg = (TextView) dialog.findViewById(R.id.answerMsg);
        final TextView yesBtn = (TextView) dialog.findViewById(R.id.yesBtnAns);
        final TextView noBtn = (TextView) dialog.findViewById(R.id.noBtnAns);
        final ProgressBar prog = (ProgressBar) dialog.findViewById(R.id.progressAns);
        final ImageView doneImg = (ImageView) dialog.findViewById(R.id.doneImg);

        answerMsg.setText("Are you sure you want to flag this post?");

        prog.setVisibility(View.INVISIBLE);
        doneImg.setVisibility(View.INVISIBLE);

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerMsg.setVisibility(View.INVISIBLE);
                yesBtn.setVisibility(View.INVISIBLE);
                noBtn.setVisibility(View.INVISIBLE);
                prog.setVisibility(View.VISIBLE);

                prog.getIndeterminateDrawable().setColorFilter(Color.parseColor("#32BEA6"), PorterDuff.Mode.SRC_IN);

                ParseQuery query = new ParseQuery("");

                if(post instanceof ImageQuestion || post instanceof BasicQuestion){
                    query = new ParseQuery("Questions");
                }else{
                    query = new ParseQuery("Answers");
                }


                query.whereEqualTo("objectId",post.getPostId());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(java.util.List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            for (final ParseObject userData : objects) {
                                userData.put("flagged",true);

                                userData.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e == null){
                                            prog.setVisibility(View.INVISIBLE);
                                            dialog.dismiss();
                                            thankYouMessage();

                                        }
                                    }
                                });
                            }
                        }
                    }
                });


            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void createLoading(){
        loadingDialog = new Dialog(c);
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
