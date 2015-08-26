package com.qualicom.availabilitydashboard.vo;

/**
 * Created by kangelov on 2015-08-25.
 */
public class Node extends ListEntry {


    private String response;

    private String version;

    public Node(String name, Status status, String response, String version) {
        super(name, status);
        this.response = response;
        this.version = version;
    }

    public Node(String name, Status status) {
        super(name, status);
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
