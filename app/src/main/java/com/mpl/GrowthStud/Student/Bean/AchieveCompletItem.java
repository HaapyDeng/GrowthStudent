package com.mpl.GrowthStud.Student.Bean;

public class AchieveCompletItem {
    private String id;
    private String name;
    private String type;
    private String image;
    private String category_name;
    private String label_name;
    private String task_star;
    private String status;
    private String role;
    private String star;

    public AchieveCompletItem() {

    }

    public AchieveCompletItem(String id, String name, String type, String image, String category_name, String label_name, String task_star, String status, String role, String star) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.image = image;
        this.category_name = category_name;
        this.label_name = label_name;
        this.task_star = task_star;
        this.status = status;
        this.role = role;
        this.star = star;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getTask_star() {
        return task_star;
    }

    public void setTask_star(String task_star) {
        this.task_star = task_star;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }


}
