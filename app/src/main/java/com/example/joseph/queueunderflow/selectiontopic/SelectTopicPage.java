package com.example.joseph.queueunderflow.selectiontopic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.alerts.AlertMessage;
import com.example.joseph.queueunderflow.headquarters.skills.Skill;
import com.example.joseph.queueunderflow.home.BasePage;
import com.example.joseph.queueunderflow.skills.SkillListRecycler;
import com.example.joseph.queueunderflow.skills.SuggestSkills;
import com.example.joseph.queueunderflow.submitpost.SubmitQuestion;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectTopicPage extends Activity {

    private ArrayList<Skill> skills;
    private Skill skill;
    private String skillName;
    private TopicListRecycler mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @BindView(R.id.skillslv)
    RecyclerView skillslv;
    @BindView(R.id.nextBtn)
    Button nextBtn;
    @BindView(R.id.searchTxt)
    EditText searchField;
    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_topic_page);
        ButterKnife.bind(this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        skillslv.setLayoutManager(mLinearLayoutManager);

        new LoadTopics().execute();

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchText = searchField.getText().toString();



                searchFunct(searchText);



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private class LoadTopics extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            skills = new ArrayList<>();

            ParseQuery fetchSkills = new ParseQuery("Skills");
            fetchSkills.orderByDescending("createdAt");



            fetchSkills.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(java.util.List<ParseObject> objects, ParseException e) {

                    if(e == null){
                        for(ParseObject userData:objects ){

                            skill = new Skill();

                            //Gets the name of the Skill
                            skillName = userData.getString("name");

                            //Gets the icon of the Skill
                            ParseFile skillImage = (ParseFile) userData.get("icon");
                            String imageUrl = skillImage.getUrl() ;//live url
                            Uri imageUri = Uri.parse(imageUrl);


                            //Set name and icon
                            skill.setSkillUrl(imageUri);
                            skill.setName(skillName);
                            skills.add(skill);

                        }



                        mAdapter = new TopicListRecycler(SelectTopicPage.this, skills);

                        skillslv.setAdapter(mAdapter);

                        nextBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(mAdapter.getSelectedSkills().size()<3){
                                    AlertMessage alertMessage = new AlertMessage("You need to select at least 3 topics",SelectTopicPage.this);
                                    alertMessage.show();

                                }else{
                                    ParseQuery userQuery = new ParseQuery("_User");
                                    userQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                                    userQuery.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(java.util.List<ParseObject> objects, ParseException e) {

                                            if (e == null) {
                                                for (ParseObject userData : objects) {

                                                   userData.add("skills",mAdapter.getSelectedSkills());
                                                    userData.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if(e == null){
                                                                Intent intent = new Intent(SelectTopicPage.this, BasePage.class);
                                                                startActivity(intent);
                                                            }else{
                                                                AlertMessage errorMessage = new AlertMessage(e.getMessage(),SelectTopicPage.this);
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });
                                }





                            }
                        });

                        //skills = new ArrayList<Skill>();


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

    public void searchFunct(final String theTxT){
        skills = new ArrayList<>();

        ParseQuery fetchSkills = new ParseQuery("Skills");
        fetchSkills.orderByDescending("createdAt");
        fetchSkills.whereContains("name", theTxT);

        fetchSkills.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(java.util.List<ParseObject> objects, ParseException e) {

                if(e == null){
                    for(ParseObject userData:objects ){

                        skill = new Skill();

                        //Gets the name of the Skill
                        skillName = userData.getString("name");

                        //Gets the icon of the Skill
                        ParseFile skillImage = (ParseFile) userData.get("icon");
                        String imageUrl = skillImage.getUrl() ;//live url
                        Uri imageUri = Uri.parse(imageUrl);

                        //Set name and icon
                        skill.setSkillUrl(imageUri);
                        skill.setName(skillName);
                        if(searchText.equals(theTxT)){
                            skills.add(skill);
                        }


                    }





                    mAdapter.updateSkillsList(skills);


                    //skills = new ArrayList<Skill>();


                }

            }


        });

    }

}
