package com.csvw.myj.smartlight;

public class ColorTamplate implements Comparable<ColorTamplate> {
    private int id;
    private String name;
    //显示的颜色值
    private String appColor;
    //传输的颜色值
    private String lightColor;



    public ColorTamplate() {
    }

    public ColorTamplate(int id, String name, String appColor, String lightColor) {
        this.id = id;
        this.name = name;
        this.appColor = appColor;
        this.lightColor = lightColor;
    }

    public String getName() {
        return name;
    }


    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppColor() {
        return appColor;
    }

    public String getLightColor() {
        return lightColor;
    }

    public void setAppColor(String appColor) {
        this.appColor = appColor;
    }

    public void setLightColor(String lightColor) {
        this.lightColor = lightColor;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(ColorTamplate o) {
        return this.id-o.getId();
    }
}
