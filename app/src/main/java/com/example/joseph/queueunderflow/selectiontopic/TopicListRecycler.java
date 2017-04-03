package com.example.joseph.queueunderflow.selectiontopic;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.comments.CommentsList;
import com.example.joseph.queueunderflow.headquarters.skills.Skill;
import com.example.joseph.queueunderflow.reputation.ReputationFactory;
import com.example.joseph.queueunderflow.skills.SkillListRecycler;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by josep on 4/3/2017.
 */

public class TopicListRecycler extends RecyclerView.Adapter<com.example.joseph.queueunderflow.selectiontopic.TopicListRecycler.PhotoHolder> {


    private Context context;


    Bitmap theBitmap;
    public ArrayList<Skill> skills;

    public ArrayList<String> getSelectedSkills() {
        return selectedSkills;
    }

    public void setSelectedSkills(ArrayList<String> selectedSkills) {
        this.selectedSkills = selectedSkills;
    }

    public ArrayList<String> selectedSkills;

    public TopicListRecycler(Context context, ArrayList<Skill> skills) {

        this.context = context;
        this.skills = skills;
        this.selectedSkills = new ArrayList<>();


    }


    @Override
    public com.example.joseph.queueunderflow.selectiontopic.TopicListRecycler.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.skillslayout, parent, false);
        return new com.example.joseph.queueunderflow.selectiontopic.TopicListRecycler.PhotoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final com.example.joseph.queueunderflow.selectiontopic.TopicListRecycler.PhotoHolder holder, final int position) {



        holder.mText.setText(skills.get(position).getName());
        final Skill skill = skills.get(position);
        final boolean isSelected = skill.isSelected();


        holder.mRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isSelected){
                    selectedSkills.add(skills.get(position).getName());
                    skill.setSelected(true);
                    skills.set(position,skill);
                }else{
                    for(int i=0;i<selectedSkills.size();i++){
                        if(selectedSkills.get(i).equals(skills.get(position).getName())){
                            selectedSkills.remove(i);
                            skill.setSelected(false);
                            skills.set(position,skill);
                        }
                    }
                }
            }
        });
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




}
