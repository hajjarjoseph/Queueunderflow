package com.example.joseph.queueunderflow;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.example.joseph.queueunderflow.alerts.OthersPostAlert;
import com.example.joseph.queueunderflow.alerts.OwnPostAlert;
import com.example.joseph.queueunderflow.basicpost.BasicPost;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.cardpage.CardPage;
import com.example.joseph.queueunderflow.headquarters.skills.Skill;
import com.example.joseph.queueunderflow.home.BasePage;
import com.example.joseph.queueunderflow.profile.ProfileActivity;
import com.example.joseph.queueunderflow.reputation.ReputationFactory;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created by josep on 2/19/2017.
 */
public class QuestRecycler extends RecyclerView.Adapter<QuestRecycler.PhotoHolder> {

    private ArrayList<BasicPost> items;



    private Context context;


    private ReputationFactory reputationGiver;
    private String currUser;


    public QuestRecycler(BasePage mainActivity, ArrayList<BasicPost> items) {

        context = mainActivity;
        this.items = items;
        this.currUser = ParseUser.getCurrentUser().getUsername();


    }

    public QuestRecycler(Context mainActivity, ArrayList<BasicPost> items) {

        context = mainActivity;
        this.items = items;
        this.currUser = ParseUser.getCurrentUser().getUsername();

    }


    @Override
    public QuestRecycler.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.newfeedlayout, parent, false);
        return new PhotoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final QuestRecycler.PhotoHolder holder, final int position) {



        final BasicPost item = items.get(position);
        //holder.bindPhoto(post);

        holder.titleqItem.setText(((BasicQuestion)item).getqTitle());
        holder.userqItem.setText(item.getqOwner());

        ArrayList<String> tags = ((BasicQuestion) item).getTags();


        ParseQuery fetchUser = new ParseQuery("_User");
        fetchUser.whereEqualTo("username", item.getqOwner().substring(1));

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

/*
        if(!item.getProUrl().equals("")){
            Uri imgUri = Uri.parse(item.getProUrl());
            Glide
                    .with(context)
                    .load(imgUri)
                    .into(holder.profilePic);
        }else{
            holder.profilePic.setImageResource(R.drawable.miniowl);
        }
        */


        if(!tags.isEmpty()){
            String tag = tags.get(0);

            holder.skillName1.setText(tag);

            holder.userqItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    /*
                    ArrayList<BasicPost> newItems = new ArrayList<BasicPost>();
                    newItems.add(items.get(position));
                    */


                    intent.putExtra("theUser",item.getqOwner().substring(1));
                    context.startActivity(intent);


                }
            });
            String  skillDraw = nametoDrawable(tag);


            final ArrayList<String> theVoters = items.get(position).getVoters();

            boolean alreadyVoted = false;
            for(int i=0;i<theVoters.size();i++){
                if(theVoters.get(i).equals(ParseUser.getCurrentUser().getUsername())){
                    alreadyVoted = true;
                    break;
                }
            }

            if(alreadyVoted){

                final ArrayList<String> upVoters = items.get(position).getUpVoters();
               // final ArrayList<String> downVoters = items.get(position).getVoters();

                boolean upVoted = false;
                for(int i=0;i<upVoters.size();i++){
                    if(upVoters.get(i).equals(ParseUser.getCurrentUser().getUsername())){
                        upVoted = true;
                        break;
                    }
                }
                if(upVoted){
                    holder.upBtn.setImageResource(R.drawable.up_arrow_selected);
                }else{
                    holder.downBtn.setImageResource(R.drawable.down_arrow_selected);
                }



            }else {
                holder.upBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.upBtn.setImageResource(R.drawable.up_arrow_selected);
                        int totalVotes = items.get(position).getVotes() + 1;
                        int upVotes = items.get(position).getUpVotes() + 1;


                        items.get(position).setVotes(totalVotes);
                        items.get(position).setUpVotes(upVotes );
                        holder.votesNum.setText("" + totalVotes);


                        final ArrayList<String> upVoters = items.get(position).getVoters();

                        theVoters.add(ParseUser.getCurrentUser().getUsername());
                        upVoters.add(ParseUser.getCurrentUser().getUsername());
                        item.setVoters(theVoters);
                        item.setUpVoters(upVoters);

                        items.set(position,item);

                        reputationGiver = new ReputationFactory(items.get(position).getqOwner().substring(1),2);
                        reputationGiver.giveReputation();

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

                        ParseQuery findPost = new ParseQuery("Questions");
                        findPost.whereEqualTo("objectId",items.get(position).getPostId());
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

                        holder.downBtn.setImageResource(R.drawable.down_arrow_selected);
                        int totalVotes = items.get(position).getVotes() - 1;
                        int downVotes = items.get(position).getDownVotes() + 1;


                        items.get(position).setVotes(totalVotes);
                        items.get(position).setDownVotes(downVotes);
                        holder.votesNum.setText("" + totalVotes);



                        final ArrayList<String> downVoters = items.get(position).getDownVoters();

                        theVoters.add(ParseUser.getCurrentUser().getUsername());
                        downVoters.add(ParseUser.getCurrentUser().getUsername());
                        item.setVoters(theVoters);
                        item.setUpVoters(downVoters);

                        items.set(position,item);


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
                        ParseQuery findPost = new ParseQuery("Questions");
                        findPost.whereEqualTo("objectId",items.get(position).getPostId());
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

            final int imgRess = context.getResources().getIdentifier(skillDraw, null, context.getPackageName());

            Log.d(QuestRecycler.class.getSimpleName(),"el fekra  eno : " + imgRess);

            holder.skillPic1.setImageResource(imgRess);

            holder.votesNum.setText("" + items.get(position).getVotes());
            Long times = items.get(position).getPostDate().getTime();



            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            long time = items.get(position).getPostDate().getTime();
            long now = System.currentTimeMillis();

            if(now - time <60000){
                holder.timeAgo.setText("now");
            }else{
                CharSequence ago =
                        DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);


                holder.timeAgo.setText(ago);
            }

            holder.optionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(items.get(position).getqOwner().equals("#" + currUser)){
                        OwnPostAlert ownQuestAlert = new OwnPostAlert(context, (BasicQuestion) items.get(position),items.get(position).getPostId() );
                        ownQuestAlert.show();
                    }else {
                        OthersPostAlert otherAlert = new OthersPostAlert(context,items.get(position));
                        otherAlert.show();

                    }
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
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,((BasicQuestion)items.get(position)).getqTitle()+" \n\n " + items.get(position).getqDescription());
                    if(items.get(position) instanceof ImageQuestion){
                        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(((ImageQuestion) items.get(position)).getImagesUri().get(0)));
                    }

                    context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                }
            });

            */

            holder.cardBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CardPage.class);
                    /*
                    ArrayList<BasicPost> newItems = new ArrayList<BasicPost>();
                    newItems.add(items.get(position));
                    */


                    intent.putExtra("postDetail",items.get(position));
                     context.startActivity(intent);
                }
            });

        }




    }




    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {




        //2
        TextView userqItem;
        TextView titleqItem;
        TextView skillName1;
        TextView votesNum;
        ImageView skillPic1;
        ImageView upBtn;
        ImageView downBtn;
        ImageView optionBtn;
        CircularImageView profilePic;
        ImageView shareBtn;
        RelativeLayout cardBox;
        TextView timeAgo;
        RoundCornerProgressBar upPrg;








        //3
        private static final String PHOTO_KEY = "PHOTO";

        //4
        public PhotoHolder(View v) {
            super(v);



            userqItem=(TextView) v.findViewById(R.id.postOwner);
            titleqItem=(TextView) v.findViewById(R.id.postTitle);
            skillName1=(TextView) v.findViewById(R.id.skillNameqItem1);
            votesNum=(TextView) v.findViewById(R.id.votesNum);
            skillPic1=(ImageView) v.findViewById(R.id.skillPicqItem1);
            upBtn=(ImageView) v.findViewById(R.id.upBtn);
            downBtn=(ImageView) v.findViewById(R.id.downBtn);
            optionBtn=(ImageView) v.findViewById(R.id.postOption);
            profilePic=(CircularImageView) v.findViewById(R.id.profilePic);
            cardBox=(RelativeLayout) v.findViewById(R.id.cardBox);

            //shareBtn=(ImageView) v.findViewById(R.id.shareBtn);
            timeAgo = (TextView ) v.findViewById(R.id.timestamp);
            //upPrg = (RoundCornerProgressBar ) v.findViewById(R.id.upPrg);



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

        Log.d(QuestRecycler.class.getSimpleName(),"el ossa eno : " + skillImgName);

        return skillImgName;
    }





}