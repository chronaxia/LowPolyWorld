package com.chronaxia.lowpolyworld.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 一非 on 2018/4/23.
 */
@Entity
public class Player {
    @Id
    private Long id;
    private String name;
    @Generated(hash = 1918402006)
    public Player(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 30709322)
    public Player() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
