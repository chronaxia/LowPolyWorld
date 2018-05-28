package com.chronaxia.lowpolyworld.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 一非 on 2018/5/28.
 */

@Entity
public class Token {
    @Id
    private Long id;
    private String appId;
    private String apiKey;
    private String secretKey;
    private String token;
    private String opdate;
    @Generated(hash = 106060108)
    public Token(Long id, String appId, String apiKey, String secretKey,
            String token, String opdate) {
        this.id = id;
        this.appId = appId;
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.token = token;
        this.opdate = opdate;
    }
    @Generated(hash = 79808889)
    public Token() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAppId() {
        return this.appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getApiKey() {
        return this.apiKey;
    }
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    public String getSecretKey() {
        return this.secretKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getOpdate() {
        return this.opdate;
    }
    public void setOpdate(String opdate) {
        this.opdate = opdate;
    }
}
