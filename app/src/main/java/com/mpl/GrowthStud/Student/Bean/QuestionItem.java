package com.mpl.GrowthStud.Student.Bean;

import org.json.JSONArray;
import org.json.JSONObject;

public class QuestionItem {
    /*
    id (string, optional): id ,
    type (integer, optional): 类型。1单选题，2多选题。3说明 ,
    name (string, optional): 题干，内容 ,
    point (string, optional): 每道题总分数 ,
    options (##QuestionItemOptions, optional): 选择项，json数组
    ##options {
    id (string, optional): 选项id ,
    name (string, optional): 内容 ,
    point (string, optional): 每道题分数
}
     */
    private String id;
    private String name;
    private int point;
    private int type;
    private JSONArray options;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public JSONArray getOptions() {
        return options;
    }

    public void setOptions(JSONArray options) {
        this.options = options;
    }


    public QuestionItem(String id, String name, int point, int type, JSONArray options) {
        this.id = id;
        this.name = name;
        this.point = point;
        this.type = type;
        this.options = options;
    }


}
