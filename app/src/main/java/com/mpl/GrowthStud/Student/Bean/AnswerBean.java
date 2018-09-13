package com.mpl.GrowthStud.Student.Bean;

public class AnswerBean {
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getOptionsId() {
        return optionsId;
    }

    public void setOptionsId(String optionsId) {
        this.optionsId = optionsId;
    }

    public AnswerBean(String itemId, String optionsId) {
        this.itemId = itemId;
        this.optionsId = optionsId;
    }

    public String itemId;
    public String optionsId;
}
