package com.ytempest.wanandroid.http.bean;

import java.util.List;

/**
 * @author ytempest
 * @since 2020/8/11
 */
public class LoginBean {

    /**
     * collectIds : []
     * icon :
     * password :
     * type : 0
     * username : 0147369258
     */
    private String icon;
    private String password;
    private int type;
    private String username;
    private List<?> collectIds;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<?> getCollectIds() {
        return collectIds;
    }

    public void setCollectIds(List<?> collectIds) {
        this.collectIds = collectIds;
    }
}