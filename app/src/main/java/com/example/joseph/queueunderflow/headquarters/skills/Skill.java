package com.example.joseph.queueunderflow.headquarters.skills;

import android.net.Uri;

/**
 * Created by josep on 2/7/2017.
 */
public class Skill {

    private String name;
    private Uri skillUrl;
    private boolean isSelected;

   public void  skill(){
        this.name = "";
       this.isSelected = false;

    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getSkillUrl() {
        return skillUrl;
    }

    public void setSkillUrl(Uri skillUrl) {
        this.skillUrl = skillUrl;
    }
}

