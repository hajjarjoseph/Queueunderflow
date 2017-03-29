package com.example.joseph.queueunderflow.authentication.LogIn;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joseph.queueunderflow.MainActivity;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.headquarters.MainPage;
import com.example.joseph.queueunderflow.headquarters.QuestionsList;
import com.example.joseph.queueunderflow.home.BasePage;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogIn extends AppCompatActivity {

    @BindView(R.id.userTF)
    EditText userTF;
    @BindView(R.id.passTF)
    EditText passTF;
    @BindView(R.id.loginBtn)
    Button loginBtn;
    @BindView(R.id.cancelButton)
    TextView cancelBtn;
    @BindView(R.id.loginProgress)
    ProgressBar loginProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);

        userTF.setHintTextColor(Color.WHITE);
        passTF.setHintTextColor(Color.WHITE);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usernametxt = userTF.getText().toString();
                String password = passTF.getText().toString();

                loginBtn.setVisibility(View.INVISIBLE);
                loginProgress.setVisibility(View.VISIBLE);
                if (usernametxt.equals("") || password.equals("")) {

                    createAlert("Form is not Complete.");

                    loginBtn.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);


                }else{

                    ParseUser.logInInBackground(usernametxt, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                // If user exist and authenticated, send user to Welcome.class
                                Intent intent = new Intent(
                                        LogIn.this,
                                        BasePage.class);
                                startActivity(intent);
                                finish();
                            } else {

                                if(e.getMessage().equals("Invalid username/password.")){
                                    createAlert("Invalid username/password.");
                                    loginBtn.setVisibility(View.VISIBLE);
                                    loginProgress.setVisibility(View.INVISIBLE);

                                }else{
                                    createAlert("Unknown Error occured. Please verify your internet connection and try again.");
                                    loginBtn.setVisibility(View.VISIBLE);
                                    loginProgress.setVisibility(View.INVISIBLE);

                                }


                            }

                        }
                    });



                }
            }
        });






    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public void createAlert(String message){
        final Dialog dialog = new Dialog(this);
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




}
