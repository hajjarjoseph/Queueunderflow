package com.example.joseph.queueunderflow.interest;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.headquarters.skills.Skill;
import com.example.joseph.queueunderflow.skills.SkillListRecycler;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by josep on 4/4/2017.
 */

public class InterestRecycler extends RecyclerView.Adapter<com.example.joseph.queueunderflow.interest.InterestRecycler.PhotoHolder> {


    private Context context;


    Bitmap theBitmap;
    public ArrayList<Skill> skills;
    public ArrayList<Skill> fullSkills;

    public ArrayList<String> getSelectedSkills() {
        return selectedSkills;
    }

    public void setSelectedSkills(ArrayList<String> selectedSkills) {
        this.selectedSkills = selectedSkills;
    }

    public ArrayList<String> selectedSkills;

    public InterestRecycler(Context context, ArrayList<Skill> skills) {

        this.context = context;
        this.skills = new ArrayList<>();
        for(int i=0;i<skills.size();i++){
            this.skills.add(skills.get(i));
        }
        this.selectedSkills = new ArrayList<>();





    }




    @Override
    public com.example.joseph.queueunderflow.interest.InterestRecycler.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.skillslayout, parent, false);
        return new com.example.joseph.queueunderflow.interest.InterestRecycler.PhotoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final com.example.joseph.queueunderflow.interest.InterestRecycler.PhotoHolder holder, final int position) {



        holder.mText.setText(skills.get(position).getName());

        holder.mRadio.setVisibility(View.GONE);


        final Uri imageUri = skills.get(position).getSkillUrl();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    theBitmap = Glide.
                            with(context).
                            load(imageUri).
                            asBitmap().
                            into(-1,-1).
                            get();
                } catch (final ExecutionException e) {

                } catch (final InterruptedException e) {

                }
                return null;
            }
            @Override
            protected void onPostExecute(Void dummy) {
                if (null != theBitmap) {
                    // The full bitmap should be available here
                    Log.d(SkillListRecycler.class.getSimpleName(),"Image is not null?");
                    holder.mImg.setImageBitmap(theBitmap);

                }else{
                    Log.d(SkillListRecycler.class.getSimpleName(),"Image is null?");
                }
            }
        }.execute();

    }


    @Override
    public int getItemCount() {
        return skills.size();
    }

    public static class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        //2

        public RadioButton mRadio;
        public TextView mText;
        public ImageView mImg;




        //3
        private static final String PHOTO_KEY = "PHOTO";

        //4
        public PhotoHolder(View v) {
            super(v);


            mText = (TextView) v.findViewById(R.id.skillName);
            mRadio = (RadioButton) v.findViewById(R.id.skillPicked);
            mImg = (ImageView) v.findViewById(R.id.skillPic);


            v.setOnClickListener(this);


        }

        //click method
        @Override
        public void onClick(View v) {

        }





    }

    public void updateSkillsList(ArrayList<Skill>items) {
        skills.clear();
        skills.addAll(items);
        this.notifyDataSetChanged();
    }




    public void createAlert(String message) {
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




    public String nametoDrawable(String skillName) {
        String skillImgName = "drawable/";
        skillName = skillName.toLowerCase();
        skillImgName += skillName;
        skillImgName = skillImgName.replaceAll("\\++", "plus");

        Log.d(com.example.joseph.queueunderflow.QuestRecycler.class.getSimpleName(), "el ossa eno : " + skillImgName);

        return skillImgName;
    }

    private  void updateFullSkills(Skill skill){
        for(int i=0;i<fullSkills.size();i++){
            if(fullSkills.get(i).getName().equals(skill.getName())){
                fullSkills.set(i,skill);
            }
        }
    }

    public void searchList(String searchText){
        if(searchText.equals("")){
            skills = new ArrayList<>();
            for(int i=0;i<fullSkills.size();i++){
                skills.add(fullSkills.get(i));
                notifyDataSetChanged();
            }
        }else{
            skills = new ArrayList<>();
            for(int i=0;i<fullSkills.size();i++){
                if(fullSkills.get(i).getName().toLowerCase().contains(searchText.toLowerCase())){
                    skills.add(fullSkills.get(i));
                    notifyDataSetChanged();
                }
            }
        }
    }




}
