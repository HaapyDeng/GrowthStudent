package com.mpl.GrowthStud.Student.Bean;

import org.json.JSONObject;


public class MixtureInfoListItem {

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }

    public MixtureInfoListItem(int t, JSONObject object) {
        this.t = t;
        this.object = object;
    }

    private int t;
    private JSONObject object;

}
