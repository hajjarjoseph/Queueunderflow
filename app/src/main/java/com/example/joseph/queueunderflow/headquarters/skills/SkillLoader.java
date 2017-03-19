package com.example.joseph.queueunderflow.headquarters.skills;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.authentication.askquestion.AskQuestionMain;
import com.example.joseph.queueunderflow.headquarters.MainPage;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by josep on 2/10/2017.
 */
public class SkillLoader {


    private Context mContext;

    private ArrayList<String> skillsPicked;
    private ArrayList<Skill>  skills;
    private Skill skill;
    private String skillName;


    private Dialog dialogSkills;
    private RecyclerView skillslv;
    private EditText searchSkill;
    private ImageView donePickBtn;
    private LinearLayoutManager mLinearLayoutManager;
    private SkillsRecycler mAdapter;


    private boolean fromAsk = false;







    public SkillLoader(AskQuestionMain mainActvity){


        //Load Data Coming from the Main Page

        this.mContext = mainActvity;
        fromAsk = true;



        /************************************************* Init Dialog properties ***********************************************************/
        dialogSkills = new Dialog(mContext);
        dialogSkills.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSkills.setContentView(R.layout.pickskills);

        skillslv = (RecyclerView) dialogSkills.findViewById(R.id.skillslv);
        searchSkill = (EditText) dialogSkills.findViewById(R.id.searchSkill);
        donePickBtn = (ImageView) dialogSkills.findViewById(R.id.donePickBtn);
        RelativeLayout dialogBox = (RelativeLayout) dialogSkills.findViewById(R.id.dialogBox);

        mLinearLayoutManager = new LinearLayoutManager(mContext);
        skillslv.setLayoutManager(mLinearLayoutManager);

        /************************************************* Init Dialog properties ***********************************************************/






        /************************************************* Dismiss Dialog when the Done Button is Taped ***********************************************************/

        donePickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSkills.dismiss();

            }
        });

        /************************************************* Dismiss Dialog when the Done Button is Taped ***********************************************************/





        /************************************************* Dismiss Keyboard when Users Taps on the Screen ***********************************************************/
        dialogBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        /************************************************* Dismiss Keyboard when Users Taps on the Screen ***********************************************************/


        /************************************************* Search for Particular Skill ***********************************************************/
        searchSkill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchTxt = searchSkill.getText().toString();
                searchSkills(searchTxt);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /************************************************* Search for Particular Skill ***********************************************************/

    }







    public SkillLoader(ArrayList<String> skillsPicked, MainPage mainActvity){


        //Load Data Coming from the Main Page
        this.skillsPicked = skillsPicked;
        this.mContext = mainActvity;

        Collections.sort(this.skillsPicked);

        /************************************************* Init Dialog properties ***********************************************************/
        dialogSkills = new Dialog(mContext);
        dialogSkills.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSkills.setContentView(R.layout.pickskills);

        skillslv = (RecyclerView) dialogSkills.findViewById(R.id.skillslv);
        searchSkill = (EditText) dialogSkills.findViewById(R.id.searchSkill);
        donePickBtn = (ImageView) dialogSkills.findViewById(R.id.donePickBtn);
        RelativeLayout dialogBox = (RelativeLayout) dialogSkills.findViewById(R.id.dialogBox);

        mLinearLayoutManager = new LinearLayoutManager(mContext);
        skillslv.setLayoutManager(mLinearLayoutManager);

        /************************************************* Init Dialog properties ***********************************************************/






        /************************************************* Dismiss Dialog when the Done Button is Taped ***********************************************************/

        donePickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSkills.dismiss();
                ((MainPage)mContext).giveOrderQueue();
            }
        });

        /************************************************* Dismiss Dialog when the Done Button is Taped ***********************************************************/





        /************************************************* Dismiss Keyboard when Users Taps on the Screen ***********************************************************/
        dialogBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        /************************************************* Dismiss Keyboard when Users Taps on the Screen ***********************************************************/


        /************************************************* Search for Particular Skill ***********************************************************/
        searchSkill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchTxt = searchSkill.getText().toString();
                searchSkills(searchTxt);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /************************************************* Search for Particular Skill ***********************************************************/

    }

    public int searchUserSkills(){
        int index =  Collections.binarySearch(skillsPicked,skillName);
        return index;
    }

    public void loadSkills(){

        skills = new ArrayList<>();

        ParseQuery fetchSkills = new ParseQuery("Skills");
        fetchSkills.orderByDescending("createdAt");
        fetchSkills.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(java.util.List<ParseObject> objects, ParseException e) {

                if(e == null){
                    for(ParseObject userData:objects){

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



                        if(fromAsk){
                            skill.setSelected(false);
                        }else{
                            if(searchUserSkills() <= -1){
                                skill.setSelected(false);
                            }else{
                                skill.setSelected(true);
                            }
                        }



                        skills.add(skill);

                    }


                    skillslv.setAdapter(mAdapter);
                    skills = new ArrayList<Skill>();
                    dialogSkills.show();

                }

            }


        });





    }

    public void searchSkills(String searchTxT){
       mAdapter.filter(searchTxT);
    }

    public void setTag(){
      dialogSkills.dismiss();

    }

}
