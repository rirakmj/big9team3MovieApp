package com.cookandroid.big9team3movieapp;

import java.sql.Timestamp;

public class ReviewItem {
   // String id;
    String writer;
    String content;
    String regdate;

    public ReviewItem(){}

    public ReviewItem( String writer, String content, String regdate) {
       // this.id = id;
        this.writer = writer;
        this.content = content;
        this.regdate = regdate;
    }


//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }


}
