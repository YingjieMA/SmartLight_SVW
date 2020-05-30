package com.csvw.myj.smartlight;

public class SmartLight extends RgbLight {
    private String animation;

    public String getAnimation() {
        return animation;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public SmartLight(String animation) {
        this.animation = animation;
    }

    public SmartLight(int rValue, int gValue, int bValue, int intenseValue, String animation) {
        super(rValue, gValue, bValue, intenseValue);
        this.animation = animation;
    }

    public SmartLight(int id, String name, String type, Boolean state, int rValue, int gValue, int bValue, int intenseValue, String animation) {
        super(id, name, type, state, rValue, gValue, bValue, intenseValue);
        this.animation = animation;
    }

    @Override
    public String toString() {
        return "SmartLight{" +
                "animation='" + animation + '\'' +
                '}';
    }
}
