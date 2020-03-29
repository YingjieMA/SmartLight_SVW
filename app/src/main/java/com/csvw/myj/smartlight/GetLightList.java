package com.csvw.myj.smartlight;


import java.util.ArrayList;
/***
 * 生成灯列表，并给灯分类
 */
public class GetLightList {
    private ArrayList<Light> allList = new ArrayList<>();
    private ArrayList<Light> whiteLampList= new ArrayList<>();
    private ArrayList<Light> rgbLampList= new ArrayList<>();
    private ArrayList<Light> smartLightList= new ArrayList<>();
    public GetLightList() {
        this.init();
    }

    public void setWhiteLampList(ArrayList<Light> whiteLampList) {
        this.whiteLampList = whiteLampList;
    }

    public void setRgbLampList(ArrayList<Light> rgbLampList) {
        this.rgbLampList = rgbLampList;
    }

    private void init(){
        this.allList = allList;
        Light light1 = new Light("r1","Reading Lamp","white",false);
        Light light2 = new Light("f1","Footwell Lamp_vo.","white",true);
        Light light3 = new Light("f2","Footwell Lamp_vo.","white",true);
        Light light4 = new Light("m1","MIKO Lamp_up","white",false);
        Light light5 = new Light("m2","MIKO Lamp_down","white",false);
        Light light6 = new Light("m2","Makeup Lamp","white",false);
        Light light7 = new Light("m2","Door Handle Lamp","white",false);
        Light light8 = new Light("m2","Door opener Lamp","white",false);
        Light light9 = new Light("m2","Rear Reading Lamp","white",false);
        Light light10 = new Light("m2","Glove Box Lamp","white",false);
        RgbLight rgbLight1 = new RgbLight("rgb1","IP Decor Lamp","rgb",true,255,0,0,100);
        RgbLight rgbLight2 = new RgbLight("rgb2","Starry Lamp","rgb",false,255,255,255,100);
        RgbLight rgbLight3 = new RgbLight("rgb3","Door Decor Lamp_vo.","rgb",false,255,255,255,100);
        RgbLight rgbLight4 = new RgbLight("rgb4","Door Decor Lamp_vo.","rgb",false,255,255,255,100);
        RgbLight rgbLight5 = new RgbLight("rgb5","Door Handle Lamp","rgb",false,255,255,255,100);
        SmartLight smartLight1 = new SmartLight("sl1","Smart Light","smart",false,255,255,255,100,"sport");
        allList.add(light1);
        allList.add(light2);
        allList.add(light3);
        allList.add(light4);
        allList.add(light5);
        allList.add(light6);
        allList.add(light7);
        allList.add(light8);
        allList.add(light9);
        allList.add(light10);
        allList.add(rgbLight1);
        allList.add(rgbLight2);
        allList.add(rgbLight3);
        allList.add(rgbLight4);
        allList.add(rgbLight5);
        allList.add(smartLight1);
        //分类
        for (Light light : allList) {
            if (light.getType()=="rgb"){
                rgbLampList.add(light);
            }else if (light.getType()=="white"){
                whiteLampList.add(light);
            }else smartLightList.add(light);
        }
    }

    public void setAllList(ArrayList<Light> allList) {
        this.allList = allList;
    }

    public ArrayList<Light> getAllList() {
        return allList;
    }

    public ArrayList<Light> getWhiteLampList() {
        return whiteLampList;
    }

    public ArrayList<Light> getRgbLampList() {
        return rgbLampList;
    }

    public ArrayList<Light> getSmartLightList() {
        return smartLightList;
    }
}
