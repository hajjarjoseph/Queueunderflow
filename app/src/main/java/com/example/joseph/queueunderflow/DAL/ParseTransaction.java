package com.example.joseph.queueunderflow.DAL;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.example.joseph.queueunderflow.QuestRecycler;
import com.example.joseph.queueunderflow.authentication.IntroPage;
import com.example.joseph.queueunderflow.basicpost.BasicPost;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.basicpost.basicquestion.imagequestion.ImageQuestion;
import com.example.joseph.queueunderflow.comments.Comment;
import com.example.joseph.queueunderflow.comments.CommentsList;
import com.example.joseph.queueunderflow.home.BasePage;
import com.example.joseph.queueunderflow.selectiontopic.SelectTopicPage;
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

/**
 * Created by josep on 4/2/2017.
 */

public class ParseTransaction {
    private ParseObject parseObj;
    private ArrayList<BasicPost>items;
    private Context context;
    private QuestRecycler mAdapter;

    private ArrayList<String> userTopics;

    public ParseTransaction(){

    }

    public void commit(){
        parseObj.saveInBackground();
    }

    public void getQuestions(final RecyclerView widgi,int switcher){


        items = new ArrayList<>();

        final ParseQuery fetchQuests = new ParseQuery("Questions");
        fetchQuests.orderByDescending("createdAt");
        //switcher 1 then it's the questions from the feed
        //switcher 2 then it's the questions of the users
        //switcher 3 then it's the subscribed post of the user
        if(switcher == 1){
            userTopics = new ArrayList<>();

            ParseQuery fetchUser= new ParseQuery("_User");
            fetchUser.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
            fetchUser.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(java.util.List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        for (ParseObject userData : objects) {

                            userTopics = (ArrayList<String>) userData.get("skills");
                            if(userTopics.size() == 0){
                                Intent intent = new Intent(widgi.getContext(), SelectTopicPage.class);
                                widgi.getContext().startActivity(intent);
                            }
                        }

                        fetchQuests.whereContainedIn("tags",userTopics);
                        findQuestsBackground(fetchQuests,widgi);



                    }

                }

            });


        }else{
             if(switcher == 2){
                fetchQuests.whereEqualTo("owner", ParseUser.getCurrentUser().getUsername());
            }else if(switcher == 3){

            }
            findQuestsBackground(fetchQuests,widgi);
        }

    }


    private void findQuestsBackground(ParseQuery fetchQuests, final RecyclerView widgi){
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
                            items.add(basicQuestion);

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
                            items.add(imageQuestion);
                        }

                    }

                    mAdapter = new QuestRecycler(widgi.getContext(),items);
                    widgi.setAdapter(mAdapter);



                }



            }

        });

    }




}
