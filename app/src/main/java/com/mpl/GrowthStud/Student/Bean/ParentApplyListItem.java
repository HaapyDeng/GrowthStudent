package com.mpl.GrowthStud.Student.Bean;

public class ParentApplyListItem {

    private int id;
    private String user_id;
    private String parent_id;
    private String parent_username;
    private int role;
    private int status;
    private String content;
    private String mobile;
    private int gender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getParent_username() {
        return parent_username;
    }

    public void setParent_username(String parent_username) {
        this.parent_username = parent_username;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    public ParentApplyListItem(int id, String user_id, String parent_id, String parent_username, int role, int status, String content, String mobile, int gender) {
        this.id = id;
        this.user_id = user_id;
        this.parent_id = parent_id;
        this.parent_username = parent_username;
        this.role = role;
        this.status = status;
        this.content = content;
        this.mobile = mobile;
        this.gender = gender;
    }


}
