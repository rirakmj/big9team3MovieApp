package com.cookandroid.big9team3movieapp;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {
    private String detail_link;
    private String title;
    private String release;
    private String genre;
    // private int likecount;
    // private String rank;
    private List rate;
    private String audiencecnt;
    private String synopsis;
    private String director;
    private String actor;
    private String img_url;
    private String grade;

    public Movie(String title, String url, String link, String release, String director, String genre,
                 List rate, String audiencecnt, String synopsis, String actor, String grade) {
        this.title = title;
        this.img_url = url;
        this.detail_link = link;
        this.release = release;
        this.director = director;
        this.genre = genre;
//        this.likecount = likecount;
//        this.rank = rank;
        this.rate = rate;
        this.audiencecnt = audiencecnt;
        this.synopsis = synopsis;
        this.actor = actor;
        this.grade = grade;


    }

    public String getDetail_link() {
        return detail_link;
    }

    public String getTitle() {
        return title;
    }

    public String getRelease() {
        return release;
    }

    public String getGenre() {
        return genre;
    }

    public List getRate() {
        return rate;
    }

    public String getAudiencecnt() {
        return audiencecnt;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getDirector() {
        return director;
    }

    public String getActor() {
        return actor;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getGrade() {
        return grade;
    }
}


