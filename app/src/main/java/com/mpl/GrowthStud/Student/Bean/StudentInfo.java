package com.mpl.GrowthStud.Student.Bean;
/*
##StudentInfo {
school_name (string, optional): 学校名称 ,
classroom_name (string, optional): 班级名称 ,
grade (string, optional): 年级 ,
teacher_name (string, optional): 班主任 ,
birthday (string, optional): 生日 ,
gender (string, optional): 性别 ,
scope (string, optional): 学龄段 ,
username (string, optional): 账号
}
 */

public class StudentInfo {
    private String school_name;
    private String classroom_name;
    private String grade;
    private String teacher_name;
    private String birthday;
    private String gender;
    private String scope;
    private String username;

    public StudentInfo(String school_name, String classroom_name, String grade, String teacher_name, String birthday, String gender, String scope, String username) {
        this.school_name = school_name;
        this.classroom_name = classroom_name;
        this.grade = grade;
        this.teacher_name = teacher_name;
        this.birthday = birthday;
        this.gender = gender;
        this.scope = scope;
        this.username = username;
    }

    public StudentInfo() {

    }


    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
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

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
