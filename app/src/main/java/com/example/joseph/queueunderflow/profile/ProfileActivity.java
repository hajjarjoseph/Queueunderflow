package com.example.joseph.queueunderflow.profile;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joseph.queueunderflow.DAL.ParseTransaction;
import com.example.joseph.queueunderflow.QuestRecycler;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.basicpost.BasicPost;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.basicpost.basicquestion.imagequestion.ImageQuestion;
import com.example.joseph.queueunderflow.comments.Comment;
import com.example.joseph.queueunderflow.comments.CommentsList;
import com.example.joseph.queueunderflow.home.BasePage;
import com.example.joseph.queueunderflow.interest.InterestPage;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    private int mode;
    private Bitmap proBitmap;
    TabHost tabHost;
    ArrayList<BasicPost> items;
    ArrayList<BasicPost> items2;
    QuestRecycler mAdapter;
    QuestRecycler mAdapter2;
    private LinearLayoutManager mLinearLayoutManager;
    private LinearLayoutManager mLinearLayoutManager2;
    RecyclerView myquestlv;
    RecyclerView mysubslv;


    ProfilePage context;
    private Dialog photoDilog;

    @BindView(R.id.mypropic)
    CircularImageView mypropic;
    @BindView(R.id.bgpicture)
    ImageView bgpicture;
    @BindView(R.id.repNum)
    TextView repNum;
    @BindView(R.id.firstPost)
    ImageView firstPost;
    @BindView(R.id.userNameField)
    TextView userNameField;
    @BindView(R.id.editBg)
    ImageView editBg;
    @BindView(R.id.interestBtn)
    Button interestBtn;
    private int otherUser = 0;
    private String theUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            theUser = extras.getString("theUser");
        }

            initializeTabHoster();


        userNameField.setText(theUser);



        interestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, InterestPage.class);
                intent.putExtra("theUser",theUser);
                startActivity(intent);
            }
        });

    }

    private void initializeTabHoster() {
        tabHost = (TabHost) findViewById(R.id.tabhost);
        //Important
        tabHost.setup();

        final TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab Tag");
        final TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab Tag");


        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected


        tab1.setIndicator("Questions");
        tab1.setContent(R.id.i_layout_1);



        tab2.setIndicator("Subscriptions");
        tab2.setContent(R.id.i_layout_2);





        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);

        int wantedTabIndex = 0;
        // TextView tv = (TextView)(((LinearLayout)((LinearLayout)tabHost.getChildAt(1)).getChildAt(wantedTabIndex)).getChildAt(1));

        FrameLayout testLayout =  tabHost.getTabContentView();
        int num =  testLayout.getChildCount();



        Log.d(ProfilePage.class.getSimpleName(),"el fekra eno num is " + num );



        myquestlv = (RecyclerView) findViewById(R.id.myquestlv);
        mysubslv = (RecyclerView) findViewById(R.id.mysubslv);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager2 = new LinearLayoutManager(this);
        myquestlv.setLayoutManager(mLinearLayoutManager);
        mysubslv.setLayoutManager(mLinearLayoutManager2);

        new LoadQuests().execute();

        new LoadSubs().execute();


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if(tab1.equals(tabId)) {

                }
                if(tab2.equals(tabId)) {



                }
            }});



    }

    private class LoadQuests extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            ParseTransaction questionsTransaction = new ParseTransaction();

            questionsTransaction.setTheUser(theUser);
            questionsTransaction.getQuestions(myquestlv,3);



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml


        }
    }

    private class LoadSubs extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            items2 = new ArrayList<>();

            ParseQuery fetchUser = new ParseQuery("_User");
            fetchUser.whereEqualTo("username", theUser);

            fetchUser.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(java.util.List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        for (ParseObject userData : objects) {



                            ParseFile proImg = (ParseFile) userData.get("profilePicture");

                            if(proImg == null){

                            }else{
                                String imgUrl = proImg.getUrl();
                                Uri imgUri = Uri.parse(imgUrl);

                                Glide
                                        .with(ProfileActivity.this)
                                        .load(imgUri)
                                        .into(mypropic);
                            }






                            ParseFile bacImg = (ParseFile) userData.get("photobg");
                            if(bacImg == null){

                            }else{
                                String imgUrlBg = bacImg.getUrl();
                                Uri imgUriBg = Uri.parse(imgUrlBg);

                                Glide
                                        .with(ProfileActivity.this)
                                        .load(imgUriBg)
                                        .into(bgpicture);

                            }


                            int rep = userData.getInt("Reputation");

                            repNum.setText(String.valueOf(rep));

                            boolean firstP = userData.getBoolean("firstPost");
                            if(firstP){
                                firstPost.setVisibility(View.VISIBLE);
                            }



                            ArrayList<String> questsId = (ArrayList<String>) userData.get("subscribedPosts");

                            ParseQuery fetchQuests = new ParseQuery("Questions");
                            fetchQuests.whereContainedIn("objectId", questsId);

                            fetchQuests.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(java.util.List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        for (ParseObject userData : objects) {

                                            String title = userData.getString("title");
                                            String owner = "#";
                                            owner += userData.getString("owner");
                                            String description = userData.getString("description");
                                            Date postDate = userData.getCreatedAt();
                                            String postId = userData.getObjectId();
                                            boolean hasAnswer = userData.getBoolean("hasAnswer");
                                            boolean edited = userData.getBoolean("edited");
                                            int votes = userData.getInt("Votes");
                                            int upVotes = userData.getInt("upvotes");
                                            int downVotes = userData.getInt("downvotes");

                                            ArrayList<HashMap<String,String>> commentListArr = (ArrayList<HashMap<String,String>>) userData.get("comments");
                                            ArrayList<Comment> theList = new ArrayList<Comment>();
                                            HashMap<String, String> commentList = new HashMap<String, String>();
                                            if(commentListArr.size()>0) {

                                                for(int i=0;i<commentListArr.size();i++){
                                                    commentList = commentListArr.get(i);
                                                    Map.Entry<String,String> entry=commentList.entrySet().iterator().next();
                                                    Comment c = new Comment(entry.getKey(),entry.getValue());
                                                    theList.add(c);

                                                }
                                            }

                                            CommentsList finalCom = new CommentsList(theList);




                                            ArrayList<String> voters = (ArrayList<String>) userData.get("voters");
                                            ArrayList<String> tags = (ArrayList<String>) userData.get("tags");
                                            ArrayList<String> answersId = (ArrayList<String>) userData.get("answers");


                                            ArrayList<String> images = new ArrayList<String>();

                                            ParseFile qImage = (ParseFile) userData.get("image1");
                                            if(qImage == null){

                                                // Create BasicQuestion with no images
                                                BasicQuestion basicQuestion = new BasicQuestion(owner,title,description,postId,postDate,voters,tags,answersId);
                                                basicQuestion.setHasAnswer(hasAnswer);
                                                basicQuestion.setEdited(edited);
                                                basicQuestion.setVotes(upVotes-downVotes);
                                                basicQuestion.setCommentsList(finalCom);
                                                items2.add(basicQuestion);

                                            }else{
                                                String imageUrl = qImage.getUrl() ;//live url


                                                images.add(imageUrl);


                                                qImage = (ParseFile) userData.get("image2");

                                                if(qImage == null){
                                                    //Do nothing
                                                }else{
                                                    imageUrl = qImage.getUrl() ;//live url


                                                    images.add(imageUrl);


                                                    qImage = (ParseFile) userData.get("image3");

                                                    if(qImage == null){

                                                    }else{
                                                        imageUrl = qImage.getUrl() ;//live url

                                                        images.add(imageUrl);
                                                    }


                                                }

                                                //Create ImageQuestion
                                                ImageQuestion imageQuestion = new ImageQuestion(owner,title,description,postId,postDate,tags,answersId,images,voters);
                                                imageQuestion.setHasAnswer(hasAnswer);
                                                imageQuestion.setEdited(edited);
                                                imageQuestion.setVotes(upVotes-downVotes);
                                                imageQuestion.setCommentsList(finalCom);
                                                items2.add(imageQuestion);
                                            }







                                        }

                                        mAdapter2 = new QuestRecycler(ProfileActivity.this,items2);


                                        mysubslv.setAdapter(mAdapter2);
                                    }
                                }

                            });

                        }

                    }
                }

            });


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml


        }
    }
}
