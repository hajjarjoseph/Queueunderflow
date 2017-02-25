package com.example.joseph.queueunderflow.headquarters.skills;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.authentication.askquestion.AskQuestionMain;
import com.example.joseph.queueunderflow.headquarters.MainPage;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josep on 2/7/2017.
 */
public class SkillsRecycler extends RecyclerView.Adapter<SkillsRecycler.PhotoHolder> {

    private ArrayList<Skill> skills;
    private ArrayList<Skill> arraylist;
    private ArrayList<String> skillsPicked;
    private boolean isSelected;
    private boolean fromAsk;


    private Context context;


    public SkillsRecycler(MainPage mainActivity, ArrayList<Skill> skills,ArrayList<String>skillsPicked) {
        this.skills = skills;
        context = mainActivity;
        this.skillsPicked = skillsPicked;


        this.arraylist = new ArrayList<Skill>();
        this.arraylist.addAll(skills);

        if(this.skillsPicked == null){
            this.skillsPicked = new ArrayList<>();
        }
        fromAsk = false;


    }

    public SkillsRecycler(AskQuestionMain mainActivity, ArrayList<Skill> skills, ArrayList<String>skillsPicked) {
        this.skills = skills;
        context = mainActivity;
        this.skillsPicked = skillsPicked;

        this.arraylist = new ArrayList<Skill>();
        this.arraylist.addAll(skills);

        if(this.skillsPicked == null){
            this.skillsPicked = new ArrayList<>();
        }

        fromAsk = true;


    }

    @Override
    public SkillsRecycler.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pickskillscell, parent, false);
        return new PhotoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final SkillsRecycler.PhotoHolder holder, final int position) {



        final Skill skill = skills.get(position);
        //holder.bindPhoto(post);



      holder.bindPhoto(skill);

        if(skill.isSelected()){
            holder.addBtn.setImageResource(R.drawable.dltico);
        }else{
            holder.addBtn.setImageResource(R.drawable.addbtn);
        }

        holder.skillName.setText(skill.getName());

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 isSelected = skill.isSelected();
                if(!isSelected){
                    holder.addBtn.setImageResource(R.drawable.dltico);
                    isSelected = true;
                    skill.setSelected(isSelected);
                    skills.set(position,skill);
                    skillsPicked.add(skill.getName());



                    arraylist.clear();
                    arraylist.addAll(skills);

                    if(fromAsk){
                        if(skillsPicked.size()>0) {
                            ((AskQuestionMain) context).setTagFirst(skill.getName(), skill.getSkillUrl());
                        }
                    }else{

                        ParseQuery<ParseObject> updateSkills = ParseQuery.getQuery("_User");
                        updateSkills.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                        updateSkills.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if(e == null){
                                    for (int i = 0; i < objects.size(); i++) {
                                        ParseObject object = objects.get(i);
                                        object.put("skills",skillsPicked);
                                        object.saveInBackground();

                                    }
                                }else{

                                    createAlert("Something Went Wrong please try again");

                                    holder.addBtn.setImageResource(R.drawable.addbtn);
                                    isSelected = false;
                                    skill.setSelected(isSelected);
                                    skills.set(position,skill);

                                    arraylist.clear();
                                    arraylist.addAll(skills);

                                    for(int i=0;i<skillsPicked.size();i++){
                                        if(skillsPicked.get(i).equals(skill.getName())){
                                            skillsPicked.remove(i);
                                        }
                                    }


                                }
                            }
                        });


                    }






                }else{
                    holder.addBtn.setImageResource(R.drawable.addbtn);
                    isSelected = false;
                    skill.setSelected(isSelected);
                    skills.set(position,skill);
                    arraylist.clear();
                    arraylist.addAll(skills);


                    for(int i=0;i<skillsPicked.size();i++){
                        if(skillsPicked.get(i).equals(skill.getName())){
                            skillsPicked.remove(i);
                        }
                    }



                    ParseQuery<ParseObject> updateSkills = ParseQuery.getQuery("_User");
                    updateSkills.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    updateSkills.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(e == null){
                                for (int i = 0; i < objects.size(); i++) {
                                    ParseObject object = objects.get(i);
                                    object.put("skills",skillsPicked);
                                    object.saveInBackground();

                                }
                            }else{

                                createAlert("Something Went Wrong please try again");

                                holder.addBtn.setImageResource(R.drawable.dltico);
                                isSelected = true;
                                skill.setSelected(isSelected);
                                skills.set(position,skill);
                                skillsPicked.add(skill.getName());

                                arraylist.clear();
                                arraylist.addAll(skills);


                            }
                        }
                    });




                }

            }
        });






    }




    @Override
    public int getItemCount() {
        return skills.size();
    }

    public static class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {




        //2
        ImageView skillImage;
        ImageView addBtn;
        TextView skillName;







        //3
        private static final String PHOTO_KEY = "PHOTO";

        //4
        public PhotoHolder(View v) {
            super(v);


            skillImage=(ImageView) v.findViewById(R.id.skillPic);
            addBtn=(ImageView) v.findViewById(R.id.addBtn);
            skillName=(TextView) v.findViewById(R.id.skillName);


            v.setOnClickListener(this);







        }

        //5
        @Override
        public void onClick(View v) {

        }


        public void bindPhoto(Skill skill) {

            final Uri imageUri = skill.getSkillUrl();

            Glide.with(skillImage.getContext()).load(imageUri).dontAnimate().into(skillImage);





        }





    }

    public void filter(String charText) {

        skills.clear();
        if (charText.length() == 0) {
            Skill tstSkill = arraylist.get(0);
            Log.d(SkillsRecycler.class.getSimpleName(),"el meshkle eno hiye : " + tstSkill.isSelected());
            skills.addAll(arraylist);
        } else {
            for (Skill sk : arraylist) {
                if (sk.getName()
                        .contains(charText)) {
                    skills.add(sk);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void createAlert(String message){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.customdialog);

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