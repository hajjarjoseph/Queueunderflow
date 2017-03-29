package com.example.joseph.queueunderflow.basicpost.basicquestion.imagequestion;

import com.example.joseph.queueunderflow.basicpost.basicanswer.BasicAnswer;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by josep on 2/28/2017.
 */
public class ImageQuestion extends BasicQuestion implements Serializable {

    private ArrayList<String> imagesUri;

    public ImageQuestion(String qOwner, String qTitle, String qDescription, String postId, Date postDate, ArrayList<String> tags,ArrayList<String> answersId, ArrayList<String>imagesUri){
        super(qOwner,qTitle,qDescription,postId,postDate,tags,answersId);
        this.imagesUri = new ArrayList<>();
        for(int i=0;i<imagesUri.size();i++){
            this.imagesUri.add(imagesUri.get(i));
        }
    }

    public ImageQuestion(String qOwner, String qTitle, String qDescription, String postId, Date postDate, ArrayList<String> tags,ArrayList<String> answersId, ArrayList<String>imagesUri,ArrayList<String>voters){
        super(qOwner,qTitle,qDescription,postId,postDate,voters,tags,answersId);
        this.imagesUri = new ArrayList<>();
        for(int i=0;i<imagesUri.size();i++){
            this.imagesUri.add(imagesUri.get(i));
        }
    }

    public ArrayList<String> getImagesUri() {
        return imagesUri;
    }

    public void setImagesUri(ArrayList<String> imagesUri) {
        this.imagesUri = imagesUri;
    }
}
