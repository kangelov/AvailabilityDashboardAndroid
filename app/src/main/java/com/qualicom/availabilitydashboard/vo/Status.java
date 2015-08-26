package com.qualicom.availabilitydashboard.vo;

/**
 * Created by kangelov on 2015-08-24.
 */
public enum Status {
    FAILED("FAILED"),
    WRONG_VERSION("WRONG_VERSION"),
    OK("OK"),
    UNKNOWN("UNKNOWN");


    String status;

    private Status(String status) {
        this.status = status;
    }
}
