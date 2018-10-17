package com.mpl.GrowthStud.Student.Bean;

public class FormAnswerBean {

    private String id;
    private String answer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public FormAnswerBean(String id, String answer) {
        this.id = id;
        this.answer = answer;
    }


}
