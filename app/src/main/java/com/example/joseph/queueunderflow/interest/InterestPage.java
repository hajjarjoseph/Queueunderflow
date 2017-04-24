package com.example.joseph.queueunderflow.interest;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.headquarters.skills.Skill;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InterestPage extends AppCompatActivity {

    private ArrayList<Skill> skills;
    private Skill skill;
    private LinearLayoutManager mLinearLayoutManager;
    private InterestRecycler mAdapter;
    @BindView(R.id.backButton)
    ImageButton backButton;
    @BindView(R.id.interestlv)
    RecyclerView interestlv;
    private String theUser = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_page);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            theUser = extras.getString("theUser");
        }else {
            theUser = ParseUser.getCurrentUser().getUsername();
        }

        mLinearLayoutManager = new LinearLayoutManager(this);
        interestlv.setLayoutManager(mLinearLayoutManager);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        new LoadInterest().execute();
    }




    private class LoadInterest extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            skills = new ArrayList<>();

            ParseQuery fetchSkillsL = new ParseQuery("_User");
            if(!theUser.isEmpty()){
                fetchSkillsL.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            }else{
                fetchSkillsL.whereEqualTo("username", theUser);
            }



            fetchSkillsL.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(java.util.List<ParseObject> objects, ParseException e) {

                    if(e == null){
                        for(ParseObject userData:objects ){



                            //Gets the name of the Skill
                            ArrayList<String> skillNames = (ArrayList<String>) userData.get("skills");

                            ParseQuery fetchSkills = new ParseQuery("Skills");
                            fetchSkills.orderByDescending("createdAt");
                            fetchSkills.whereContainedIn("name",skillNames);


                            fetchSkills.findInBackground(new FindCallback<ParseObject>() {
                                                             @Override
                                                             public void done(java.util.List<ParseObject> objects, ParseException e) {

                                                                 if (e == null) {
                                                                     for (ParseObject userData : objects) {
                                                                         skill = new Skill();

                                                                         String skillName = userData.getString("name");

                                                                         //Gets the icon of the Skill
                                                                         ParseFile skillImage = (ParseFile) userData.get("icon");
                                                                         String imageUrl = skillImage.getUrl() ;//live url
                                                                         Uri imageUri = Uri.parse(imageUrl);


                                                                         //Set name and icon
                                                                         skill.setSkillUrl(imageUri);
                                                                         skill.setName(skillName);
                                                                         skills.add(skill);

                                                                     }

                                                                     mAdapter = new InterestRecycler(InterestPage.this,skills);
                                                                     interestlv.setAdapter(mAdapter);

                                                                 }
                                                             }
                                                         });





                        }






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
