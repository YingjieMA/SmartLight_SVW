package com.csvw.myj.smartlight;

/**
 * @ClassName: MoodTemplate
 * @Description:
 * @Author: MYJ
 * @CreateDate: 2020/4/27 11:04
 */
public class MoodTemplate implements Comparable<MoodTemplate> {
    private int id;
    private String name;
    private String imagePath;

    public MoodTemplate(int id, String name, String imagePath) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int compareTo(MoodTemplate o) {
        return 0;
    }
}
