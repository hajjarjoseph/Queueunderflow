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

import com.bumptech.glide.Glide;
import com.example.joseph.queueunderflow.authentication.IntroPage;
import com.example.joseph.queueunderflow.authentication.askquestion.AskQuestionMain;
import com.example.joseph.queueunderflow.basicpost.BasicPost;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.cardpage.CardPage;
import com.example.joseph.queueunderflow.headquarters.MainPage;
import com.example.joseph.queueunderflow.headquarters.QuestionsList;
import com.example.joseph.queueunderflow.headquarters.queuebuilder.QueueBuilder;
import com.example.joseph.queueunderflow.headquarters.skills.Skill;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by josep on 2/19/2017.
 */
public class QuestRecycler extends RecyclerView.Adapter<QuestRecycler.PhotoHolder> {

    private ArrayList<BasicPost> items;



    private Context context;


    public QuestRecycler(QuestionsList mainActivity, ArrayList<BasicPost> items) {

        context = mainActivity;
        this.items = items;



    }


    @Override
    public QuestRecycler.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.questitem, parent, false);
        return new PhotoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final QuestRecycler.PhotoHolder holder, final int position) {



        final BasicPost item = items.get(position);
        //holder.bindPhoto(post);

        holder.titleqItem.setText(((BasicQuestion)item).getqTitle());
        holder.userqItem.setText(item.getqOwner());

        ArrayList<String> tags = ((BasicQuestion) item).getTags();

        if(!tags.isEmpty()){
            String tag = tags.get(0);

            holder.skillName1.setText(tag);

            String  skillDraw = nametoDrawable(tag);



            final int imgRess = context.getResources().getIdentifier(skillDraw, null, context.getPackageName());

            Log.d(QuestRecycler.class.getSimpleName(),"el fekra  eno : " + imgRess);

            holder.skillPic1.setImageResource(imgRess);

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
        ImageView skillPic1;
        RelativeLayout cardBox;
        TextView timeAgo;








        //3
        private static final String PHOTO_KEY = "PHOTO";

        //4
        public PhotoHolder(View v) {
            super(v);



            userqItem=(TextView) v.findViewById(R.id.userqItem);
            titleqItem=(TextView) v.findViewById(R.id.titleqItem);
            skillName1=(TextView) v.findViewById(R.id.skillNameqItem1);
            skillPic1=(ImageView) v.findViewById(R.id.skillPicqItem1);
            cardBox=(RelativeLayout) v.findViewById(R.id.cardBox);
            timeAgo = (TextView ) v.findViewById(R.id.timestamp);



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