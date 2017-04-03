package com.example.joseph.queueunderflow.alerts;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.joseph.queueunderflow.R;

/**
 * Created by josep on 4/3/2017.
 */

public class LoadingAlert {
    private Dialog dialog;
    private TextView alertMessage;
    private ProgressBar alertProg;


    public LoadingAlert(){

    }

    public LoadingAlert(String message, Context c){
        dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.loadingalert);



        dialog.setCancelable(false);

         alertMessage = (TextView) dialog.findViewById(R.id.alertMsg);
        alertMessage.setVisibility(View.INVISIBLE);
         alertProg = (ProgressBar) dialog.findViewById(R.id.loadingBar);

        alertMessage.setText(message);

    }

    public void show(){
        dialog.show();
    }

    public void doneLoading(){
        alertProg.setVisibility(View.INVISIBLE);
        alertMessage.setVisibility(View.VISIBLE);
        dialog.setCancelable(true);
    }
}
