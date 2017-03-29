package com.example.joseph.queueunderflow.comments;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by josep on 3/27/2017.
 */

public class CommentsList implements Serializable{
    public ArrayList<Comment> getCommentsL() {
        return commentsL;
    }

    public void setCommentsL(ArrayList<Comment> commentsL) {
        this.commentsL = commentsL;
    }

    private ArrayList<Comment> commentsL;

    public CommentsList(){
        commentsL = new ArrayList<>();
    }

    public CommentsList(ArrayList<Comment> commentsL){
        this.commentsL = new ArrayList<>();
        for(int i=0;i<commentsL.size();i++){
            this.commentsL.add(commentsL.get(i));
        }
    }

    public void addComment(Comment c){
        this.commentsL.add(c);
    }
}
