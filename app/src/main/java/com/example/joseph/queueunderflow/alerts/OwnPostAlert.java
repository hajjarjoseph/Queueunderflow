package com.example.joseph.queueunderflow.alerts;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.basicpost.BasicPost;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.basicpost.basicquestion.imagequestion.ImageQuestion;
import com.example.joseph.queueunderflow.cardpage.PostRecycler;
import com.example.joseph.queueunderflow.home.BasePage;
import com.example.joseph.queueunderflow.selectiontopic.SelectTopicPage;
import com.example.joseph.queueunderflow.submitpost.SubmitQuestion;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

/**
 * Created by josep on 4/4/2017.
 */

public class OwnPostAlert {
    private Dialog dialog;
    private  Context c;
    private BasicPost basicPost;

    public OwnPostAlert(final Context c, final BasicPost post, final String questionId){
        this.c = c;
        this.basicPost = post;
        dialog = new Dialog(this.c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.setContentView(R.layout.editpostdialog);


        RelativeLayout editLayout = (RelativeLayout) dialog.findViewById(R.id.editPost);


        RelativeLayout deleteLayout = (RelativeLayout) dialog.findViewById(R.id.deletePost);

        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseQuery findPost = new ParseQuery("");
                if(post instanceof BasicQuestion || post instanceof ImageQuestion) {
                     findPost = new ParseQuery("Questions");
                }else {
                    findPost = new ParseQuery("Answers");
                    ParseQuery findQuestList = new ParseQuery("Questions");

                    findQuestList.whereEqualTo("objectId", questionId);
                    findQuestList.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(java.util.List<ParseObject> objects, ParseException e) {

                            if (e == null) {
                                for (ParseObject userData : objects) {
                                    ArrayList<String> answerIds = (ArrayList<String>) userData.get("answers");
                                    for(int i=0;i<answerIds.size();i++){
                                        if(answerIds.get(i).equals(post.getPostId())){
                                            answerIds.remove(i);
                                            break;
                                        }
                                    }
                                    userData.put("answers",answerIds);
                                    userData.saveInBackground();
                                }
                            }
                        }
                    });
                }




                findPost.whereEqualTo("objectId",post.getPostId());
                findPost.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(java.util.List<ParseObject> objects, ParseException e) {

                        if (e == null) {
                            for (ParseObject userData : objects) {
                                    userData.deleteEventually(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if(e == null){
                                                Intent intent = new Intent(c, BasePage.class);
                                                c.startActivity(intent);
                                            }
                                        }
                                    });
                            }
                        }
                    }
                });
                            }
        });

        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(c, SubmitQuestion.class);


                    intent.putExtra("editPost", post);
                    intent.putExtra("fromActivity",2);


                c.startActivity(intent);

            }
        });

    }

    public void show(){
        dialog.show();
    }

}
