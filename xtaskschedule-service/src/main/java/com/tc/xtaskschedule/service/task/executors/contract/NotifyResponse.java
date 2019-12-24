package com.tc.xtaskschedule.service.task.executors.contract;

import java.io.Serializable;

public class NotifyResponse implements Serializable {

    private ResponseStatusType responseStatus;

    public ResponseStatusType getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatusType responseStatus) {
        this.responseStatus = responseStatus;
    }
}
