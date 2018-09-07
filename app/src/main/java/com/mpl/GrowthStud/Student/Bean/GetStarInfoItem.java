package com.mpl.GrowthStud.Student.Bean;

public class GetStarInfoItem {
    private String classroom_id;
    private String category_name;
    private String category_id;
    private String grade;
    private String star;
    private String task_star;
    private String point;
    private String total_point;

    public GetStarInfoItem(String classroom_id, String category_name, String category_id, String grade, String star, String task_star, String point, String total_point) {
        this.classroom_id = classroom_id;
        this.category_name = category_name;
        this.category_id = category_id;
        this.grade = grade;
        this.star = star;
        this.task_star = task_star;
        this.point = point;
        this.total_point = total_point;
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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
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


}
