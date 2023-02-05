package com.cookandroid.big9team3movieapp;

public class MovieDetail {
    public String d_title;
    public String d_img_url;
    public String d_starrating;
    public String d_release;
    public String d_director;
//    public String d_grade;
//    public String d_audiencecnt;
//    public String d_likecnt;
//    public String d_synopsis;
//    public String d_actor;

    public MovieDetail(String title, String url, String starrating, String release, String director) {
        this.d_title = title;
        this.d_img_url = url;
        this.d_starrating = starrating;
        this.d_release = release;
        this.d_director = director;
    }

    public String getD_title() {
        return d_title;
    }

    public void setD_title(String d_title) {
        this.d_title = d_title;
    }

    public String getD_img_url() {
        return d_img_url;
    }

    public void setD_img_url(String d_img_url) {
        this.d_img_url = d_img_url;
    }

    public String getD_starrating() {
        return d_starrating;
    }

    public void setD_starrating(String d_starrating) {
        this.d_starrating = d_starrating;
    }

    public String getD_release() {
        return d_release;
    }

    public void setD_release(String d_release) {
        this.d_release = d_release;
    }

    public String getD_director() {
        return d_director;
    }

    public void setD_director(String d_director) {
        this.d_director = d_director;
    }
}
