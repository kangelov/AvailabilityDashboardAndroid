package com.qualicom.availabilitydashboard.vo;

import java.io.Serializable;

/**
 * Created by kangelov on 2015-08-31.
 */
public class Settings implements Serializable {

    private String uri;

    private String username;

    private String password;

    private String lastUpdateDate;

    private String lastRefreshDate;

    public Settings(String uri, String username, String password) {
        this.password = password;
        this.uri = uri;
        this.username = username;
    }

    public Settings(String uri, String username, String password, String lastRefreshDate, String lastUpdateDate) {
        this.lastRefreshDate = lastRefreshDate;
        this.lastUpdateDate = lastUpdateDate;
        this.password = password;
        this.uri = uri;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getLastRefreshDate() {
        return lastRefreshDate;
    }

    public void setLastRefreshDate(String lastRefreshDate) {
        this.lastRefreshDate = lastRefreshDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
