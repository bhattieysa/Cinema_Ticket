package com.example.cinematicket.Model;

import android.net.Uri;

import java.util.ArrayList;

public class MovieModel {
    public MovieModel() {
    }

    public String getFirstshow() {
        return firstshow;
    }

    public void setFirstshow(String firstshow) {
        this.firstshow = firstshow;
    }

    public String getSecondshow() {
        return secondshow;
    }

    public void setSecondshow(String secondshow) {
        this.secondshow = secondshow;
    }

    public String getThirdshow() {
        return thirdshow;
    }

    public void setThirdshow(String thirdshow) {
        this.thirdshow = thirdshow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getImage() {
        return image;
    }

    public MovieModel(String title, String summary, String date, String image, String language, String genre, String firstshow, String secondshow, String thirdshow, String id, String imgpath) {
        this.title = title;
        this.summary = summary;
        this.date = date;
        this.image = image;
        this.language = language;
        this.genre = genre;
        this.firstshow = firstshow;
        this.secondshow = secondshow;
        this.thirdshow = thirdshow;
        this.id = id;
        this.imgpath = imgpath;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    String title,summary,date,image,language,genre,firstshow,secondshow,thirdshow,id,imgpath;

}
