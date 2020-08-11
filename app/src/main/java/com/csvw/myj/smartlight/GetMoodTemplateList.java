package com.csvw.myj.smartlight;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * @ClassName: GetMoodTemplateList
 * @Description: Storage Mood List
 * @Author: MYJ
 * @CreateDate: 2020/4/27 11:33
 */
public class GetMoodTemplateList {
    private ArrayList<MoodTemplate> allList = new ArrayList<>();
    private MoodTemplate[] moodTemplates = new MoodTemplate[6];
    public GetMoodTemplateList() {
        this.init();
    }
    private void init() {
        this.allList = allList;
        this.moodTemplates[0] = new MoodTemplate(1,"Euphoria", "c");
        this.moodTemplates[1] = new MoodTemplate(2,"Vitality","c");
        this.moodTemplates[2] = new MoodTemplate(3,"Desire","c");
        this.moodTemplates[3] = new MoodTemplate(4,"Eternity","c");
        this.moodTemplates[4] = new MoodTemplate(5,"Infinity","c");
        this.moodTemplates[5] = new MoodTemplate(6,"Freedom","c");
        for (MoodTemplate moodTemplate : moodTemplates) {
            allList.add(moodTemplate);
        }
    }

    public ArrayList<MoodTemplate> getAllList() {
        return allList;
    }
}
