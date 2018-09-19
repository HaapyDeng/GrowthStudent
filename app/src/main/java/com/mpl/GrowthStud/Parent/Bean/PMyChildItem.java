package com.mpl.GrowthStud.Parent.Bean;

import org.json.JSONObject;

public class PMyChildItem {
    private String user_id;
    private String username;
    private JSONObject info;

    public PMyChildItem(String user_id, String username, JSONObject info) {
        this.user_id = user_id;
        this.username = username;
        this.info = info;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public JSONObject getInfo() {
        return info;
    }

    public void setInfo(JSONObject info) {
        this.info = info;
    }


}
