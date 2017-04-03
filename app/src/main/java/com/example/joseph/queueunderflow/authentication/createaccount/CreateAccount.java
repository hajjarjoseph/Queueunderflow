package com.example.joseph.queueunderflow.authentication.createaccount;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.joseph.queueunderflow.MainActivity;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.headquarters.MainPage;
import com.example.joseph.queueunderflow.home.BasePage;
import com.example.joseph.queueunderflow.selectiontopic.SelectTopicPage;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAccount extends AppCompatActivity {

    @BindView(R.id.nameTF)
    EditText nameTF;
    @BindView(R.id.usernameTF)
    EditText usernameTF;
    @BindView(R.id.emailTF)
    EditText emailTF;
    @BindView(R.id.passwordTF)
    EditText passwordTF;

    @BindView(R.id.signUpBtn)
    Button signUpBtn;

    private Dialog signDialog;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);



        mContext = this;




        /**********************************************Sign Up Listener *****************************************************************/

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                signAlert();
                final String fullNameText = nameTF.getText().toString();
                final String usernameText = usernameTF.getText().toString();
                final String emailText = emailTF.getText().toString();
                String password = passwordTF.getText().toString();




                // Check conditions....

                if(fullNameText.equals("") || usernameText.equals("") || emailText.equals("") || password.equals("")){

                    signDialog.dismiss();
                    createAlert("Form is not Complete.");

                }

                else if(usernameText.length() <5){

                    signDialog.dismiss();
                    createAlert("Username length must be at least 5 characters long.");


                } else if(password.length()<8){

                    signDialog.dismiss();
                    createAlert("Password length must be at least 8 characters long.");

                }else{
                    final ParseUser user = new ParseUser();
                    user.setUsername(usernameText);
                    user.setPassword(password);
                    user.setEmail(emailText);


                    ArrayList<String> emptyArr = new ArrayList<String>();
                    user.put("FullName",fullNameText);
                    user.put("firstPost",false);
                    user.put("Reputation",0);

                    user.put("skills",emptyArr);
                    user.put("subscribedPosts",emptyArr);
                    user.put("notifications",emptyArr);



                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {

                            if(e==null){




                                ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        Map<String,Object> params = new HashMap<String,Object>();
                                        String deviceToken = (String) ParseInstallation.getCurrentInstallation().get("deviceToken");
                                        params.put("deviceTok", deviceToken);
                                        params.put("theUser",usernameText);

                                        ParseCloud.callFunctionInBackground("setUserInstall", params, new FunctionCallback<Object>() {
                                            public void done(Object object, ParseException e) {
                                                if (e == null) {

                                                } else {

                                                }
                                            }
                                        });

                                        Map<String,Object> paramsMail = new HashMap<String,Object>();

                                        String userName = "";

                                        if(fullNameText.contains(" ")){
                                            userName= fullNameText.substring(0, fullNameText.indexOf(" "));

                                        }else{
                                            userName = fullNameText;
                                        }


                                        paramsMail.put("myEmail", emailText);
                                        paramsMail.put("userName", userName);


                                        ParseCloud.callFunctionInBackground("sendWelcomeMail", paramsMail, new FunctionCallback<Object>() {
                                            public void done(Object object, ParseException e) {
                                                if (e == null) {

                                                } else {

                                                }
                                            }
                                        });
                                    }
                                });




                                ParseQuery incrementUsrers = new ParseQuery("Numbers");
                                incrementUsrers.whereEqualTo("objectId","AI9X0DPPfe");
                                incrementUsrers.findInBackground(new FindCallback<ParseObject>() {
                                                                     @Override
                                                                     public void done(java.util.List<ParseObject> objects, ParseException e) {
                                                                         if (e == null) {
                                                                             for (ParseObject userData : objects) {

                                                                                 userData.increment("usersNum");
                                                                                 userData.saveInBackground();



                                                                             }
                                                                         }
                                                                     }
                                                                 });
                                Intent intent = new Intent(mContext, SelectTopicPage.class);
                                intent.putExtra("firstTime",true);
                                signDialog.dismiss();
                                startActivity(intent);
                            }else{
                                if(e.getMessage().equals("Email address format is invalid.")){

                                    signDialog.dismiss();
                                    createAlert("Email address format is invalid.");

                                }else if(e.getMessage().equals("Account already exists for this username.")){

                                    signDialog.dismiss();
                                    createAlert("Account already exists for this username.");

                                }else if(e.getMessage().equals("Account already exists for this email address.")){

                                    signDialog.dismiss();
                                    createAlert("Account already exists for this email address.");
                                }
                                Log.d(CreateAccount.class.getSimpleName(),"The problem is : " + e.getMessage());//Show error message
                            }

                        }
                    });
                }
                
            }
        });
    }
    public void createAlert(String message){
        final Dialog dialog = new Dialog(mContext);
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

    public void signAlert(){
         signDialog = new Dialog(mContext);
        signDialog.setContentView(R.layout.signdialog);
        signDialog.show();
    }
}
