package com.cookandroid.big9team3movieapp;

public class ReviewStarItem {
    String myscore;
    String myshortreview;
    String writer;

    public ReviewStarItem(String myscore, String myshortreview, String writer) {
        this.myscore = myscore;
        this.myshortreview = myshortreview;
        this.writer = writer;
    }

    public String getMyscore() {
        return myscore;
    }

    public void setMyscore(String myscore) {
        this.myscore = myscore;
    }

    public String getMyshortreview() {
        return myshortreview;
    }

    public void setMyshortreview(String myshortreview) {
        this.myshortreview = myshortreview;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
}
