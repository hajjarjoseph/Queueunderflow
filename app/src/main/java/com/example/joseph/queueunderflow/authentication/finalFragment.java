package com.example.joseph.queueunderflow.authentication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joseph.queueunderflow.authentication.LogIn.LogIn;
import com.example.joseph.queueunderflow.authentication.createaccount.CreateAccount;
import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.reset.ResetPage;

/**
 * A simple {@link Fragment} subclass.
 */
public class finalFragment extends Fragment {


    private TextView createAccBtn;
    private TextView signInBtn;
    private TextView resetPass;

    public finalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_final, container, false);

        createAccBtn = (TextView) view.findViewById(R.id.createAccBtn);
        signInBtn = (TextView) view.findViewById(R.id.signInBtn);
        resetPass = (TextView) view.findViewById(R.id.forgotPassBtn);

        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateAccount.class);
                startActivity(intent);
            }
        });



        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LogIn.class);
                startActivity(intent);
            }
        });



        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ResetPage.class);
                startActivity(intent);
            }
        });






        return view;
    }


}
