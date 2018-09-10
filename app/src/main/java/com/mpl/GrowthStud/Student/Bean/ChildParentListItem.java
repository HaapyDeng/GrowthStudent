package com.mpl.GrowthStud.Student.Bean;

public class ChildParentListItem {
    private String parent_id; //家长ID
    private String parent_username;//家长姓名
    private int role;//家长角色
    private int gender;//学龄段
    private String mobile;//手机号码

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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public ChildParentListItem(String parent_id, String parent_username, int role, int gender, String mobile) {
        this.parent_id = parent_id;
        this.parent_username = parent_username;
        this.role = role;
        this.gender = gender;
        this.mobile = mobile;
    }


}
