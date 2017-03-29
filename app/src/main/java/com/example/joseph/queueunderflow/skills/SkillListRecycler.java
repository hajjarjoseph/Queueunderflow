package com.example.joseph.queueunderflow.skills;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.joseph.queueunderflow.headquarters.skills.Skill;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by josep on 3/17/2017.
 */

public class SkillListRecycler extends RadioAdapter<Skill> {
    Bitmap theBitmap;
    public SkillListRecycler(Context context, ArrayList<Skill> items) {
        super(context, items);
    }

    public int getSelectedIndex(){
        return mSelectedItem;
    }

    public void setSelectedIndex(int index){
        mSelectedItem = index;
    }

    @Override
    public void onBindViewHolder(final RadioAdapter.ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);

        viewHolder.mText.setText(skills.get(i).getName());
        final Uri imageUri = skills.get(i).getSkillUrl();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    theBitmap = Glide.
                            with(mContext).
                            load(imageUri).
                            asBitmap().
                            into(-1,-1).
                            get();
                } catch (final ExecutionException e) {

                } catch (final InterruptedException e) {

                }
                return null;
            }
            @Override
            protected void onPostExecute(Void dummy) {
                if (null != theBitmap) {
                    // The full bitmap should be available here
                    Log.d(SkillListRecycler.class.getSimpleName(),"Image is not null?");
                    viewHolder.mImg.setImageBitmap(theBitmap);

                }else{
                    Log.d(SkillListRecycler.class.getSimpleName(),"Image is null?");
                }
            }
        }.execute();
       // Glide.with(viewHolder.mImg.getContext()).load("http://queueunderflow.herokuapp.com/parse/files/cmps253/31df968bce4fcae43d4fd1cc9f49466c_astronomy.png").dontAnimate().into(viewHolder.mImg);

    }

    public void updateSkillsList(ArrayList<Skill>items) {
        skills.clear();
        skills.addAll(items);
        this.notifyDataSetChanged();
    }
}