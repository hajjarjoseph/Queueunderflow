package com.example.joseph.queueunderflow.submitpost;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.basicpost.basicquestion.BasicQuestion;
import com.example.joseph.queueunderflow.basicpost.basicquestion.imagequestion.ImageQuestion;
import com.example.joseph.queueunderflow.cardpage.CardPage;
import com.example.joseph.queueunderflow.headquarters.QuestionsList;
import com.example.joseph.queueunderflow.headquarters.skills.Skill;
import com.example.joseph.queueunderflow.home.BasePage;
import com.example.joseph.queueunderflow.skills.SkillListRecycler;
import com.example.joseph.queueunderflow.skills.SuggestSkills;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SubmitQuestion extends AppCompatActivity {

    final int PICK_PHOTO_FROM_GALLERY = 1;
    final int PICK_PHOTO_FROM_CAMERA = 2;

@BindView(R.id.addPhoto)
    ImageButton addPhoto;
    @BindView(R.id.photoContainer)
    LinearLayout photoContainer;
    @BindView(R.id.titlePick)
    EditText titlePick;
    @BindView(R.id.descriptionPick)
    EditText descriptionPick;
    @BindView(R.id.postBtn)
    Button postBtn;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.img4)
    ImageView img4;

    @BindView(R.id.cancelImg1)
    TextView cancelImg1;

    @BindView(R.id.cancelImg2)
    TextView cancelImg2;

    @BindView(R.id.cancelImg3)
    TextView cancelImg3;

    @BindView(R.id.cancelImg4)
    TextView cancelImg4;

    private Dialog photoDilog;
    private String tag;

    private  ProgressBar loadingBar;
    private  ImageView doneLoading;

private BasicQuestion question;
    Bitmap bmp1,bmp2,bmp3,bmp4;
    ArrayList<Bitmap> bmpList = new ArrayList<>();

    private int imgStatus = -1;
    private BasicQuestion editedPost;
    private BasicQuestion theQuestion;

    private int fromActivity;
    // 1 is posting question
    // 2 is edting post
    // 3 is posting n answer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_question);
        ButterKnife.bind(this);

        bmpList.add(bmp1);
        bmpList.add(bmp2);
        bmpList.add(bmp3);
        bmpList.add(bmp4);

        Bundle extras = getIntent().getExtras();
        fromActivity = extras.getInt("fromActivity");

        if (extras != null) {

            if(fromActivity == 1) {
                tag = extras.getString("skill");
            }else if(fromActivity == 2){
               editedPost =  (BasicQuestion) extras.getSerializable("editPost");
                titlePick.setText(editedPost.getqTitle());
                descriptionPick.setText(editedPost.getqDescription());
                ArrayList<String> urlList = ((ImageQuestion) editedPost).getImagesUri();
                ArrayList<Uri>uriList = new ArrayList<>();
                for(int i=0;i<urlList.size();i++){
                    Uri imageUri = Uri.parse(urlList.get(i).toString());
                    uriList.add(imageUri);
                }

                if(editedPost instanceof ImageQuestion){
                    postBtn.setText("Edit");
                    imgStatus = ((ImageQuestion)editedPost).getImagesUri().size() - 1;
                    if(imgStatus == 0){
                        photoContainer.setVisibility(View.VISIBLE);
                        Glide.with(this)
                                .load(uriList.get(0))
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        // you can do something with loaded bitmap here

                                        // .....

                                      bmp1 = resource;
                                        img1.setImageBitmap(bmp1);
                                    }
                                });
                    }else if(imgStatus == 1){
                        photoContainer.setVisibility(View.VISIBLE);
                        Glide.with(this)
                                .load(uriList.get(0))
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        // you can do something with loaded bitmap here

                                        // .....

                                        bmp1 = resource;
                                        img1.setImageBitmap(bmp1);
                                    }
                                });

                        Glide.with(this)
                                .load(uriList.get(1))
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        // you can do something with loaded bitmap here

                                        // .....

                                        bmp2 = resource;
                                        img2.setImageBitmap(bmp2);
                                    }
                                });
                    }else if(imgStatus == 2){
                        photoContainer.setVisibility(View.VISIBLE);
                        Glide.with(this)
                                .load(uriList.get(0))
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        // you can do something with loaded bitmap here

                                        // .....

                                        bmp1 = resource;
                                        img1.setImageBitmap(bmp1);
                                    }
                                });

                        Glide.with(this)
                                .load(uriList.get(1))
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        // you can do something with loaded bitmap here

                                        // .....

                                        bmp2 = resource;
                                        img2.setImageBitmap(bmp2);
                                    }
                                });

                        Glide.with(this)
                                .load(uriList.get(2))
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        // you can do something with loaded bitmap here

                                        // .....

                                        bmp3 = resource;
                                        img3.setImageBitmap(bmp3);
                                    }
                                });
                    }



                }
            }else if(fromActivity == 3){
                theQuestion =  (BasicQuestion) extras.getSerializable("theQuestion");
                titlePick.setText("Answer for: " + theQuestion.getqTitle());
                titlePick.setFocusable(false);
                postBtn.setText("Answer");
            }

        }


        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgStatus < 2) {
                    createPhotoSelection();
                }else{
                    createAlert("Maximum Photos Reached! 3/3");
                }
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(fromActivity == 1){
                    if(titlePick.getText().toString().isEmpty()){
                        createAlert("Title Field cannot be empty!");
                    }else if(descriptionPick.getText().toString().isEmpty()){
                        createAlert("Description Field cannot be empty!");
                    }else{
                        createLoading();
                        String title = titlePick.getText().toString();
                        String description = descriptionPick.getText().toString();
                        ArrayList<String> emptyStr = new ArrayList<String>();
                        ArrayList<String> skillList = new ArrayList<String>();
                        skillList.add(tag);

                        ParseObject post = new ParseObject("Questions");
                        post.put("title",title);
                        post.put("description",description);
                        post.put("owner", ParseUser.getCurrentUser().getUsername());

                        post.put("answers",emptyStr);
                        post.put("hasAnswer",false);
                        post.put("flagged",false);
                        post.put("tags",skillList);

                        bindImgs(post);

                        post.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null){
                                    loadingBar.setVisibility(View.INVISIBLE);
                                    doneLoading.setVisibility(View.VISIBLE);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(SubmitQuestion.this, BasePage.class);
                                            startActivity(intent);

                                        }
                                    }, 2000);

                                }else {
                                    createAlert(e.getMessage());
                                }
                            }
                        });
                    }

                }else if(fromActivity == 2){
                    if(titlePick.getText().toString().isEmpty()){
                        createAlert("Title Field cannot be empty!");
                    }else if(descriptionPick.getText().toString().isEmpty()){
                        createAlert("Description Field cannot be empty!");
                    }else{
                        createLoading();
                        final String title = titlePick.getText().toString();
                        final String description = descriptionPick.getText().toString();
                       // ArrayList<String> emptyStr = new ArrayList<String>();
                        ArrayList<String> skillList = new ArrayList<String>();
                        skillList.add(tag);

                        ParseQuery findPost = new ParseQuery("Questions");
                        findPost.whereEqualTo("objectId",editedPost.getPostId());
                        findPost.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(java.util.List<ParseObject> objects, ParseException e) {

                                if(e == null){
                                    for(ParseObject userData:objects ){

                                        userData.put("title",title);
                                        userData.put("description",description);

                                        bindImgs(userData);

                                        userData.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if(e == null){
                                                    loadingBar.setVisibility(View.INVISIBLE);
                                                    doneLoading.setVisibility(View.VISIBLE);
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Intent intent = new Intent(SubmitQuestion.this, BasePage.class);
                                                            startActivity(intent);

                                                        }
                                                    }, 2000);

                                                }else {
                                                    createAlert(e.getMessage());
                                                }
                                            }
                                        });



                                    }


                                    }

                                }

                            });


                        }
                }else if(fromActivity == 3){
                     if(descriptionPick.getText().toString().isEmpty()){
                        createAlert("Description Field cannot be empty!");
                    }else{
                         createLoading();
                         final ParseObject newAnswer = new ParseObject("Answers");
                         newAnswer.put("owner",ParseUser.getCurrentUser().getUsername());
                         newAnswer.put("Description",descriptionPick.getText().toString());
                         newAnswer.put("posterid",theQuestion.getPostId());
                         bindImgs(newAnswer);



                         newAnswer.saveInBackground(new SaveCallback() {
                             @Override
                             public void done(ParseException e) {
                                 if(e == null){
                                     ParseQuery findQuestion = new ParseQuery("Questions");
                                     findQuestion.whereEqualTo("objectId",theQuestion.getPostId());
                                     findQuestion.findInBackground(new FindCallback<ParseObject>() {
                                         @Override
                                         public void done(java.util.List<ParseObject> objects, ParseException e) {

                                             if(e == null){
                                                 for(ParseObject userData:objects ){

                                                     ArrayList<String> answersList = (ArrayList<String>) userData.get("answers");
                                                     answersList.add(newAnswer.getObjectId());
                                                     theQuestion.setAnswersId(answersList);
                                                     userData.put("answers",answersList);



                                                     userData.saveInBackground(new SaveCallback() {
                                                         @Override
                                                         public void done(ParseException e) {
                                                             if(e == null){
                                                                 loadingBar.setVisibility(View.INVISIBLE);
                                                                 doneLoading.setVisibility(View.VISIBLE);
                                                                 new Handler().postDelayed(new Runnable() {
                                                                     @Override
                                                                     public void run() {
                                                                         Intent intent = new Intent(SubmitQuestion.this, CardPage.class);
                                                                         intent.putExtra("postDetail",theQuestion);
                                                                         startActivity(intent);

                                                                     }
                                                                 }, 2000);

                                                             }else {
                                                                 createAlert(e.getMessage());
                                                             }
                                                         }
                                                     });



                                                 }


                                             }

                                         }

                                     });
                                 }else{
                                     createAlert(e.getMessage());
                                 }
                             }
                         });


                     }
                }


            }
        });




    }

    public void createAlert(String message){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.customdialog);
        dialog.setCancelable(false);

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

    public void createLoading(){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.postingloading);
        dialog.setCancelable(false);

         loadingBar = (ProgressBar) dialog.findViewById(R.id.loadingBar);


        loadingBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#32BEA6"), PorterDuff.Mode.SRC_IN);

         doneLoading = (ImageView) dialog.findViewById(R.id.doneLoading);




        dialog.show();
    }

    public void createPhotoSelection(){
        photoDilog = new Dialog(this);
        photoDilog.setCancelable(false);
        photoDilog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        photoDilog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        photoDilog.setContentView(R.layout.pickphotodialog);


        Button cancelBtn = (Button) photoDilog.findViewById(R.id.cancelBtn);
        Button galleryBtn = (Button) photoDilog.findViewById(R.id.galleryBtn);
        Button cameraBtn = (Button) photoDilog.findViewById(R.id.cameraBtn);

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_PHOTO_FROM_GALLERY);
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, PICK_PHOTO_FROM_CAMERA);
            }
        });

           cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDilog.dismiss();
            }
        });
        photoDilog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == PICK_PHOTO_FROM_GALLERY || requestCode == PICK_PHOTO_FROM_CAMERA) && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }

            if(requestCode == PICK_PHOTO_FROM_CAMERA){
                if(imgStatus == -1){
                    bmp1= (Bitmap) data.getExtras().get("data");
                    img1.setImageBitmap(bmp1);

                    bmpList.set(0,bmp1);
                    cancelImg1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            img1.setImageResource(0);
                        }
                    });

                    photoContainer.setVisibility(View.VISIBLE);
                    photoDilog.dismiss();
                    imgStatus = imgStatus + 1;
                }else if(imgStatus == 0){
                    bmp2= (Bitmap) data.getExtras().get("data");
                    img2.setImageBitmap(bmp2);
                    bmpList.set(1,bmp2);

                    imgStatus = imgStatus + 1;
                    cancelImg2.setVisibility(View.VISIBLE);
                    photoDilog.dismiss();
                }else if(imgStatus == 1){
                    bmp3= (Bitmap) data.getExtras().get("data");
                    img3.setImageBitmap(bmp3);
                    bmpList.set(2,bmp3);
                    cancelImg3.setVisibility(View.VISIBLE);
                    imgStatus = imgStatus + 1;
                    photoDilog.dismiss();
                }else if(imgStatus == 2){
                    bmp4= (Bitmap) data.getExtras().get("data");
                    img4.setImageBitmap(bmp4);
                    bmpList.set(3,bmp4);
                    cancelImg4.setVisibility(View.VISIBLE);
                    imgStatus = imgStatus + 1;
                    photoDilog.dismiss();
                }

            }else{
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());

                    if(imgStatus == -1){
                        bmp1 = BitmapFactory.decodeStream(inputStream);
                        img1.setImageBitmap(bmp1);
                        img1.setVisibility(View.VISIBLE);
                        bmpList.set(0,bmp1);
                        imgStatus = imgStatus + 1;
                        cancelImg1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                orderImg(0);
                            }
                        });
                        photoContainer.setVisibility(View.VISIBLE);
                        photoDilog.dismiss();

                    }else if(imgStatus == 0){
                        bmp2 = BitmapFactory.decodeStream(inputStream);
                        img2.setImageBitmap(bmp2);
                        img2.setVisibility(View.VISIBLE);
                        bmpList.set(1,bmp2);
                        cancelImg2.setVisibility(View.VISIBLE);
                        photoDilog.dismiss();
                        imgStatus = imgStatus + 1;

                        cancelImg2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                orderImg(1);
                            }
                        });
                    }else if(imgStatus == 1){
                        bmp3 = BitmapFactory.decodeStream(inputStream);
                        img3.setImageBitmap(bmp3);

                        cancelImg3.setVisibility(View.VISIBLE);
                        img3.setVisibility(View.VISIBLE);
                        photoDilog.dismiss();
                        imgStatus = imgStatus + 1;
                        cancelImg3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                orderImg(2);
                            }
                        });
                    }else if(imgStatus == 2){
                        bmp4 = BitmapFactory.decodeStream(inputStream);
                        img4.setImageBitmap(bmp4);
                        bmpList.set(3,bmp4);
                        cancelImg4.setVisibility(View.VISIBLE);
                        img4.setVisibility(View.VISIBLE);
                        photoDilog.dismiss();
                        imgStatus = imgStatus + 1;
                        cancelImg4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                orderImg(3);
                            }
                        });
                    }



                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }


    public void orderImg(int pickImg){
        int currIndex = imgStatus ;

        if(pickImg == currIndex){
            if(pickImg == 0){
                img1.setImageResource(0);
                photoContainer.setVisibility(View.GONE);
                imgStatus = imgStatus - 1;
            }else if (pickImg == 1){
                img2.setImageResource(0);
                cancelImg2.setVisibility(View.GONE);
                imgStatus = imgStatus - 1;
            }else if (pickImg == 2){
                img3.setImageBitmap(null);
                cancelImg3.setVisibility(View.GONE);
                imgStatus = imgStatus - 1;
            }else if (pickImg == 3){
                img4.setImageBitmap(null);
                cancelImg4.setVisibility(View.GONE);
                imgStatus = imgStatus - 1;
            }
        }else if(pickImg < currIndex){
            if(currIndex == 1){
                bmp1 =  ((BitmapDrawable)img2.getDrawable()).getBitmap();
                img1.setImageDrawable(img2.getDrawable());
                img2.setVisibility(View.GONE);
                cancelImg2.setVisibility(View.GONE);
                imgStatus = imgStatus - 1;
            }else if(currIndex == 2){
                if(pickImg == 0){

                   bmp1 =  ((BitmapDrawable)img2.getDrawable()).getBitmap();
                    bmp2 = ((BitmapDrawable)img3.getDrawable()).getBitmap();

                    img1.setImageDrawable(img2.getDrawable());
                    img2.setImageDrawable(img3.getDrawable());
                    img3.setVisibility(View.GONE);
                    cancelImg3.setVisibility(View.GONE);
                    imgStatus = imgStatus - 1;
                }else if(pickImg == 1){
                    bmp2 = ((BitmapDrawable)img3.getDrawable()).getBitmap();

                    img2.setImageDrawable(img3.getDrawable());
                    img3.setVisibility(View.GONE);
                    cancelImg3.setVisibility(View.GONE);
                    imgStatus = imgStatus - 1;
                }
            }
        }

    }

    public ParseObject bindImgs(ParseObject post){
        if(imgStatus > -1){
            if(imgStatus == 0){


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                bmp1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();

                // Create the ParseFile
                ParseFile file = new ParseFile("androidbegin.png", image);
                // Upload the image into Parse Cloud
                file.saveInBackground();
                post.put("image1",file);


            }else if(imgStatus == 1){

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                bmp1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();

                // Create the ParseFile
                ParseFile file = new ParseFile("androidbegin.png", image);
                // Upload the image into Parse Cloud

                post.put("image1",file);


                ByteArrayOutputStream stream2 = new ByteArrayOutputStream();


                bmp2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                byte[] image2 = stream2.toByteArray();

                // Create the ParseFile
                ParseFile file2 = new ParseFile("parsos.png", image2);
                // Upload the image into Parse Cloud

                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){

                        }else{
                            createAlert(e.getMessage());
                        }
                    }
                });

                file2.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){

                        }else{
                            createAlert(e.getMessage());
                        }
                    }
                });
                post.put("image2",file2);
            }else if(imgStatus == 2){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                bmp1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();

                // Create the ParseFile
                ParseFile file = new ParseFile("androidbegin.png", image);
                // Upload the image into Parse Cloud
                file.saveInBackground();
                post.put("image1",file);

                ByteArrayOutputStream stream2 = new ByteArrayOutputStream();

                bmp2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                byte[] image2 = stream2.toByteArray();

                // Create the ParseFile
                ParseFile file2 = new ParseFile("androidbegin2.png", image2);
                // Upload the image into Parse Cloud
                file2.saveInBackground();
                post.put("image2",file2);


                ByteArrayOutputStream stream3 = new ByteArrayOutputStream();

                bmp3.compress(Bitmap.CompressFormat.PNG, 100, stream3);
                byte[] image3 = stream3.toByteArray();

                // Create the ParseFile
                ParseFile file3 = new ParseFile("androidbegin3.png", image3);
                // Upload the image into Parse Cloud
                file3.saveInBackground();
                post.put("image3",file3);
            }
        }
        return post;
    }


}
