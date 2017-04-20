package com.example.joseph.queueunderflow.comments;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.joseph.queueunderflow.QuestRecycler;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.cardpage.CardPage;
import com.example.joseph.queueunderflow.cardpage.PostRecycler;
import com.example.joseph.queueunderflow.home.BasePage;
import com.example.joseph.queueunderflow.home.FeedPage;
import com.example.joseph.queueunderflow.submitpost.SubmitQuestion;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsPage extends AppCompatActivity {

    @BindView(R.id.postCBtn)
    Button postCBtn;
    @BindView(R.id.commentslv)
    RecyclerView commentslv;
    @BindView(R.id.commentField)
    EditText commentField;
    
    private LinearLayoutManager mLinearLayoutManager;
    private CommentsRecycler mAdapter;
    private CommentsList commentsList;
    private PostRecycler context;
    private String postId;
    private int fromPost;// 0 pressed from question and 1 from answer
    private int pos;
    private BasicQuestion theQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_page);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            commentsList = (CommentsList) extras.get("commentList");
            postId = extras.getString("thePostId");
            fromPost = extras.getInt("fromPost");
            pos = extras.getInt("pos");
            theQuestion = (BasicQuestion) extras.get("theQuestion");


            mLinearLayoutManager = new LinearLayoutManager(this);
            commentslv.setLayoutManager(mLinearLayoutManager);

            mAdapter = new CommentsRecycler(this,commentsList);
            commentslv.setAdapter(mAdapter);

        }








        postCBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(commentField.getText().equals("")){
                    createAlert("Field is empty!");
                }else{
                    String bodyC = commentField.getText().toString();
                    String userC = ParseUser.getCurrentUser().getUsername();
                    final HashMap<String,String> newC = new HashMap<String, String>();
                    newC.put(userC,bodyC);

                    final Comment newCom = new Comment(userC,bodyC);
                    ParseQuery findQ = new ParseQuery("");
                    if(fromPost == 0){
                        findQ = new ParseQuery("Questions");
                    }else{
                        findQ = new ParseQuery("Answers");
                    }

                    findQ.whereEqualTo("objectId",postId);
                    findQ.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(java.util.List<ParseObject> objects, ParseException e) {

                            if (e == null) {
                                for (ParseObject userData : objects) {

                                    userData.add("comments",newC);
                                    userData.saveInBackground();

                                    commentsList.addComment(newCom);
                                    mAdapter.notifyDataSetChanged();

                                    if(fromPost == 0){
                                        theQuestion.setCommentsList(commentsList);
                                    }




                                }
                            }
                        }
                    });

                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CommentsPage.this, BasePage.class);
        startActivity(intent);
    }

    public void createAlert(String message){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.customdialog);
        dialog.setCancelable(false);

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
}
