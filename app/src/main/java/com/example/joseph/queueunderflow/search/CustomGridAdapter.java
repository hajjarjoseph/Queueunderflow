package com.example.joseph.queueunderflow.search;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.NoAnimation;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.authentication.IntroPage;
import com.example.joseph.queueunderflow.authentication.askquestion.AskQuestionMain;
import com.example.joseph.queueunderflow.basicpost.BasicPost;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.cardpage.CardPage;
import com.example.joseph.queueunderflow.headquarters.MainPage;
import com.example.joseph.queueunderflow.headquarters.QuestionsList;
import com.example.joseph.queueunderflow.headquarters.queuebuilder.QueueBuilder;
import com.example.joseph.queueunderflow.headquarters.skills.Skill;
import com.example.joseph.queueunderflow.search.SearchPage;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by josep on 2/19/2017.
 */
public class CustomGridAdapter extends BaseAdapter {
    private Context mContext;
    LayoutInflater layoutInflater;
    private ArrayList<Skill>skillsList;

    // Gets the context so it can be used later
    public CustomGridAdapter(Context c) {
        mContext = c;
        skillsList = new ArrayList<>();
    }

    public CustomGridAdapter(Context c,ArrayList<Skill>skillsList) {
        mContext = c;
        this.skillsList = skillsList;
    }

    // Total number of things contained within the adapter
    public int getCount() {
        return skillsList.size();
    }

    // Require for structure, not really used in my code.
    public Object getItem(int position) {
        return null;
    }

    // Require for structure, not really used in my code. Can
    // be used to get the id of an item in the adapter for
    // manual control.
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position,
                        View convertView, ViewGroup parent) {
        TextView item1Name = null;
        ImageView item1Img = null;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView= inflater.inflate(R.layout.tagsgrid, parent, false);
            item1Name=(TextView) convertView.findViewById(R.id.item1Name);
            item1Img=(ImageView) convertView.findViewById(R.id.item1Img);



            if(skillsList.size()>0){
                Skill skill = skillsList.get(position);
                item1Name.setText(skill.getName());
                Glide.with(mContext).load(skill.getSkillUrl()).into(item1Img);
            }













        return convertView;
    }




    public void confirmationAlert(final String postId){
        final Dialog dialog = new Dialog(mContext);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.setContentView(R.layout.answerdialog);

        final TextView answerMsg = (TextView) dialog.findViewById(R.id.answerMsg);
        final TextView yesBtn = (TextView) dialog.findViewById(R.id.yesBtnAns);
        final TextView noBtn = (TextView) dialog.findViewById(R.id.noBtnAns);
        final ProgressBar prog = (ProgressBar) dialog.findViewById(R.id.progressAns);
        final ImageView doneImg = (ImageView) dialog.findViewById(R.id.doneImg);

        answerMsg.setText("Are you sure you want to flag this post?");

        prog.setVisibility(View.INVISIBLE);
        doneImg.setVisibility(View.INVISIBLE);

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerMsg.setVisibility(View.INVISIBLE);
                yesBtn.setVisibility(View.INVISIBLE);
                noBtn.setVisibility(View.INVISIBLE);
                prog.setVisibility(View.VISIBLE);

                prog.getIndeterminateDrawable().setColorFilter(Color.parseColor("#32BEA6"), PorterDuff.Mode.SRC_IN);

                ParseQuery query = new ParseQuery("Questions");
                query.whereEqualTo("objectId",postId);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(java.util.List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            for (final ParseObject userData : objects) {
                                userData.put("flagged",true);

                                userData.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e == null){
                                            prog.setVisibility(View.INVISIBLE);
                                            dialog.dismiss();
                                            thankYouMessage();

                                        }
                                    }
                                });
                            }
                        }
                    }
                });


            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public void createAlert(String message){
        final Dialog dialog = new Dialog(mContext);
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

    public void thankYouMessage(){
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.thankyoumessage);

        dialog.show();
    }

    public void optionAlert(final String postId){
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.setContentView(R.layout.optiondialog);

        RelativeLayout flagLayout = (RelativeLayout) dialog.findViewById(R.id.flagPost);

        flagLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                confirmationAlert(postId);
            }
        });


        dialog.show();
    }

    public String nametoDrawable(String skillName){
        String skillImgName = "drawable/";
        skillName = skillName.toLowerCase();
        skillImgName += skillName;
        skillImgName =  skillImgName.replaceAll("\\++","plus");



        return skillImgName;
    }


}




