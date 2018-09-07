package com.mpl.GrowthStud.Student.Bean;

public class GetStarInfoInfoItem {
    /*
    id (string, optional): id ,
classroom_id (integer, optional): 班级id ,
category_name (string, optional): 分类名称 ,
label_name (string, optional): 标签名称 ,
name (string, optional): 成就名称 ,
image (string, optional): 图片 ,
complete_name (string, optional): 完成人姓名 ,
type (string, optional): 类型， 1文本，2图文，3视频，4问卷，5系统 ,
grade (string, optional): 年级 ,
star (integer, optional): 获得星 ,
task_star (integer, optional): 成就星 ,
point (integer, optional): 问卷分 ,
total_point (integer, optional): 总分 ,
updated_at (integer, optional): 完成时间
     */
    private String id;
    private String classroom_id;
    private String category_name;
    private String label_name;
    private String name;
    private String image;
    private String complete_name;
    private String type;
    private String grade;
    private String star;
    private String task_star;
    private String point;
    private String total_point;
    private String updated_at;

    public GetStarInfoInfoItem(String id, String classroom_id, String category_name, String label_name, String name, String image, String complete_name, String type, String grade, String star, String task_star, String point, String total_point, String updated_at) {
        this.id = id;
        this.classroom_id = classroom_id;
        this.category_name = category_name;
        this.label_name = label_name;
        this.name = name;
        this.image = image;
        this.complete_name = complete_name;
        this.type = type;
        this.grade = grade;
        this.star = star;
        this.task_star = task_star;
        this.point = point;
        this.total_point = total_point;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassroom_id() {
        return classroom_id;
    }

    public void setClassroom_id(String classroom_id) {
        this.classroom_id = classroom_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getLabel_name() {
        return label_name;
    }

    public void setLabel_name(String label_name) {
        this.label_name = label_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComplete_name() {
        return complete_name;
    }

    public void setComplete_name(String complete_name) {
        this.complete_name = complete_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getTask_star() {
        return task_star;
    }

    public void setTask_star(String task_star) {
        this.task_star = task_star;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getTotal_point() {
        return total_point;
    }

    public void setTotal_point(String total_point) {
        this.total_point = total_point;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


}
