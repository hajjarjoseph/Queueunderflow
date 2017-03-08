package com.example.joseph.queueunderflow.headquarters;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.example.joseph.queueunderflow.QuestItem;
import com.example.joseph.queueunderflow.QuestRecycler;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.basicpost.BasicPost;
import com.example.joseph.queueunderflow.basicpost.basicanswer.BasicAnswer;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.basicpost.basicquestion.imagequestion.ImageQuestion;
import com.example.joseph.queueunderflow.headquarters.skills.SkillLoader;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionsList extends AppCompatActivity {

    @BindView(R.id.questlv)
    RecyclerView questlv;


    private LinearLayoutManager mLinearLayoutManager;
    private QuestRecycler mAdapter;

    private ArrayList<BasicPost> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_list);
        ButterKnife.bind(this);



        mLinearLayoutManager = new LinearLayoutManager(this);
        questlv.setLayoutManager(mLinearLayoutManager);

        new LoadQuests().execute();


    }


    private class LoadQuests extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            items = new ArrayList<>();

            ParseQuery fetchQuests = new ParseQuery("Questions");
            fetchQuests.orderByDescending("createdAt");
            fetchQuests.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(java.util.List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        for (ParseObject userData : objects) {



                            String title = userData.getString("title");
                            String owner = "#";
                            owner += userData.getString("owner");
                            String description = userData.getString("description");
                            Date postDate = userData.getCreatedAt();
                            String postId = userData.getObjectId();
                            boolean hasAnswer = userData.getBoolean("hasAnswer");
                            ArrayList<String> tags = (ArrayList<String>) userData.get("tags");
                            ArrayList<String> answersId = (ArrayList<String>) userData.get("answers");


                            ArrayList<String> images = new ArrayList<String>();

                            ParseFile qImage = (ParseFile) userData.get("image1");
                            if(qImage == null){

                                // Create BasicQuestion with no images
                                BasicQuestion basicQuestion = new BasicQuestion(owner,title,description,postId,postDate,tags,answersId);
                                basicQuestion.setHasAnswer(hasAnswer);
                                items.add(basicQuestion);

                            }else{
                                String imageUrl = qImage.getUrl() ;//live url


                                images.add(imageUrl);


                                qImage = (ParseFile) userData.get("image2");

                                if(qImage == null){
                                    //Do nothing
                                }else{
                                    imageUrl = qImage.getUrl() ;//live url


                                    images.add(imageUrl);


                                    qImage = (ParseFile) userData.get("image3");

                                    if(qImage == null){

                                    }else{
                                        imageUrl = qImage.getUrl() ;//live url

                                        images.add(imageUrl);
                                    }


                                }

                                //Create ImageQuestion
                                ImageQuestion imageQuestion = new ImageQuestion(owner,title,description,postId,postDate,tags,answersId,images);
                                imageQuestion.setHasAnswer(hasAnswer);
                                items.add(imageQuestion);
                            }







                        }

                        mAdapter = new QuestRecycler(QuestionsList.this,items);

                        questlv.setAdapter(mAdapter);

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
