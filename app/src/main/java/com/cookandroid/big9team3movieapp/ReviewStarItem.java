package com.cookandroid.big9team3movieapp;

public class ReviewStarItem {
    String dkey;
    String score;
    String shortreview;
    String writer;
    String title;

    public ReviewStarItem() {

    }

    public ReviewStarItem(String dkey, String score, String shortreview, String writer, String title) {
        this.dkey= dkey;
        this.score = score;
        this.shortreview = shortreview;
        this.writer = writer;
        this.title = title;
    }

    public String getDkey() {
        return dkey;
    }

    public void setDkey(String dkey) {
        this.dkey = dkey;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getShortreview() {
        return shortreview;
    }

    public void setShortreview(String shortreview) {
        this.shortreview = shortreview;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
