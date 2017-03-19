package com.example.joseph.queueunderflow.skills;

import android.content.Context;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.example.joseph.queueunderflow.headquarters.skills.Skill;

import java.util.ArrayList;

/**
 * Created by josep on 3/17/2017.
 */

public class SkillListRecycler extends RadioAdapter<Skill> {
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
    public void onBindViewHolder(RadioAdapter.ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);

        viewHolder.mText.setText(skills.get(i).getName());
        final Uri imageUri = skills.get(i).getSkillUrl();

        Glide.with(viewHolder.mImg.getContext()).load(imageUri).dontAnimate().into(viewHolder.mImg);

    }

    public void updateSkillsList(ArrayList<Skill>items) {
        skills.clear();
        skills.addAll(items);
        this.notifyDataSetChanged();
    }
}