package com.qualicom.availabilitydashboard.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kangelov on 2015-08-24.
 */
public class ListEntry implements Serializable {

    public static List<Environment> dummyEntries = new ArrayList<Environment>();
    static {
        dummyEntries.add(new Environment("PRODUCTION B",Status.OK,
                Arrays.asList(new Service("CSAg 4", Status.OK,
                        Arrays.asList(new Node("SOA", Status.OK, "Pong", "4.0"),
                                new Node("Node 1", Status.OK, "Pong", "4.0"))),
                new Service("CSAg 5", Status.FAILED,
                        Arrays.asList(new Node("SOA", Status.WRONG_VERSION, "Pong", "5.0"),
                                new Node("Node 1", Status.FAILED, "Pong", "5.0"))))));
        dummyEntries.add(new Environment("PT148",Status.FAILED,
                Arrays.asList(new Service("CSAg 4", Status.OK,
                                Arrays.asList(new Node("SOA", Status.OK, "Pong", "4.0"),
                                        new Node("Node 1", Status.OK, "Pong", "4.0"))),
                        new Service("CSAg 5", Status.FAILED,
                                Arrays.asList(new Node("SOA", Status.WRONG_VERSION, "Pong", "5.0"),
                                        new Node("Node 1", Status.FAILED, "Pong", "5.0"))))));
        dummyEntries.add(new Environment("PT168",Status.WRONG_VERSION,
                Arrays.asList(new Service("CSAg 4", Status.OK,
                                Arrays.asList(new Node("SOA", Status.OK, "Pong", "4.0"),
                                        new Node("Node 1", Status.OK, "Pong", "4.0"))),
                        new Service("CSAg 5", Status.WRONG_VERSION,
                                Arrays.asList(new Node("SOA", Status.WRONG_VERSION, "Pong", "5.0"),
                                        new Node("Node 1", Status.FAILED, "Pong", "5.0"))))));
    }

    private Status status;

    private String name;

    public ListEntry() {
    }

    public ListEntry(String name, Status status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    //this must be overriden with something meaningful.
    public String getDescription() {
        return getName();
    }


    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!this.getClass().isInstance(o)) return false;
        ListEntry other = (ListEntry) o;
        return ((this.getName() == null && other.getName() == null) ||
                (this.getName().equals(other.getName())));
    }
}
