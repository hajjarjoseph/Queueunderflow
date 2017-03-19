package com.example.joseph.queueunderflow.cardpage;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joseph.queueunderflow.QuestItem;
import com.example.joseph.queueunderflow.QuestRecycler;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.authentication.IntroPage;
import com.example.joseph.queueunderflow.basicpost.BasicPost;
import com.example.joseph.queueunderflow.basicpost.basicanswer.BasicAnswer;
import com.example.joseph.queueunderflow.basicpost.basicanswer.imageanswer.ImageAnswer;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.basicpost.basicquestion.imagequestion.ImageQuestion;
import com.example.joseph.queueunderflow.headquarters.QuestionsList;
import com.example.joseph.queueunderflow.headquarters.skills.SkillLoader;
import com.example.joseph.queueunderflow.submitpost.SubmitQuestion;
import com.example.joseph.queueunderflow.viewpager.ViewPagerAdapter;
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

public class CardPage extends AppCompatActivity  {

    @BindView(R.id.postlv)
    RecyclerView poslv;

    @BindView(R.id.postBtn)
    TextView postBtn;

    private LinearLayoutManager mLinearLayoutManager;
    private PostRecycler mAdapter;


    private ArrayList<BasicPost> items;

    private BasicQuestion theQuestion;

    private String  questId;
    private String  owner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_page);
        ButterKnife.bind(this);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            theQuestion = (BasicQuestion) extras.getSerializable("postDetail");
            theQuestion.setAnswersList(new ArrayList<BasicAnswer>());
        }



        mLinearLayoutManager = new LinearLayoutManager(this);
        poslv.setLayoutManager(mLinearLayoutManager);



        mAdapter = new PostRecycler(CardPage.this,theQuestion,mAdapter);

        poslv.setAdapter(mAdapter);

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardPage.this, SubmitQuestion.class);


                    intent.putExtra("theQuestion", theQuestion);
                    intent.putExtra("fromActivity",3);


                    startActivity(intent);
            }
        });

        new LoadPost().execute();


    }



    private class LoadPost extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            ParseQuery fetchUser = new ParseQuery("Answers");
            fetchUser.whereContainedIn("objectId",theQuestion.getAnswersId());
            fetchUser.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(java.util.List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        for (ParseObject userData : objects) {


                            String owner = "#";
                            owner += userData.getString("owner");
                            String description = userData.getString("Description");
                            Date postDate = userData.getCreatedAt();
                            String postId = userData.getObjectId();


                            ArrayList<String> images = new ArrayList<String>();

                            ParseFile qImage = (ParseFile) userData.get("image1");
                            if(qImage == null){

                                // Create BasicQuestion with no images
                                BasicAnswer basicAnswer = new BasicAnswer(owner,description,postId,postDate,false);
                                ArrayList<BasicAnswer> answers = theQuestion.getAnswersList();
                                answers.add(basicAnswer);
                                theQuestion.setAnswersList(answers);


                            }else {
                                String imageUrl = qImage.getUrl();//live url


                                images.add(imageUrl);


                                qImage = (ParseFile) userData.get("image2");

                                if (qImage == null) {
                                    //Do nothing
                                } else {
                                    imageUrl = qImage.getUrl();//live url


                                    images.add(imageUrl);


                                    qImage = (ParseFile) userData.get("image3");

                                    if (qImage == null) {

                                    } else {
                                        imageUrl = qImage.getUrl();//live url

                                        images.add(imageUrl);
                                    }


                                }

                                ImageAnswer imageAnswer = new ImageAnswer(owner,description,postId,postDate,images,false);
                                ArrayList<BasicAnswer> answers = theQuestion.getAnswersList();
                                answers.add(imageAnswer);
                                theQuestion.setAnswersList(answers);
                            }




                        }

                        //Rearrange Order
                        ArrayList<String> theIds = theQuestion.getAnswersId();
                        ArrayList<BasicAnswer> answersList = theQuestion.getAnswersList();
                        int answerSize = theIds.size();
                        for(int i=0;i<answerSize;i++){
                            if(!answersList.get(i).getPostId().equals(theIds.get(i))){
                                String theId = answersList.get(i).getPostId();
                                for(int z=0;z<answerSize;z++){
                                    if(theId.equals(theIds.get(z))){
                                        //swap
                                        BasicAnswer prevAns = answersList.get(i);
                                        BasicAnswer currAns = answersList.get(z);
                                        answersList.set(i,currAns);
                                        answersList.set(z,prevAns);

                                        break;

                                    }
                                }
                            }
                        }

                        theQuestion.setAnswersList(answersList);
                        mAdapter.notifyDataSetChanged();


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
