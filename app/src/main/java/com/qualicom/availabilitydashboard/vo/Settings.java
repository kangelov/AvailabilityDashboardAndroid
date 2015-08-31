package com.qualicom.availabilitydashboard.vo;

/**
 * Created by kangelov on 2015-08-31.
 */
public class Settings {

    private String uri;

    private String username;

    private String password;

    public Settings(String uri, String username, String password) {
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
}
