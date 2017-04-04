package com.example.joseph.queueunderflow.skills;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.headquarters.skills.Skill;
import com.example.joseph.queueunderflow.home.BasePage;
import com.example.joseph.queueunderflow.submitpost.SubmitQuestion;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SuggestSkills extends AppCompatActivity {

    @BindView(R.id.skillslv)
    RecyclerView skillslv;
    @BindView(R.id.nextBtn)
    Button nextBtn;
    @BindView(R.id.searchTxt)
    EditText searchField;
    @BindView(R.id.backButton)
    ImageButton backButton;

    private ArrayList<Skill> skills;
    Skill skill;
    String skillName;
    SkillListRecycler mAdapter;
    SuggestSkills mContext;
    private LinearLayoutManager mLinearLayoutManager;

    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_skills);
        ButterKnife.bind(this);
        mContext = this;




        mLinearLayoutManager = new LinearLayoutManager(this);
        skillslv.setLayoutManager(mLinearLayoutManager);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new LoadQuests().execute();

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchText = searchField.getText().toString();

                mAdapter.setSelectedIndex(-1);

                searchFunct(searchText);



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




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



    private class SearchSkills extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            skills = new ArrayList<>();

            ParseQuery fetchSkills = new ParseQuery("Skills");
            fetchSkills.orderByDescending("createdAt");
            fetchSkills.whereContains("name", searchText);

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





                        mAdapter.notifyDataSetChanged();


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




    private class LoadQuests extends AsyncTask<Void, Void, Void> {
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



                        mAdapter = new SkillListRecycler(mContext, skills);

                        skillslv.setAdapter(mAdapter);

                        nextBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                int index = mAdapter.getSelectedIndex();
                                Log.d(SuggestSkills.class.getSimpleName(),"okay selected index is " + index);
                                Intent intent = new Intent(view.getContext(), SubmitQuestion.class);
                                intent.putExtra("skill",skills.get(index).getName());
                                intent.putExtra("fromActivity",1);
                                startActivity(intent);


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

}
