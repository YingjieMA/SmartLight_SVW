package com.csvw.myj.smartlight;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class GetColorTableList {
    private ArrayList<ColorTamplate> allList = new ArrayList<>();
    private ColorTamplate[] colorTamplates = new ColorTamplate[30];
    public GetColorTableList() {
        this.init();
    }
    private void init() {
        this.allList = allList;

//        ColorTamplate colorTamplate0 = new ColorTamplate(1,"gelb",Integer.parseInt("FEEF30", 16),Integer.parseInt("FEEF30", 16));
//        ColorTamplate colorTamplate1 = new ColorTamplate(2,"kaltweiss",Integer.parseInt("FFEBF7", 16),Integer.parseInt("FFEBF7", 16));
        this.colorTamplates[0] = new ColorTamplate(1,"creme","#DEFFA1","#DEFFA1");
        this.colorTamplates[1] = new ColorTamplate(2,"kaltweiss","#FEEBF7","#FFEBF7");
        this.colorTamplates[2] = new ColorTamplate(3,"magenta","#9A00FF","#9A00FF");
        this.colorTamplates[3] = new ColorTamplate(4,"sapir","#2E00FF","#2E00FF");
        this.colorTamplates[4] = new ColorTamplate(5,"greeTea","#00FFAB","#00FFAB");
        this.colorTamplates[5] = new ColorTamplate(6,"gelb","#FEEF30","#FEEF30");
        this.colorTamplates[6] = new ColorTamplate(7,"lachs","#FF7B98","#FF7B98");
        this.colorTamplates[7] = new ColorTamplate(8,"fileder","#7C39FE","#7C39FE");
        this.colorTamplates[8] = new ColorTamplate(9,"cyan","#0089FF","#0089FF");
        this.colorTamplates[9] = new ColorTamplate(10,"grun","#00FF22","#00FF22");
        this.colorTamplates[10] = new ColorTamplate(11,"bernstein","#FFA200","FFA200");
        this.colorTamplates[11] = new ColorTamplate(12,"koralle","#FF5563","#FF5563");
        this.colorTamplates[12] = new ColorTamplate(13,"lila","#4514FF","#4514FF");
        this.colorTamplates[13] = new ColorTamplate(14,"azur","#00B1FF","#00B1FF");
        this.colorTamplates[14] = new ColorTamplate(15,"apfelgrun","#00FF27","#00FF27");
        this.colorTamplates[15] = new ColorTamplate(16,"kurkuma","#FF9726","FF9726");
        this.colorTamplates[16] = new ColorTamplate(17,"hummer","#FF1261","#FF1261");
        this.colorTamplates[17] = new ColorTamplate(18,"blueberry","#3900FF","#3900FF");
        this.colorTamplates[18] = new ColorTamplate(19,"smaraged","#00FED5","#00FED5");
        this.colorTamplates[19] = new ColorTamplate(20,"pistazie","#70FF29","#70FF29");
        this.colorTamplates[20] = new ColorTamplate(21,"blutorange","#FF5F1E","#FF5F1E");
        this.colorTamplates[21] = new ColorTamplate(22,"pink","#FF008F","#FF008F");
        this.colorTamplates[22] = new ColorTamplate(23,"dunkeblau","#002CFF","#002CFF");
        this.colorTamplates[23] = new ColorTamplate(24,"mint","#00FFE5","#00FFE5");
        this.colorTamplates[24] = new ColorTamplate(25,"limette","#B8FF2D","#B8FF2D");
        this.colorTamplates[25] = new ColorTamplate(26,"rot","#FF001A","#FF001A");
        this.colorTamplates[26] = new ColorTamplate(27,"zuckerwatte","#B34FFF","#B34FFF");
        this.colorTamplates[27] = new ColorTamplate(28,"kornblume","#2672FE","#2672FE");
        this.colorTamplates[28] = new ColorTamplate(29,"pfefferminz","#59FE86","#59FE86");
        this.colorTamplates[29] = new ColorTamplate(30,"zitronengelb","#E9FF26","#E9FF26");
        for (ColorTamplate colorTamplate : colorTamplates) {
            allList.add(colorTamplate);
        }
//        allList.add(colorTamplate0);
//        allList.add(colorTamplate1);

    }

    public ArrayList<ColorTamplate> getAllList() {
        return allList;
    }
}
