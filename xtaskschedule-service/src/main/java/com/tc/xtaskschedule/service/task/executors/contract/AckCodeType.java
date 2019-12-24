
package com.tc.xtaskschedule.service.task.executors.contract;

public enum AckCodeType {
    SUCCESS("Success"),
    FAILURE("Failure"),
    WARNING("Warning"),
    PARTIAL_FAILURE("PartialFailure");
    private final String value;

    AckCodeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AckCodeType fromValue(String v) {
        for (AckCodeType c: AckCodeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
