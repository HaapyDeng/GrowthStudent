package com.mpl.GrowthStud.Parent.Bean;

public class PstudentApplyListItem {
    /*
    "child":[{"user_id":"00153700036298300001000084480001","username":"邓吉州","classroom_name":"二班","grade":"小学一年级","school":"赤峰第四实验小学","gender":1}]}
     */
    private String user_id;
    private String username;
    private String classroom_name;
    private String grade;
    private String school;
    private int gender;

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

    public String getClassroom_name() {
        return classroom_name;
    }

    public void setClassroom_name(String classroom_name) {
        this.classroom_name = classroom_name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    public PstudentApplyListItem(String user_id, String username, String classroom_name, String grade, String school, int gender) {
        this.user_id = user_id;
        this.username = username;
        this.classroom_name = classroom_name;
        this.grade = grade;
        this.school = school;
        this.gender = gender;
    }


}
