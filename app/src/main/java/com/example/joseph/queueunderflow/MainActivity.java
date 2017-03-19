package com.example.joseph.queueunderflow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.joseph.queueunderflow.authentication.IntroPage;
import com.example.joseph.queueunderflow.headquarters.MainPage;
import com.example.joseph.queueunderflow.headquarters.QuestionsList;
import com.example.joseph.queueunderflow.home.BasePage;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser == null){
            Intent intent = new Intent(this, IntroPage.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, BasePage.class);
            startActivity(intent);
        }
    }
}
