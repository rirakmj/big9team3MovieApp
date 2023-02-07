package com.cookandroid.big9team3movieapp;

public class MovieDetail {
    public String d_title;
    public String d_img_url;
    public String d_release;
    public String d_genre;
    public String d_runtime;
    public String d_grade;
    public Float d_starscorerb;
    public Float d_starscore;
    public String d_audiencecnt;
    public String d_synopsis;
    public String d_director;
    public String d_actor;
    public String d_likecnt;

    public MovieDetail(String title, String url, String release, String genre, String runtime,
                       String grade, Float starscorerb, Float starscore,
                       String audiencecnt, String synopsis, String director, String actor, String likecnt) {
        this.d_title = title;
        this.d_img_url = url;
        this.d_release = release;
        this.d_genre = genre;
        this.d_runtime = runtime;
        this.d_grade = grade;
        this.d_starscorerb = starscorerb;
        this.d_starscore = starscore;
        this.d_audiencecnt = audiencecnt;
        this.d_synopsis = synopsis;
        this.d_director = director;
        this.d_actor = actor;
        this.d_likecnt = likecnt;
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

    public String getD_release() {
        return d_release;
    }

    public void setD_release(String d_release) {
        this.d_release = d_release;
    }

    public String getD_genre() {
        return d_genre;
    }

    public void setD_genre(String d_genre) {
        this.d_genre = d_genre;
    }

    public String getD_runtime() {
        return d_runtime;
    }

    public void setD_runtime(String d_runtime) {
        this.d_runtime = d_runtime;
    }

    public String getD_grade() {
        return d_grade;
    }

    public void setD_grade(String d_grade) {
        this.d_grade = d_grade;
    }

    public Float getD_starscorerb() {
        return d_starscorerb;
    }

    public void setD_starscorerb(Float d_starscorerb) {
        this.d_starscorerb = d_starscorerb;
    }

    public Float getD_starscore() {
        return d_starscore;
    }

    public void setD_starscore(Float d_starscore) {
        this.d_starscore = d_starscore;
    }

    public String getD_audiencecnt() {
        return d_audiencecnt;
    }

    public void setD_audiencecnt(String d_audiencecnt) {
        this.d_audiencecnt = d_audiencecnt;
    }

    public String getD_synopsis() {
        return d_synopsis;
    }

    public void setD_synopsis(String d_synopsis) {
        this.d_synopsis = d_synopsis;
    }

    public String getD_director() {
        return d_director;
    }

    public void setD_director(String d_director) {
        this.d_director = d_director;
    }

    public String getD_actor() {
        return d_actor;
    }

    public void setD_actor(String d_actor) {
        this.d_actor = d_actor;
    }

    public String getD_likecnt() {
        return d_likecnt;
    }

    public void setD_likecnt(String d_likecnt) {
        this.d_likecnt = d_likecnt;
    }
}
