package com.example.joseph.queueunderflow.reset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.alerts.AlertMessage;
import com.example.joseph.queueunderflow.alerts.LoadingAlert;
import com.example.joseph.queueunderflow.home.BasePage;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResetPage extends AppCompatActivity {

    @BindView(R.id.emailField)
    EditText emailField;
    @BindView(R.id.resetPassBtn)
    Button resetBtn;
    @BindView(R.id.backBtn)
    TextView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_page);
        ButterKnife.bind(this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailField.equals("")){
                    AlertMessage emptyAlert = new AlertMessage("Email cannot be empty",ResetPage.this);
                    emptyAlert.show();

                }else{

                    final LoadingAlert loadingAlert = new LoadingAlert("A reset link has been sent to your email.",ResetPage.this);
                    loadingAlert.show();


                    ParseUser.requestPasswordResetInBackground(emailField.getText().toString(), new RequestPasswordResetCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){

                                loadingAlert.doneLoading();

                            }else{
                                AlertMessage errorAlert = new AlertMessage(e.getMessage(),ResetPage.this);
                                errorAlert.show();
                            }

                        }
                    });

                }
            }
        });
    }
}
