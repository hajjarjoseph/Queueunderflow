package com.example.joseph.queueunderflow.cardpage;

/**
 * Created by josep on 2/22/2017.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.joseph.queueunderflow.QuestItem;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.basicpost.BasicPost;
import com.example.joseph.queueunderflow.basicpost.basicanswer.BasicAnswer;
import com.example.joseph.queueunderflow.basicpost.basicanswer.imageanswer.ImageAnswer;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.basicpost.basicquestion.imagequestion.ImageQuestion;
import com.example.joseph.queueunderflow.headquarters.QuestionsList;
import com.example.joseph.queueunderflow.headquarters.skills.Skill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;



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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joseph.queueunderflow.authentication.askquestion.AskQuestionMain;
import com.example.joseph.queueunderflow.headquarters.MainPage;
import com.example.joseph.queueunderflow.headquarters.QuestionsList;
import com.example.joseph.queueunderflow.headquarters.queuebuilder.QueueBuilder;
import com.example.joseph.queueunderflow.headquarters.skills.Skill;
import com.example.joseph.queueunderflow.headquarters.skills.SkillsRecycler;
import com.example.joseph.queueunderflow.submitpost.SubmitQuestion;
import com.example.joseph.queueunderflow.viewpager.ViewPagerAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import android.os.Handler;
import java.util.zip.InflaterInputStream;

import butterknife.BindView;

/**
 * Created by josep on 2/19/2017.
 */
public class PostRecycler extends RecyclerView.Adapter<PostRecycler.PhotoHolder>  {

    private ArrayList<BasicPost> items;
    private BasicQuestion theQuestion;
    private ArrayList<BasicAnswer> answersList;


private RecyclerView postlv;

    private Context context;
    private  PostRecycler mAdapter;


    public PostRecycler(CardPage mainActivity,BasicQuestion theQuestion, PostRecycler mAdapter) {

        context = mainActivity;
        this.theQuestion = theQuestion;
        this.mAdapter = mAdapter;
        this.answersList = new ArrayList<>();



    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return 0;
        }else{
            return 1;
        }

    }


    @Override
    public PostRecycler.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View   inflatedView = null;
        switch (viewType) {
            case 1:
                 inflatedView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.answerwimage, parent, false);
                return new PhotoHolder(inflatedView);


            case 0:
                 inflatedView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.postwimage, parent, false);
                return new PhotoHolder(inflatedView);

        }

        return new PhotoHolder(inflatedView);

    }






    @Override
    public void onBindViewHolder(final PostRecycler.PhotoHolder holder, final int position) {


        //It's An answer
        if(getItemViewType(position) == 1){

            String title = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            long time = theQuestion.getAnswersList().get(position-1).getPostDate().getTime();
            long now = System.currentTimeMillis();


            answersList = theQuestion.getAnswersList();


            BasicAnswer theAnswer = answersList.get(position-1);


            holder.postOwner.setText(theAnswer.getqOwner().toString());

            String currUsr = "#";
            currUsr += ParseUser.getCurrentUser().getUsername();

            if(theQuestion.isHasAnswer()){
                if(position == 1){
                    holder.answerPicker.setVisibility(View.VISIBLE);
                }
            }

            if(currUsr.equals(theQuestion.getqOwner().toString())){
                if(!theQuestion.isHasAnswer()) {
                    holder.pickAnswerBtn.setVisibility(View.VISIBLE);

                    holder.pickAnswerBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            createAnswerDialog(position - 1);
                        }
                    });
                }else{
                    holder.pickAnswerBtn.setVisibility(View.INVISIBLE);
                }

            }



            holder.postDescription.setText(theAnswer.getqDescription().toString());

            if(theAnswer instanceof ImageAnswer){
                ArrayList<String> urlList = ((ImageAnswer) theAnswer).getImagesUri();
                ArrayList<Uri>uriList = new ArrayList<>();
                for(int i=0;i<urlList.size();i++){
                    Uri imageUri = Uri.parse(urlList.get(i).toString());
                    uriList.add(imageUri);
                }
                holder.mViewPagerAdapter = new ViewPagerAdapter(context, uriList,holder);


                holder.intro_images.setAdapter(holder.mViewPagerAdapter);
                holder.intro_images.setCurrentItem(0);


            }else{
                ArrayList<Uri>uriList = new ArrayList<>();
                holder.mViewPagerAdapter = new ViewPagerAdapter(context, uriList,holder);


                holder.intro_images.setAdapter(holder.mViewPagerAdapter);
                holder.intro_images.setCurrentItem(0);
            }





            //Sets Time of Post


            if(now - time <60000){
                holder.timeago.setText("now");
            }else{
                CharSequence ago =
                        DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);


                holder.timeago.setText(ago);
            }



            holder.setUiPageViewController();



        }else if(getItemViewType(position) == 0){
            String title = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            long time = theQuestion.getPostDate().getTime();
            long now = System.currentTimeMillis();

            holder.postOwner.setText(theQuestion.getqOwner().toString());


            title = theQuestion.getqTitle().toString();
            holder.postTitle.setText(title);
            holder.postDescription.setText(theQuestion.getqDescription().toString());


            holder.options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position == 0){
                        if(theQuestion.getqOwner().equals("#" + ParseUser.getCurrentUser().getUsername())){
                            editAlert(0);
                        }else{
                            optionAlert(theQuestion.getPostId());
                        }
                    }else if(position > 0){
                        if(theQuestion.getAnswersList().get(position).getqOwner().equals(ParseUser.getCurrentUser().getUsername())){

                        }else{
                            optionAlert(theQuestion.getPostId());
                        }
                    }


                }
            });

            if(theQuestion instanceof ImageQuestion){
                ArrayList<String> urlList = ((ImageQuestion) theQuestion).getImagesUri();
                ArrayList<Uri>uriList = new ArrayList<>();
                for(int i=0;i<urlList.size();i++){
                    Uri imageUri = Uri.parse(urlList.get(i).toString());
                    uriList.add(imageUri);
                }
                holder.mViewPagerAdapter = new ViewPagerAdapter(context, uriList,holder);


                holder.intro_images.setAdapter(holder.mViewPagerAdapter);
                holder.intro_images.setCurrentItem(0);


            }else{
                ArrayList<Uri>uriList = new ArrayList<>();
                holder.mViewPagerAdapter = new ViewPagerAdapter(context, uriList,holder);


                holder.intro_images.setAdapter(holder.mViewPagerAdapter);
                holder.intro_images.setCurrentItem(0);
            }





            if(now - time <60000){
                holder.timeago.setText("now");
            }else{
                CharSequence ago =
                        DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);


                holder.timeago.setText(ago);
            }




            holder.setUiPageViewController();
        }




    }






    @Override
    public int getItemCount() {
        int number = 1;
        number += theQuestion.getAnswersList().size();
        return number;
    }





 public static class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener,ViewPager.OnPageChangeListener{





        //2
        TextView postOwner;
        TextView postTitle;
        TextView postDescription;
        ViewPager intro_images;
        ImageView answerPicker;
        Button pickAnswerBtn;
        ImageView options;
        LinearLayout pager_indicator;
        private int dotsCount;
        private ImageView[] dots;
        private ViewPagerAdapter mViewPagerAdapter;
        private Context contxt;
        private TextView timeago;

     private ArrayList<Integer> calcHeights;




        public void resetPager(){
            int visibility = intro_images.getVisibility();
            intro_images.setVisibility(View.GONE);
            intro_images.setVisibility(visibility);
        }




        //3
        private static final String PHOTO_KEY = "PHOTO";

        //4
        public PhotoHolder(View v) {
            super(v);






            postOwner=(TextView) v.findViewById(R.id.postOwner);
            timeago=(TextView) v.findViewById(R.id.timeago);
            postTitle=(TextView) v.findViewById(R.id.postTitle);
            pickAnswerBtn=(Button) v.findViewById(R.id.pickAnswerBtn);
            answerPicker=(ImageView) v.findViewById(R.id.theAnsPick);
            options=(ImageView) v.findViewById(R.id.postOption);
            postDescription=(TextView) v.findViewById(R.id.postDescription);
            intro_images = (ViewPager) v.findViewById(R.id.pager_introduction);
            pager_indicator = (LinearLayout) v.findViewById(R.id.viewPagerCountDots);


            contxt = v.getContext();

            calcHeights = new ArrayList<>();



            v.setOnClickListener(this);


            intro_images.setOnPageChangeListener(this);


            if (calcHeights.size() == 0){
                intro_images.getLayoutParams().height = 0;
            }



        }





        //5
        @Override
        public void onClick(View v) {

        }

        private void setUiPageViewController() {

            pager_indicator.removeAllViews();
            dotsCount = mViewPagerAdapter.getCount();
            dots = new ImageView[dotsCount];

            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(contxt);
                dots[i].setImageDrawable(contxt.getResources().getDrawable(R.drawable.nonselecteditem_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);

                pager_indicator.addView(dots[i], params);
            }


            if(dotsCount>0) {
                dots[0].setImageDrawable(contxt.getResources().getDrawable(R.drawable.selecteditem_dot));
            }
        }



        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < dotsCount; i++) {
                dots[i].setImageDrawable(contxt.getResources().getDrawable(R.drawable.nonselecteditem_dot));
            }

            dots[position].setImageDrawable(contxt.getResources().getDrawable(R.drawable.selecteditem_dot));


            if(calcHeights.size()>position) {
                intro_images.getLayoutParams().height = calcHeights.get(position);
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }



        public void bindPhoto(int calcHeight) {





            calcHeights.add(calcHeight);





        }

     public  void firstPic(int calcHeight){
         calcHeights.add(calcHeight);
         intro_images.getLayoutParams().height = calcHeights.get(0);

     }





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

    public void createAnswerDialog(final int pos){
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.setContentView(R.layout.answerdialog);

        final TextView answerMsg = (TextView) dialog.findViewById(R.id.answerMsg);
        final TextView yesBtn = (TextView) dialog.findViewById(R.id.yesBtnAns);
        final TextView noBtn = (TextView) dialog.findViewById(R.id.noBtnAns);
        final ProgressBar prog = (ProgressBar) dialog.findViewById(R.id.progressAns);
        final ImageView doneImg = (ImageView) dialog.findViewById(R.id.doneImg);

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

                //Set the chosen answer to number one

                ArrayList<BasicAnswer> answers = theQuestion.getAnswersList();

                BasicAnswer currAns = answers.get(pos);
                BasicAnswer prevAns  = answers.get(0);

                answers.set(pos,prevAns);
                answers.set(0,currAns);

                theQuestion.setAnswersList(answers);


                final ArrayList<String> ansIds = theQuestion.getAnswersId();

                String currId = ansIds.get(pos);
                String prevId = ansIds.get(0);

                ansIds.set(pos,prevId);
                ansIds.set(0,currId);

                theQuestion.setAnswersId(ansIds);


                ParseQuery query = new ParseQuery("Questions");
                query.whereEqualTo("objectId",theQuestion.getPostId());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(java.util.List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            for (final ParseObject userData : objects) {

                                ArrayList<String> ids = (ArrayList<String>) userData.get("answers");






                                for(int i=0;i<ansIds.size();i++){
                                    ids.set(i,ansIds.get(i));
                                }


                                userData.put("answers",ids);
                                userData.put("hasAnswer",true);

                                            userData.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if(e == null){
                                                        doneImg.setVisibility(View.VISIBLE);
                                                        prog.setVisibility(View.INVISIBLE);
                                                        theQuestion.setHasAnswer(true);
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                dialog.dismiss();
                                                                notifyDataSetChanged();
                                                            }
                                                        }, 2000);



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
            public void onClick(View v) {
                dialog.dismiss();
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

    public void editAlert(final int position){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.setContentView(R.layout.editpostdialog);

        RelativeLayout flagLayout = (RelativeLayout) dialog.findViewById(R.id.editPost);

        flagLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(context, SubmitQuestion.class);

                if(position == 0) {
                    intent.putExtra("editPost", theQuestion);
                    intent.putExtra("fromActivity",2);
                }

                context.startActivity(intent);

            }
        });


        dialog.show();
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


}