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
    private Light light1;
    private Light light2;
    private Light light3;
    private Light light4;
    private Light light5;
    private Light light6;
    private Light light7;
    private Light light8;
    private Light light9;
    private Light light10;
    private RgbLight rgbLight1;
    private RgbLight rgbLight2;
    private RgbLight rgbLight3;
    private RgbLight rgbLight4;
    private RgbLight rgbLight5;
    private RgbLight rgbLight6;
    private RgbLight rgbLight7;
    private RgbLight rgbLight8;
    private RgbLight rgbLight9;
    private RgbLight rgbLight10;
    private RgbLight rgbLight11;
    private RgbLight rgbLight12;
    private RgbLight rgbLight13;
    private RgbLight rgbLight14;
    private RgbLight rgbLight15;
    private RgbLight rgbLight16;
    private RgbLight rgbLight17;
    private RgbLight rgbLight18;
    private RgbLight rgbLight19;
    private RgbLight rgbLight20;


    private SmartLight smartLight1;

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
        light1 = new Light(101,"Reading Lamp","white",false);
        light2 = new Light(102,"Footwell Lamp_vo.","white",true);
        light3 = new Light(103,"Footwell Lamp_hi.","white",true);
        light4 = new Light(104,"MIKO Lamp_up","white",false);
        light5 = new Light(105,"MIKO Lamp_down","white",false);
        light6 = new Light(106,"Makeup Lamp","white",false);
        light7 = new Light(107,"Door Handle Lamp","white",false);
        light8 = new Light(108,"Door Opener Lamp","white",false);
        light9 = new Light(109,"Rear Reading Lamp","white",false);
        light10 = new Light(110,"Glove Box Lamp","white",false);
        rgbLight1 = new RgbLight(1,"IP Decor Lamp","rgb",true,255,0,0,100);
        rgbLight2 = new RgbLight(2,"Starry Lamp","rgb",false,255,255,255,100);
        rgbLight3 = new RgbLight(3,"Door Decor Lamp_vo.","rgb",false,255,255,255,100);
        rgbLight4 = new RgbLight(4,"Door Decor Lamp_hi.","rgb",false,255,255,255,100);
        rgbLight5 = new RgbLight(5,"Door Handle Lamp","rgb",false,255,255,255,100);
        rgbLight6 = new RgbLight(6,"Door Handle Lamp","rgb",false,255,255,255,100);
        rgbLight7 = new RgbLight(7,"Door Handle Lamp","rgb",false,255,255,255,100);
        rgbLight8 = new RgbLight(8,"Door Handle Lamp","rgb",false,255,255,255,100);
        rgbLight9 = new RgbLight(9,"Door Handle Lamp","rgb",false,255,255,255,100);
        rgbLight10 = new RgbLight(10,"Door Handle Lamp","rgb",false,255,255,255,100);
        rgbLight11 = new RgbLight(11,"Door Handle Lamp","rgb",false,255,255,255,100);
        rgbLight12 = new RgbLight(12,"Door Handle Lamp","rgb",false,255,255,255,100);
        rgbLight13 = new RgbLight(13,"Door Handle Lamp","rgb",false,255,255,255,100);
        rgbLight14 = new RgbLight(14,"Door Handle Lamp","rgb",false,255,255,255,100);
        rgbLight15 = new RgbLight(15,"Door Handle Lamp","rgb",false,255,255,255,100);
        rgbLight16 = new RgbLight(16,"Door Handle Lamp","rgb",false,255,255,255,100);
        rgbLight17 = new RgbLight(17,"Door Handle Lamp","rgb",false,255,255,255,100);rgbLight5 = new RgbLight(5,"Door Handle Lamp","rgb",false,255,255,255,100);
        rgbLight18 = new RgbLight(18,"Door Handle Lamp","rgb",false,255,255,255,100);
        rgbLight19 = new RgbLight(19,"Door Handle Lamp","rgb",false,255,255,255,100);
        rgbLight20 = new RgbLight(20,"Door Handle Lamp","rgb",false,255,255,255,100);

        smartLight1 = new SmartLight(201,"Smart Light","smart",false,255,255,255,100,"sport");
        allList.add(light1);
        allList.add(light8);
        allList.add(light2);
        allList.add(light3);
        allList.add(light4);
        allList.add(light5);
        allList.add(light6);
        allList.add(light7);
        allList.add(light9);
        allList.add(light10);
        allList.add(rgbLight1);
        allList.add(rgbLight3);
        allList.add(rgbLight4);
        allList.add(rgbLight2);
        allList.add(rgbLight5);
        allList.add(rgbLight6);
        allList.add(rgbLight7);
        allList.add(rgbLight8);
        allList.add(rgbLight9);
        allList.add(rgbLight10);
        allList.add(rgbLight11);
        allList.add(rgbLight12);
        allList.add(rgbLight13);
        allList.add(rgbLight14);
        allList.add(rgbLight15);
        allList.add(rgbLight16);
        allList.add(rgbLight17);
        allList.add(rgbLight18);
        allList.add(rgbLight19);
        allList.add(rgbLight20);
        allList.add(smartLight1);
        //分类
        for (Light light : allList) {
            if (light.getType()=="rgb"){
                rgbLampList.add((RgbLight) light);
            }else if (light.getType()=="white"){
                whiteLampList.add(light);
            }else smartLightList.add(light);
        }
    }

    public void setAllList(ArrayList<Light> allList) {
        this.allList = allList;
    }

    /**
     * 将数组转为灯头属性
     * @param id
     * @param state
     * @param online
     * @param diagose
     * @param r
     * @param g
     * @param b
     * @param i
     */
    public void setByte2LightList(int id,int state,int online, int diagose,int r,int g, int b, int i){
    }

    /**
     * 将LightList转为二维数组
     * @return
     */
    public byte[][] setLightList2Byte(){
        byte[][] attrs = new byte[20][8];
        for (Light light : rgbLampList) {
            RgbLight rgbLight = (RgbLight) light;
            attrs[rgbLight.getId()-1][0] = (byte) rgbLight.getId();
            if (rgbLight.getState()){
                attrs[rgbLight.getId()-1][1] = (byte) 0xff;
            }else{attrs[rgbLight.getId()-1][1] = (byte) 0x00;}
            if (rgbLight.getOnline()){attrs[rgbLight.getId()-1][2]= (byte) 0xff;}else {attrs[rgbLight.getId()-1][2] = (byte) 0x00;}
            if (rgbLight.getDiagnose()){attrs[rgbLight.getId()-1][3]= (byte) 0xff;}else {attrs[rgbLight.getId()-1][3] = (byte) 0x00;}
            attrs[rgbLight.getId()-1][4] = (byte) rgbLight.getrValue();
            attrs[rgbLight.getId()-1][5] = (byte) rgbLight.getgValue();
            attrs[rgbLight.getId()-1][6] = (byte) rgbLight.getbValue();
            attrs[rgbLight.getId()-1][7] = (byte) rgbLight.getIntenseValue();
        }
        return attrs;
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
