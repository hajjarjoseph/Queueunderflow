package com.example.joseph.queueunderflow.home;




import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.joseph.queueunderflow.R;
import com.example.joseph.queueunderflow.utils.FragmentChangeListener;

import butterknife.BindView;

public class BasePage extends AppCompatActivity implements
        FragmentChangeListener {

    @BindView(R.id.containerFrag)
    RelativeLayout contaierFrag;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_page);



        if (savedInstanceState == null) {
            Fragment newFragment = new FeedPage();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(newFragment,newFragment.toString());
            fragmentTransaction.addToBackStack(newFragment.toString());
            fragmentTransaction.commit();


        }


    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {


            getFragmentManager().popBackStack();



        }

    }




    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerFrag, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }



}
