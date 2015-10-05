package com.qualicom.availabilitydashboard.net;

/**
 * Created by kangelov on 2015-09-02.
 */
public interface CommunicationCallbacks {

    void handleRefresh();

    void handleError(String message);

}
