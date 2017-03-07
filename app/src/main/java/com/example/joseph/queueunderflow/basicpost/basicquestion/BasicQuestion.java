package com.example.joseph.queueunderflow.basicpost.basicquestion;

import com.example.joseph.queueunderflow.basicpost.BasicPost;
import com.example.joseph.queueunderflow.basicpost.basicanswer.BasicAnswer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by josep on 2/28/2017.
 */
public class BasicQuestion extends BasicPost implements Serializable {



    private String qTitle;

    private ArrayList<String> tags;

    private ArrayList<BasicAnswer> answersList;


    private ArrayList<String> answersId;

    public BasicQuestion(String qOwner, String qTitle, String qDescription, String postId, Date postDate,ArrayList<String>tags,ArrayList<String>answersId){
        super(qOwner,qDescription,postId,postDate);
        this.qTitle = qTitle;
        this.tags = new ArrayList<>();
        for(int i=0;i<tags.size();i++){
            this.tags.add(tags.get(i));
        }
        this.answersId = new ArrayList<>();
        for(int i=0;i<answersId.size();i++){
            this.answersId.add(answersId.get(i));
        }
        this.answersList = new ArrayList<>();


    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getqTitle() {
        return qTitle;
    }

    public void setqTitle(String qTitle) {
        this.qTitle = qTitle;
    }

    public ArrayList<BasicAnswer> getAnswersList() {
        return answersList;
    }

    public void setAnswersList(ArrayList<BasicAnswer> answersList) {
        this.answersList = answersList;
    }

    public ArrayList<String> getAnswersId() {
        return answersId;
    }


    public void setAnswersId(ArrayList<String> answersId) {
        this.answersId = answersId;
    }


}
