
package com.tc.xtaskschedule.service.task.executors.contract;

import java.io.Serializable;

public class ErrorDataType implements Serializable
{

    protected String errorCode;
    protected String message;
    protected String stackTrace;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String value) {
        this.errorCode = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String value) {
        this.stackTrace = value;
    }

}
