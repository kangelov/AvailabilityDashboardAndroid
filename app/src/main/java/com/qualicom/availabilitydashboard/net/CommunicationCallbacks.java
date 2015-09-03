package com.qualicom.availabilitydashboard.net;

import com.qualicom.availabilitydashboard.vo.Environment;

import java.util.Date;
import java.util.List;

/**
 * Created by kangelov on 2015-09-02.
 */
public interface CommunicationCallbacks {

    void handleResponse(List<Environment> environmentList, Date updateTime);

    void handleError(String message);

}
