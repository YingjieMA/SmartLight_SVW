package com.csvw.myj.smartlight;

import java.util.ArrayList;

public class GetColorTableList {
    private ArrayList<ColorTemplate> allList = new ArrayList<>();
    private ColorTemplate[] colorTemplates = new ColorTemplate[30];
    public GetColorTableList() {
        this.init();
    }
    private void init() {
        this.allList = allList;

//        ColorTemplate colorTamplate0 = new ColorTemplate(1,"gelb",Integer.parseInt("FEEF30", 16),Integer.parseInt("FEEF30", 16));
//        ColorTemplate colorTamplate1 = new ColorTemplate(2,"kaltweiss",Integer.parseInt("FFEBF7", 16),Integer.parseInt("FFEBF7", 16));
        this.colorTemplates[0] = new ColorTemplate(1,"creme","#DEFFA1","#DEFFA1");
        this.colorTemplates[1] = new ColorTemplate(2,"kaltweiss","#FEEBF7","#FFEBF7","VW10");
        this.colorTemplates[2] = new ColorTemplate(3,"magenta","#9A00FF","#9A00FF");
        this.colorTemplates[3] = new ColorTemplate(4,"sapir","#2E00FF","#2E00FF");
        this.colorTemplates[4] = new ColorTemplate(5,"greenTea","#00FFAB","#00FFAB");
        this.colorTemplates[5] = new ColorTemplate(6,"gelb","#FEEF30","#FEEF30");
        this.colorTemplates[6] = new ColorTemplate(7,"lachs","#FF7B98","#FF7B98");
        this.colorTemplates[7] = new ColorTemplate(8,"fileder","#7C39FE","#7C39FE");
        this.colorTemplates[8] = new ColorTemplate(9,"cyan","#0089FF","#0089FF","VW10");
        this.colorTemplates[9] = new ColorTemplate(10,"grun","#00FF22","#00FF22");
        this.colorTemplates[10] = new ColorTemplate(11,"bernstein","#FFA200","FFA200","VW10");
        this.colorTemplates[11] = new ColorTemplate(12,"koralle","#FF5563","#FF5563");
        this.colorTemplates[12] = new ColorTemplate(13,"lila","#4514FF","#4514FF","VW10");
        this.colorTemplates[13] = new ColorTemplate(14,"azur","#00B1FF","#00B1FF");
        this.colorTemplates[14] = new ColorTemplate(15,"apfelgrun","#00FF27","#00FF27");
        this.colorTemplates[15] = new ColorTemplate(16,"kurkuma","#FF9726","FF9726");
        this.colorTemplates[16] = new ColorTemplate(17,"hummer","#FF1261","#FF1261");
        this.colorTemplates[17] = new ColorTemplate(18,"blueberry","#3900FF","#3900FF");
        this.colorTemplates[18] = new ColorTemplate(19,"smaraged","#00FED5","#00FED5");
        this.colorTemplates[19] = new ColorTemplate(20,"pistazie","#70FF29","#70FF29");
        this.colorTemplates[20] = new ColorTemplate(21,"blutorange","#FF5F1E","#FF5F1E","VW10");
        this.colorTemplates[21] = new ColorTemplate(22,"pink","#FF008F","#FF008F","VW10");
        this.colorTemplates[22] = new ColorTemplate(23,"dunkeblau","#002CFF","#002CFF","VW10");
        this.colorTemplates[23] = new ColorTemplate(24,"mint","#00FFE5","#00FFE5","VW10");
        this.colorTemplates[24] = new ColorTemplate(25,"limette","#B8FF2D","#B8FF2D");
        this.colorTemplates[25] = new ColorTemplate(26,"rot","#FF001A","#FF001A","VW30");
        this.colorTemplates[26] = new ColorTemplate(27,"zuckerwatte","#B34FFF","#B34FFF");
        this.colorTemplates[27] = new ColorTemplate(28,"kornblume","#2672FE","#2672FE");
        this.colorTemplates[28] = new ColorTemplate(29,"pfefferminz","#59FE86","#59FE86");
        this.colorTemplates[29] = new ColorTemplate(30,"zitronengelb","#E9FF26","#E9FF26","VW10");
        for (ColorTemplate colorTemplate : colorTemplates) {
            allList.add(colorTemplate);
        }
//        allList.add(colorTamplate0);
//        allList.add(colorTamplate1);

    }

    public ArrayList<ColorTemplate> getAllList() {
        return allList;
    }
}
