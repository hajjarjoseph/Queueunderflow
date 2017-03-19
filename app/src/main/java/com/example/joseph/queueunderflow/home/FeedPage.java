package com.example.joseph.queueunderflow.home;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joseph.queueunderflow.QuestRecycler;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.basicpost.BasicPost;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.basicpost.basicquestion.imagequestion.ImageQuestion;
import com.example.joseph.queueunderflow.skills.SuggestSkills;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;

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

    public void tagTap(){
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
                            boolean edited = userData.getBoolean("edited");
                            ArrayList<String> tags = (ArrayList<String>) userData.get("tags");
                            ArrayList<String> answersId = (ArrayList<String>) userData.get("answers");


                            ArrayList<String> images = new ArrayList<String>();

                            ParseFile qImage = (ParseFile) userData.get("image1");
                            if(qImage == null){

                                // Create BasicQuestion with no images
                                BasicQuestion basicQuestion = new BasicQuestion(owner,title,description,postId,postDate,tags,answersId);
                                basicQuestion.setHasAnswer(hasAnswer);
                                basicQuestion.setEdited(edited);
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
                                imageQuestion.setEdited(edited);
                                items.add(imageQuestion);
                            }







                        }

                        mAdapter = new QuestRecycler((BasePage) getActivity(),items);

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
