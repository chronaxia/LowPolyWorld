package com.chronaxia.lowpolyworld.model.entity;

import java.io.Serializable;

/**
 * Created by 一非 on 2018/5/19.
 */

public class Continent implements Serializable{

    private static final long serialVersionUID = 2781005873774389807L;

    private String name;
    private String phonetic;
    private String english;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Continent{" +
                "name='" + name + '\'' +
                ", phonetic='" + phonetic + '\'' +
                ", english='" + english + '\'' +
                '}';
    }
}
