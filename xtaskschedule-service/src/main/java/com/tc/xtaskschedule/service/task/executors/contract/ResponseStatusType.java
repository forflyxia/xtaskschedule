
package com.tc.xtaskschedule.service.task.executors.contract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResponseStatusType implements Serializable {
    protected String timestamp;
    protected AckCodeType ack;
    protected List<ErrorDataType> errors;
    protected String traceLog;


    public String getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(String value) {
        this.timestamp = value;
    }

    public AckCodeType getAck() {
        return ack;
    }

    public void setAck(AckCodeType value) {
        this.ack = value;
    }

    public String getTraceLog() {
        return traceLog;
    }

    public void setTraceLog(String value) {
        this.traceLog = value;
    }

    public List<ErrorDataType> getErrors() {
        if (errors == null) {
            errors = new ArrayList<ErrorDataType>();
        }
        return errors;
    }

    public void setErrors(List<ErrorDataType> errors) {
        this.errors = errors;
    }

}
