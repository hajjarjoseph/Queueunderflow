package com.example.joseph.queueunderflow.alerts;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.joseph.queueunderflow.R;

/**
 * Created by josep on 4/4/2017.
 */

public class PhotoSelectionAlert {
    private Dialog photoDilog;
    private Context c;

    final int PICK_PHOTO_FROM_GALLERY = 1;
    final int PICK_PHOTO_FROM_CAMERA = 2;

    public PhotoSelectionAlert(Context context){
        this.c = context;
        photoDilog = new Dialog(c);
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

            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDilog.dismiss();
            }
        });
    }
}
