package com.tc.xtaskschedule.service.task;

public interface TaskEnum {

    enum TaskStatus {

        NONE,
        RUNNING,
        STOPPED;
    }

    enum TaskExecutorType {

        NONE("none"),
        KAFKA("kafka"),
        HTTP("http"),
        ElasticSearch("elasticsearch");

        private String name;

        TaskExecutorType(String name) {
            this.name = name;
        }

        public static TaskExecutorType getValue(String name) {
            for (TaskExecutorType item : TaskExecutorType.values()) {
                if (item.name.equalsIgnoreCase(name)) {
                    return item;
                }
            }
            return TaskExecutorType.NONE;
        }
    }
}
