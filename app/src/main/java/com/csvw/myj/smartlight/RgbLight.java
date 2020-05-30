package com.csvw.myj.smartlight;

public class RgbLight extends Light {
    //灯rgb值
    private int rValue;
    private int gValue;
    private int bValue;
    //灯亮度
    private int intenseValue;

    public RgbLight() {
    }

    public RgbLight(int rValue, int gValue, int bValue, int intenseValue) {
        this.rValue = rValue;
        this.gValue = gValue;
        this.bValue = bValue;
        this.intenseValue = intenseValue;
    }

    public RgbLight(int id, String name, String type, Boolean state, int rValue, int gValue, int bValue, int intenseValue) {
        super(id, name, type, state);
        this.rValue = rValue;
        this.gValue = gValue;
        this.bValue = bValue;
        this.intenseValue = intenseValue;
    }

    public int getrValue() {
        return rValue;
    }

    public int getgValue() {
        return gValue;
    }

    public int getbValue() {
        return bValue;
    }

    public int getIntenseValue() {
        return intenseValue;
    }

    public void setrValue(int rValue) {
        this.rValue = rValue;
    }

    public void setgValue(int gValue) {
        this.gValue = gValue;
    }

    public void setbValue(int bValue) {
        this.bValue = bValue;
    }

    public void setIntenseValue(int intenseValue) {
        this.intenseValue = intenseValue;
    }

    @Override
    public String toString() {
        return super.toString() + "RgbLight{" +
                "rValue=" + rValue +
                ", gValue=" + gValue +
                ", bValue=" + bValue +
                ", intenseValue=" + intenseValue +
                '}';
    }
}
