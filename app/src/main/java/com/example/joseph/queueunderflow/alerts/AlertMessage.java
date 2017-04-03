package com.example.joseph.queueunderflow.alerts;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.joseph.queueunderflow.R;

/**
 * Created by josep on 4/3/2017.
 */

public class AlertMessage {
    private Dialog dialog;
    public AlertMessage(){

    }



    public  AlertMessage(String message, Context c){
         dialog = new Dialog(c);
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

    }

    public void show(){
        dialog.show();
    }
}
