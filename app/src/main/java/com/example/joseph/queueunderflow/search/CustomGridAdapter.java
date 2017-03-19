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
public class CustomGridAdapter extends RecyclerView.Adapter<CustomGridAdapter.PhotoHolder> {

    private ArrayList<BasicPost> items;



    private Context context;


    public CustomGridAdapter(SearchPage mainActivity) {

        context = mainActivity;

    }


    @Override
    public CustomGridAdapter.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tagsgrid, parent, false);
        return new PhotoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final CustomGridAdapter.PhotoHolder holder, final int position) {




        if(position == 0){
            holder.item1Name.setText("Math");
            holder.item1Img.setImageResource(R.drawable.mathico);

            holder.item2Name.setText("Physics");
            holder.item2Img.setImageResource(R.drawable.physicsico);

            holder.item3Name.setText("Writing");
            holder.item3Img.setImageResource(R.drawable.writing);


        }else if(position == 2){
            holder.item1Name.setText("Biology");
            holder.item1Img.setImageResource(R.drawable.biologyico);

            holder.item2Name.setText("C++");

            holder.item3Name.setText("Chemistry");
            holder.item3Img.setImageResource(R.drawable.chemistryico);


        }else if(position == 1){
            holder.item1Name.setText("Java");
            holder.item1Img.setImageResource(R.drawable.java);


            holder.item1Img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d(CustomGridAdapter.class.getSimpleName(),"okay nehna jouwa");
                    ((SearchPage)context).tagPress(holder.item1Name.getText().toString());


                }
            });


            holder.item2Name.setText("Space");
            holder.item2Img.setImageResource(R.drawable.astronomy);


            holder.item3Name.setText("Cars");
            holder.item3Img.setImageResource(R.drawable.cars);






        }






    }




    @Override
    public int getItemCount() {

        return 3;
    }

    public static class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {




        //2
        TextView item1Name,item2Name,item3Name;
        ImageView item1Img,item2Img,item3Img;









        //3
        private static final String PHOTO_KEY = "PHOTO";

        //4
        public PhotoHolder(View v) {
            super(v);





            item1Name=(TextView) v.findViewById(R.id.item1Name);
            item1Img=(ImageView) v.findViewById(R.id.item1Img);

            item2Name=(TextView) v.findViewById(R.id.item2Name);
            item2Img=(ImageView) v.findViewById(R.id.item2Img);

            item3Name=(TextView) v.findViewById(R.id.item3Name);
            item3Img=(ImageView) v.findViewById(R.id.item3Img);

            v.setOnClickListener(this);









        }

        //click method
        @Override
        public void onClick(View v) {



        }


        public void bindPhoto(Skill skill) {

            final Uri imageUri = skill.getSkillUrl();







        }





    }



    public void confirmationAlert(final String postId){
        final Dialog dialog = new Dialog(context);

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

    public void thankYouMessage(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.thankyoumessage);

        dialog.show();
    }

    public void optionAlert(final String postId){
        final Dialog dialog = new Dialog(context);
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