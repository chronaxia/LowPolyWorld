package com.chronaxia.lowpolyworld.model.entity;

import java.io.Serializable;

/**
 * Created by 一非 on 2018/5/19.
 */

public class ScenicSpot implements Serializable{

    private static final long serialVersionUID = 3918553608282231108L;

    private String name;
    private String continent;
    private String phonetic;
    private String english;
    private String location;
    private String describe;
    private String picture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "ScenicSpot{" +
                "name='" + name + '\'' +
                ", continent='" + continent + '\'' +
                ", phonetic='" + phonetic + '\'' +
                ", english='" + english + '\'' +
                ", location='" + location + '\'' +
                ", describe='" + describe + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
