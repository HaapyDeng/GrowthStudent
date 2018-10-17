package com.mpl.GrowthStud.Student.Bean;

import org.json.JSONObject;

/**
 * "id": "00153948667623300001000280900001",
 * "label": "队伍名称",
 * "prompt": "请输入队伍名称",
 * "type": 1,
 * "options": "",
 * "order": 1
 * object
 */

public class FormInfoListItem {
    private String id;
    private String label;
    private String prompt;
    private int type;
    private String options;
    private int order;
    private JSONObject answers;

    public JSONObject getAnswers() {
        return answers;
    }

    public void setAnswers(JSONObject answers) {
        this.answers = answers;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }


    public FormInfoListItem(String id, String label, String prompt, int type, String options, int order, JSONObject answers) {
        this.id = id;
        this.label = label;
        this.prompt = prompt;
        this.type = type;
        this.options = options;
        this.order = order;
        this.answers = answers;
    }


}
