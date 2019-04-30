package com.mpl.GrowthStud.Student.Bean;

public class CloudPhotoBean {
    private int id;
    private String name;
    private int type;
    private boolean isChcked;    //多了这个属性来保存CheckBox的选择状态

    public CloudPhotoBean(int id, String name, int type, boolean isChcked) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isChcked = isChcked;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isChcked() {
        return isChcked;
    }

    public void setChcked(boolean chcked) {
        isChcked = chcked;
    }


}
