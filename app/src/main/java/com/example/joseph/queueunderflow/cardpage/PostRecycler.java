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
import android.os.Parcelable;
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
import com.example.joseph.queueunderflow.QuestRecycler;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.alerts.OthersPostAlert;
import com.example.joseph.queueunderflow.alerts.OwnPostAlert;
import com.example.joseph.queueunderflow.basicpost.BasicPost;
import com.example.joseph.queueunderflow.basicpost.basicanswer.BasicAnswer;
import com.example.joseph.queueunderflow.basicpost.basicanswer.imageanswer.ImageAnswer;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.basicpost.basicquestion.imagequestion.ImageQuestion;
import com.example.joseph.queueunderflow.comments.Comment;
import com.example.joseph.queueunderflow.comments.CommentsList;
import com.example.joseph.queueunderflow.comments.CommentsPage;
import com.example.joseph.queueunderflow.headquarters.QuestionsList;
import com.example.joseph.queueunderflow.headquarters.skills.Skill;

import java.io.Serializable;
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
import com.example.joseph.queueunderflow.profile.ProfileActivity;
import com.example.joseph.queueunderflow.reputation.ReputationFactory;
import com.example.joseph.queueunderflow.submitpost.SubmitQuestion;
import com.example.joseph.queueunderflow.viewpager.ViewPagerAdapter;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import android.os.Handler;
import java.util.zip.InflaterInputStream;

import butterknife.BindView;

/**
 * Created by josep on 2/19/2017.
 */
public class PostRecycler extends RecyclerView.Adapter<PostRecycler.PhotoHolder>  {

    private ArrayList<BasicPost> items;

    public BasicQuestion getTheQuestion() {
        return theQuestion;
    }

    public void setTheQuestion(BasicQuestion theQuestion) {
        this.theQuestion = theQuestion;
    }

    private BasicQuestion theQuestion;
    private ArrayList<BasicAnswer> answersList;
    private ReputationFactory reputationGiver;
    private String currUser;


private RecyclerView postlv;

    private Context context;
    private  PostRecycler mAdapter;


    public PostRecycler(CardPage mainActivity,BasicQuestion theQuestion, PostRecycler mAdapter) {

        context = mainActivity;
        this.theQuestion = theQuestion;
        this.mAdapter = mAdapter;
        this.answersList = new ArrayList<>();
        this.currUser = ParseUser.getCurrentUser().getUsername();



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
                        .inflate(R.layout.newanswerslayout, parent, false);
                return new PhotoHolder(inflatedView);


            case 0:
                 inflatedView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.newdetailedquestion, parent, false);
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


            final String answerOwner = theAnswer.getqOwner().toString();
            holder.postOwner.setText(answerOwner);

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

            ParseQuery fetchUser = new ParseQuery("_User");
            fetchUser.whereEqualTo("username", theQuestion.getAnswersList().get(position-1).getqOwner().substring(1));

            fetchUser.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(java.util.List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        for (ParseObject profileData : objects) {


                            ParseFile  proImg = (ParseFile) profileData.get("profilePicture");

                            if (proImg == null) {
                                holder.profilePic.setImageResource(R.drawable.miniowl);
                            } else {
                                String imgUrl = proImg.getUrl();
                                Uri imgUri = Uri.parse(imgUrl);

                                Glide
                                        .with(context)
                                        .load(imgUri)
                                        .into(holder.profilePic);


                            }
                        }
                    }
                }
            });


            final ArrayList<String> theVoters = theQuestion.getAnswersList().get(position-1).getVoters();

            boolean alreadyVoted = false;
            for(int i=0;i<theVoters.size();i++){
                if(theVoters.get(i).equals(ParseUser.getCurrentUser().getUsername())){
                    alreadyVoted = true;
                    break;
                }
            }

            if(alreadyVoted) {
                int numUpvotes = theQuestion.getUpVotes();
                int numDownvotes = theQuestion.getDownVotes();

                int totalDownVotes = numDownvotes + 1;
                holder.upBtn.setText("UpVotes " + numUpvotes);
                holder.downBtn.setText("DownVotes " + totalDownVotes);

                holder.upBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                holder.downBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            }else{

                holder.upBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int totalVotes = theQuestion.getVotes() + 1;
                        int upVotes = theQuestion.getUpVotes();

                        int totalUpvotes = upVotes + 1;

                        holder.upBtn.setText("UpVote " + totalUpvotes);
                        theQuestion.setVotes(totalVotes);





                        reputationGiver = new ReputationFactory(theQuestion.getqOwner().substring(1),2);
                        reputationGiver.giveReputation();

                        holder.upBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });


                        ParseQuery findPost = new ParseQuery("Answers");
                        findPost.whereEqualTo("objectId",theQuestion.getAnswersList().get(position-1).getPostId());
                        findPost.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(java.util.List<ParseObject> objects, ParseException e) {

                                if (e == null) {
                                    for (ParseObject userData : objects) {


                                        userData.increment("upvotes");
                                        userData.increment("Votes");
                                       ArrayList<String> votez = (ArrayList<String>) userData.get("voters");
                                        votez.add(ParseUser.getCurrentUser().getUsername());
                                        theQuestion.getAnswersList().get(position-1).setVoters(votez);
                                        userData.add("voters",ParseUser.getCurrentUser().getUsername());
                                        userData.add("upvoters",ParseUser.getCurrentUser().getUsername());
                                        userData.saveInBackground();

                                    }
                                }
                            }
                        });
                    }
                });



                holder.downBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int totalVotes = theQuestion.getVotes() - 1;
                        int downVotes = theQuestion.getDownVotes();

                        holder.downBtn.setText("Downvotes " + downVotes +1);

                        theQuestion.setVotes(totalVotes);


                        ParseQuery findPost = new ParseQuery("Answers");
                        findPost.whereEqualTo("objectId",theQuestion.getAnswersList().get(position-1).getPostId());
                        findPost.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(java.util.List<ParseObject> objects, ParseException e) {

                                if (e == null) {
                                    for (ParseObject userData : objects) {


                                        userData.increment("downvotes");
                                        userData.increment("Votes");
                                        ArrayList<String> votez = (ArrayList<String>) userData.get("voters");
                                        votez.add(ParseUser.getCurrentUser().getUsername());
                                        theQuestion.getAnswersList().get(position-1).setVoters(votez);
                                        userData.add("voters",ParseUser.getCurrentUser().getUsername());
                                        userData.add("downvoters",ParseUser.getCurrentUser().getUsername());
                                        userData.saveInBackground();

                                    }
                                }
                            }
                        });
                    }
                });

            }



            holder.options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        if(answerOwner.equals("#" + currUser)){
                            //Own Answer
                            OwnPostAlert ownAnswerAlert = new OwnPostAlert(context,theQuestion.getAnswersList().get(position - 1),theQuestion.getPostId());
                            ownAnswerAlert.show();
                        }else{
                            OthersPostAlert otherAlert = new OthersPostAlert(context,theQuestion.getAnswersList().get(position - 1));
                            otherAlert.show();

                        }



                }
            });


            holder.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommentsPage.class);
                    intent.putExtra("commentList", (Serializable) theQuestion.getAnswersList().get(position-1).getCommentsList());
                    intent.putExtra("thePostId",theQuestion.getAnswersList().get(position-1).getPostId());
                    intent.putExtra("fromPost",1);
                    intent.putExtra("theQuestion",theQuestion);
                    context.startActivity(intent);
                }
            });

            holder.postOwner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    /*
                    ArrayList<BasicPost> newItems = new ArrayList<BasicPost>();
                    newItems.add(items.get(position));
                    */
                    intent.putExtra("theUser",answerOwner.substring(1));
                    context.startActivity(intent);
                }
            });




            //Sets Time of Post


            if(now - time <60000){
                holder.timeago.setText("now");
            }else{
                CharSequence ago =
                        DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);


                holder.timeago.setText(ago);
            }


            /*
            holder.shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("application/image");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{""});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Sharing with you an answer for a question on Owly");

                    if(theQuestion.getAnswersList().get(position-1) instanceof ImageAnswer){
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Question: " + theQuestion.getqTitle() + "\n\n Answer: " + theQuestion.getAnswersList().get(position-1).getqDescription() +  Uri.parse(((ImageAnswer) theQuestion.getAnswersList().get(position-1)).getImagesUri().get(0)) + "\n\n What do you think ?");

                    }else{
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Question: " + theQuestion.getqTitle() + "\n\n Answer: " + theQuestion.getAnswersList().get(position-1).getqDescription() + "\n\n + What do you think ?");
                    }

                    context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                }
            });


*/
            holder.setUiPageViewController();



        }else if(getItemViewType(position) == 0){
            String title = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            long time = theQuestion.getPostDate().getTime();
            long now = System.currentTimeMillis();

            final String theOwner = theQuestion.getqOwner().toString();

            ArrayList<String> tags = theQuestion.getTags();

            ParseQuery fetchUser = new ParseQuery("_User");
            fetchUser.whereEqualTo("username", theQuestion.getqOwner().substring(1));

            fetchUser.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(java.util.List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        for (ParseObject profileData : objects) {


                            ParseFile  proImg = (ParseFile) profileData.get("profilePicture");

                            if (proImg == null) {
                                holder.profilePic.setImageResource(R.drawable.miniowl);
                            } else {
                                String imgUrl = proImg.getUrl();
                                Uri imgUri = Uri.parse(imgUrl);

                                Glide
                                        .with(context)
                                        .load(imgUri)
                                        .into(holder.profilePic);


                            }
                        }
                    }
                }
            });

            if(!tags.isEmpty()) {
                String tag = tags.get(0);

                holder.skillName1.setText(tag);
                String  skillDraw = nametoDrawable(tag);

                final int imgRess = context.getResources().getIdentifier(skillDraw, null, context.getPackageName());

                Log.d(QuestRecycler.class.getSimpleName(),"el fekra  eno : " + imgRess);

                holder.skillPic1.setImageResource(imgRess);

            }



            holder.postOwner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    /*
                    ArrayList<BasicPost> newItems = new ArrayList<BasicPost>();
                    newItems.add(items.get(position));
                    */


                    intent.putExtra("theUser",theOwner.substring(1));
                    context.startActivity(intent);
                }
            });

            /*
            holder.shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("application/image");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{""});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Can you help answer this question? Owly");

                    if(theQuestion instanceof ImageQuestion){
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,(theQuestion.getqTitle()+" \n\n " + theQuestion.getqDescription() + "\n\n " + Uri.parse(((ImageQuestion) theQuestion).getImagesUri().get(0).toString())));
                    }else{
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,(theQuestion.getqTitle()+" \n\n " + theQuestion.getqDescription()));
                    }

                    context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                }
            });

            */

            holder.postOwner.setText(theOwner);

            if(theQuestion.isEdited()){
                holder.editedText.setVisibility(View.VISIBLE);
            }



            final ArrayList<String> theVoters = theQuestion.getVoters();


            boolean alreadyVoted = false;
            for(int i=0;i<theVoters.size();i++){
                if(theVoters.get(i).equals(ParseUser.getCurrentUser().getUsername())){
                    alreadyVoted = true;
                    break;
                }
            }

            if(alreadyVoted) {
                int numUpvotes = theQuestion.getUpVotes();
                int numDownvotes = theQuestion.getDownVotes();

                holder.upBtn.setText("UpVotes " + numUpvotes);
                holder.downBtn.setText("DownVotes " + numDownvotes);

                holder.upBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                holder.downBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            }else{

                holder.upBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int totalVotes = theQuestion.getVotes() + 1;
                        int upVotes = theQuestion.getUpVotes();

                        int totalUpVotes = upVotes + 1;
                        holder.upBtn.setText("UpVote " + totalUpVotes);
                        theQuestion.setVotes(totalVotes);





                        reputationGiver = new ReputationFactory(theQuestion.getqOwner().substring(1),2);
                        reputationGiver.giveReputation();

                        holder.upBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });

                        ParseQuery findPost = new ParseQuery("Questions");
                        findPost.whereEqualTo("objectId",theQuestion.getPostId());
                        findPost.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(java.util.List<ParseObject> objects, ParseException e) {

                                if (e == null) {
                                    for (ParseObject userData : objects) {


                                        userData.increment("upvotes");
                                        userData.increment("Votes");
                                        userData.add("voters",ParseUser.getCurrentUser().getUsername());
                                        userData.add("upvoters",ParseUser.getCurrentUser().getUsername());
                                        userData.saveInBackground();

                                    }
                                }
                            }
                        });
                    }
                });



                holder.downBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        int totalVotes = theQuestion.getVotes() - 1;
                        int downVotes = theQuestion.getDownVotes();

                        int totalDownVotes = downVotes + 1;

                        holder.downBtn.setText("Downvotes " + totalDownVotes);

                        theQuestion.setVotes(totalVotes);

                        ParseQuery findPost = new ParseQuery("Questions");
                        findPost.whereEqualTo("objectId",theQuestion.getPostId());
                        findPost.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(java.util.List<ParseObject> objects, ParseException e) {

                                if (e == null) {
                                    for (ParseObject userData : objects) {


                                        userData.increment("downvotes");
                                        userData.increment("Votes");
                                        userData.add("voters",ParseUser.getCurrentUser().getUsername());
                                        userData.add("downvoters",ParseUser.getCurrentUser().getUsername());
                                        userData.saveInBackground();

                                    }
                                }
                            }
                        });
                    }
                });

            }



            holder.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommentsPage.class);
                    intent.putExtra("commentList", (Serializable) theQuestion.getCommentsList());
                    intent.putExtra("thePostId",theQuestion.getPostId());
                    intent.putExtra("fromPost",0);
                    intent.putExtra("theQuestion",theQuestion);
                    context.startActivity(intent);
                }
            });


            title = theQuestion.getqTitle().toString();
            holder.postTitle.setText(title);
            holder.postDescription.setText(theQuestion.getqDescription().toString());




            holder.options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        if(theOwner.equals("#" + currUser)){
                            //Own Question
                            OwnPostAlert ownQuestionAlert = new OwnPostAlert(context,theQuestion,theQuestion.getPostId());
                            ownQuestionAlert.show();

                        }else{
                            OthersPostAlert otherAlert = new OthersPostAlert(context,theQuestion);
                            otherAlert.show();
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
        TextView votesNum;
        ViewPager intro_images;
        ImageView answerPicker;
        CircularImageView profilePic;
        Button upBtn;
        TextView downBtn;
        ImageView shareBtn;
        Button pickAnswerBtn;
        TextView commentBtn;
        ImageView options;
        LinearLayout pager_indicator;
        private int dotsCount;
        private ImageView[] dots;
        private ViewPagerAdapter mViewPagerAdapter;
        private Context contxt;
        private TextView timeago;
        private TextView editedText;
        private TextView skillName1;
        private ImageView skillPic1;

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
            editedText=(TextView) v.findViewById(R.id.editedText);
            postTitle=(TextView) v.findViewById(R.id.postTitle);
            votesNum=(TextView) v.findViewById(R.id.votesNum);
            pickAnswerBtn=(Button) v.findViewById(R.id.pickAnswerBtn);
            commentBtn=(TextView) v.findViewById(R.id.commentBtn);
            skillName1=(TextView) v.findViewById(R.id.skillNameqItem1);
            skillPic1=(ImageView) v.findViewById(R.id. skillPicqItem1);
            answerPicker=(ImageView) v.findViewById(R.id.theAnsPick);
            profilePic=(CircularImageView) v.findViewById(R.id.profilePic);
            upBtn=(Button) v.findViewById(R.id.upBtn);
            downBtn=(TextView) v.findViewById(R.id.downBtn);
            shareBtn=(ImageView) v.findViewById(R.id.shareBtn);
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


                reputationGiver = new ReputationFactory(theQuestion.getAnswersList().get(pos).getqOwner().substring(1),5);
                reputationGiver.giveReputation();

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

    public void updateComments(int fromPost, CommentsList comments,int pos){
        if(fromPost == 0){
            theQuestion.setCommentsList(comments);
        }else{
            theQuestion.getAnswersList().get(pos).setCommentsList(comments);
        }
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

