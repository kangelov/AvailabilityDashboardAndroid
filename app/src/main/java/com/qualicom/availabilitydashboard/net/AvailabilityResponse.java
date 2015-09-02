package com.qualicom.availabilitydashboard.net;

import com.qualicom.availabilitydashboard.vo.Environment;

import java.util.Date;
import java.util.List;

/**
 * Created by kangelov on 2015-09-02.
 */
public class AvailabilityResponse {

    private List<Environment> environments;
    private Date updateTime;

    public AvailabilityResponse() {
    }

    public AvailabilityResponse(List<Environment> environments, Date updateTime) {
        this.environments = environments;
        this.updateTime = updateTime;
    }

    public List<Environment> getEnvironments() {
        return environments;
    }

    public void setEnvironments(List<Environment> environments) {
        this.environments = environments;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
