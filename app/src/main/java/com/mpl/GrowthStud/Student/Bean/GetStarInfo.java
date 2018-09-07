package com.mpl.GrowthStud.Student.Bean;

public class GetStarInfo {
    private String title;
    private int getStar;
    private int totalStar;
    private int score;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGetStar() {
        return getStar;
    }

    public void setGetStar(int getStar) {
        this.getStar = getStar;
    }

    public int getTotalStar() {
        return totalStar;
    }

    public void setTotalStar(int totalStar) {
        this.totalStar = totalStar;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public GetStarInfo(String title, int getStar, int totalStar, int score) {
        this.title = title;
        this.getStar = getStar;
        this.totalStar = totalStar;
        this.score = score;
    }

}
