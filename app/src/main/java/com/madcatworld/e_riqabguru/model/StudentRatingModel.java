package com.madcatworld.e_riqabguru.model;

import androidx.annotation.NonNull;

public class StudentRatingModel {

    private String name;
    private int rating;
    private boolean isSwitch;
    private int class_Id;
    private int id;

    private int uid;
    private int urate;
    private int uClient_id;
    private String uName;
    private int uClass_id;

    public StudentRatingModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isSwitch() {
        return isSwitch;
    }

    public void setSwitch(boolean aSwitch) {
        isSwitch = aSwitch;
    }

    public int getClass_Id() {
        return class_Id;
    }

    public void setClass_Id(int class_Id) {
        this.class_Id = class_Id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUrate() {
        return urate;
    }

    public void setUrate(int urate) {
        this.urate = urate;
    }

    public int getuClient_id() {
        return uClient_id;
    }

    public void setuClient_id(int uClient_id) {
        this.uClient_id = uClient_id;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public int getuClass_id() {
        return uClass_id;
    }

    public void setuClass_id(int uClass_id) {
        this.uClass_id = uClass_id;
    }
}
