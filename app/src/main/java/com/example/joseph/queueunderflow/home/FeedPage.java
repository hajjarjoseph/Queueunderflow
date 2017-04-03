package com.example.joseph.queueunderflow.home;


import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.joseph.queueunderflow.DAL.ParseTransaction;
import com.example.joseph.queueunderflow.QuestRecycler;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.authentication.IntroPage;
import com.example.joseph.queueunderflow.basicpost.BasicPost;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.basicpost.basicquestion.imagequestion.ImageQuestion;
import com.example.joseph.queueunderflow.comments.Comment;
import com.example.joseph.queueunderflow.comments.CommentsList;
import com.example.joseph.queueunderflow.skills.SuggestSkills;
import com.parse.FindCallback;
import com.parse.Parse;
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
public class FeedPage extends Fragment {


    @BindView(R.id.questlv)
    RecyclerView questlv;

    @BindView(R.id.askBtn)
    TextView askBtn;

    @BindView(R.id.logOutBtn)
    TextView logOutBtn;

    private LinearLayoutManager mLinearLayoutManager;
    private QuestRecycler mAdapter;

    private ArrayList<BasicPost> items = new ArrayList<>();
    Fragment context;


    public FeedPage() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_feed_page, container, false);
        ButterKnife.bind(this, view);
        context = this;
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        questlv.setLayoutManager(mLinearLayoutManager);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(context.getContext(), IntroPage.class);
                startActivity(intent);
            }
        });


        askBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context.getContext(), SuggestSkills.class);
                startActivity(intent);

            }
        });

        new LoadQuests().execute();
        return view;

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    private class LoadQuests extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            ParseTransaction questionsTransaction = new ParseTransaction();

            questionsTransaction.getQuestions(questlv,1);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml


        }
    }

    public void firstPost(){
        Dialog firstPostDialog = new Dialog(this.getContext());

        firstPostDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        firstPostDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        firstPostDialog.setContentView(R.layout.firstpostmessage);
        firstPostDialog.setCancelable(false);



        firstPostDialog.show();
    }



}
