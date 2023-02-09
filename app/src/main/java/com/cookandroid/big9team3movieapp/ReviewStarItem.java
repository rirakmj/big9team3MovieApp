package com.cookandroid.big9team3movieapp;

public class ReviewStarItem {
    String score;
    String shortreview;
    String writer;
    String title;

    public ReviewStarItem(String score, String shortreview, String writer, String title) {
        this.score = score;
        this.shortreview = shortreview;
        this.writer = writer;
        this.title = title;
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
