package com.qualicom.availabilitydashboard.net;

import com.qualicom.availabilitydashboard.vo.Environment;

import java.util.List;

/**
 * Created by kangelov on 2015-09-02.
 */
public interface CommunicationCallbacks {

    void handleRefresh();

    void handleError(String message);

}
