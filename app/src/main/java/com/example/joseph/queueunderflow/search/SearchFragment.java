package com.example.joseph.queueunderflow.search;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;

import com.example.joseph.queueunderflow.QuestRecycler;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.basicpost.BasicPost;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.basicpost.basicquestion.imagequestion.ImageQuestion;
import com.example.joseph.queueunderflow.comments.Comment;
import com.example.joseph.queueunderflow.comments.CommentsList;
import com.example.joseph.queueunderflow.headquarters.skills.Skill;
import com.example.joseph.queueunderflow.home.BasePage;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    SearchFragment context;

    @BindView(R.id.tagslv)
    GridView tagsgv;
    @BindView(R.id.searchQuest)
    EditText searchQuest;
    @BindView(R.id.questlvSearch)
    RecyclerView questlv;

    @BindView(R.id.searchPg)
    ProgressBar searchPg;


    private ArrayList<BasicPost> items = new ArrayList<>();
    private QuestRecycler mAdapter;
    private CustomGridAdapter mGridAdapter;
    private String srchTxt="";
    private boolean fromTag = false;
    private String tagName = "";
    private ArrayList<Skill> skillsList;

    public SearchFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);





        context = this;

        new LoadTopics().execute();

        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager secondlayoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);


        questlv.setLayoutManager(secondlayoutManager);

        questlv.setVisibility(View.INVISIBLE);
        searchPg.setVisibility(View.INVISIBLE);




        CustomGridAdapter mAdapter = new CustomGridAdapter(this.getContext());


        tagsgv.setAdapter((ListAdapter) mAdapter);



        searchQuest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchPg.setVisibility(View.VISIBLE);
                fromTag = false;
                srchTxt = searchQuest.getText().toString();
                if(srchTxt.equals("")){
                    tagsgv.setVisibility(View.VISIBLE);
                    questlv.setVisibility(View.INVISIBLE);
                    searchPg.setVisibility(View.INVISIBLE);
                }else{
                    tagsgv.setVisibility(View.INVISIBLE);
                    questlv.setVisibility(View.INVISIBLE);
                    searchPg.setVisibility(View.VISIBLE);
                    new LoadQuests().execute();
                }





            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        return view;
    }



    public void tagPress(String name){
        searchPg.setVisibility(View.VISIBLE);
        tagName = name;
        fromTag = true;
        new LoadQuests().execute();
        tagsgv.setVisibility(View.INVISIBLE);
        questlv.setVisibility(View.INVISIBLE);
    }

    private class LoadQuests extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            items = new ArrayList<>();

            ParseQuery fetchQuests = new ParseQuery("Questions");
            fetchQuests.orderByDescending("createdAt");
            if(fromTag){
                fetchQuests.whereContains("tags",tagName);
            }else{
                srchTxt = srchTxt.substring(0, 1).toUpperCase() + srchTxt.substring(1);
                fetchQuests.whereContains("title",srchTxt);
            }

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
                            int votes = userData.getInt("Votes");
                            int upVotes = userData.getInt("upvotes");
                            int downVotes = userData.getInt("downvotes");

                            ArrayList<String> voters = (ArrayList<String>) userData.get("voters");
                            ArrayList<String> tags = (ArrayList<String>) userData.get("tags");
                            ArrayList<String> answersId = (ArrayList<String>) userData.get("answers");


                            ArrayList<String> images = new ArrayList<String>();

                            ParseFile qImage = (ParseFile) userData.get("image1");
                            if(qImage == null){

                                // Create BasicQuestion with no images
                                BasicQuestion basicQuestion = new BasicQuestion(owner,title,description,postId,postDate,voters,tags,answersId);
                                basicQuestion.setHasAnswer(hasAnswer);
                                basicQuestion.setEdited(edited);
                                basicQuestion.setVotes(upVotes-downVotes);
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
                                ImageQuestion imageQuestion = new ImageQuestion(owner,title,description,postId,postDate,tags,answersId,images,voters);
                                imageQuestion.setHasAnswer(hasAnswer);
                                imageQuestion.setEdited(edited);
                                imageQuestion.setVotes(upVotes-downVotes);
                                items.add(imageQuestion);
                            }


                        }

                        mAdapter = new QuestRecycler( getContext(),items);


                        questlv.setAdapter(mAdapter);

                        if(srchTxt.isEmpty() && fromTag ==false){
                            questlv.setVisibility(View.INVISIBLE);
                            searchPg.setVisibility(View.INVISIBLE);
                        }else if (!srchTxt.isEmpty()){
                            questlv.setVisibility(View.VISIBLE);
                            searchPg.setVisibility(View.INVISIBLE);
                        }else if(fromTag == true){
                             questlv.setVisibility(View.VISIBLE);
                            searchPg.setVisibility(View.INVISIBLE);
                        }



                        items = new ArrayList<BasicPost>();

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


    private class LoadTopics extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            skillsList = new ArrayList<>();

            ParseQuery fetchQuests = new ParseQuery("Skills");
            fetchQuests.orderByDescending("createdAt");
            fetchQuests.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(java.util.List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        for (ParseObject userData : objects) {

                            Skill skill = new Skill();

                            String name = userData.getString("name");




                            ParseFile qImage = (ParseFile) userData.get("icon");

                            String imageUrl = qImage.getUrl() ;//live url
                            Uri imageUri = Uri.parse(imageUrl);

                            skill.setName(name);
                            skill.setSkillUrl(imageUri);

                            skillsList.add(skill);




                        }

                        mGridAdapter = new CustomGridAdapter(getContext(),skillsList,SearchFragment.this  );

                        tagsgv.setAdapter(mGridAdapter);

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
