package com.qualicom.availabilitydashboard.vo;

import java.util.List;

/**
 * Created by kangelov on 2015-08-25.
 */
public class Environment extends ListEntry {

    private List<Service> services;

    public Environment(String name, Status status) {
        super(name, status);
    }

    public Environment(String name, Status status, List<Service> services) {
        super(name, status);
        this.services = services;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
}
