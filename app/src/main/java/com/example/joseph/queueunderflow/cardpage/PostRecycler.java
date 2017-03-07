package com.example.joseph.queueunderflow.cardpage;

/**
 * Created by josep on 2/22/2017.
 */

import android.app.Dialog;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.joseph.queueunderflow.viewpager.ViewPagerAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

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

        String title = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        long time = theQuestion.getPostDate().getTime();
        long now = System.currentTimeMillis();


        if(getItemViewType(position) == 1){

            answersList = theQuestion.getAnswersList();


            BasicAnswer theAnswer = answersList.get(position-1);


            holder.postOwner.setText(theAnswer.getqOwner().toString());


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
            holder.postOwner.setText(theQuestion.getqOwner().toString());


            title = theQuestion.getqTitle().toString();
            holder.postTitle.setText(title);
            holder.postDescription.setText(theQuestion.getqDescription().toString());



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
                dots[i].setImageDrawable(contxt.getDrawable(R.drawable.nonselecteditem_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);

                pager_indicator.addView(dots[i], params);
            }


            if(dotsCount>0) {
                dots[0].setImageDrawable(contxt.getDrawable(R.drawable.selecteditem_dot));
            }
        }



        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < dotsCount; i++) {
                dots[i].setImageDrawable(contxt.getDrawable(R.drawable.nonselecteditem_dot));
            }

            dots[position].setImageDrawable(contxt.getDrawable(R.drawable.selecteditem_dot));


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

    public String nametoDrawable(String skillName){
        String skillImgName = "drawable/";
        skillName = skillName.toLowerCase();
        skillImgName += skillName;
        skillImgName =  skillImgName.replaceAll("\\++","plus");


        return skillImgName;
    }


}