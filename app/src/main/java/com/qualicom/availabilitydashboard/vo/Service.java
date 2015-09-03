package com.qualicom.availabilitydashboard.vo;

import java.util.List;

/**
 * Created by kangelov on 2015-08-25.
 */
public class Service extends ListEntry {

    private List<Node> nodes;

    public Service() {
        super();
    }

    public Service(String name, Status status) {
        super(name, status);
    }

    public Service(String name, Status status, List<Node> nodes) {
        super(name, status);
        this.nodes = nodes;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
}
