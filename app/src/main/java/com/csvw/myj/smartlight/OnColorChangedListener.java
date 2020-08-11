package com.csvw.myj.smartlight;

/**
 * Created by ygz on 2017/8/19.
 */


public interface OnColorChangedListener {
    void colorChanged(int s);
    void intenseChanged(int s);
    void moodChanged(String m);
    void moodSave(String m);
}
