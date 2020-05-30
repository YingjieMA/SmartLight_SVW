package com.csvw.myj.smartlight;

public class Light implements Comparable<Light> {

    // 灯泡id
    private int id;
    // 灯泡名称
    private String name;
    // 类型
    private String type;
    // 灯的状态
    private Boolean state;
    public void setState(Boolean state) {
        this.state = state;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getState() {
        return state;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Light() {
    }

    public Light(int id, String name, String type, Boolean state) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.state = state;
    }

    @Override
    public int compareTo(Light o) {
        int id1 = Integer.valueOf(this.id);
        int id2 = Integer.valueOf(o.getId());
        return id1 - id2;
    }

    @Override
    public String toString() {
        return  "Light{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", state=" + state +
                '}';
    }
}
