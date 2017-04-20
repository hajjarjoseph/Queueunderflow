package com.example.joseph.queueunderflow.authentication.verification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.alerts.LoadingAlert;
import com.example.joseph.queueunderflow.selectiontopic.SelectTopicPage;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerificationPage extends AppCompatActivity {

    @BindView(R.id.continueBtn)
    Button continueBtn;

    private boolean isVerfied;
    private LoadingAlert loadAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_page);
        ButterKnife.bind(this);





        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAlert = new LoadingAlert("Email is not Verified!",VerificationPage.this);
                loadAlert.show();

                ParseQuery fetchUser = new ParseQuery("_User");
                fetchUser.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(java.util.List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            for (ParseObject userData : objects) {
                                    isVerfied = userData.getBoolean("emailVerified");
                            }

                            if(isVerfied){
                                Intent intent = new Intent(VerificationPage.this, SelectTopicPage.class);
                                intent.putExtra("firstTime",true);

                                startActivity(intent);

                            }else{

                                loadAlert.doneLoading();

                            }

                        }
                    }
                });

            }
        });


    }
}
