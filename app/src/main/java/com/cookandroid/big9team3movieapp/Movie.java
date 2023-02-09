package com.cookandroid.big9team3movieapp;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {

    private String key;
    private String title;
    private String detail_link;
    private String img_url;
    private String release;
    private String director;

    public Movie(String key, String title, String link, String url, String release, String director) {
        this.key = key;
        this.title = title;
        this.detail_link = link;
        this.img_url = url;
        this.release = release;
        this.director = director;
    }

    public String getTitle() {
        return title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail_link() {
        return detail_link;
    }

    public void setDetail_link(String detail_link) {
        this.detail_link = detail_link;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

}


