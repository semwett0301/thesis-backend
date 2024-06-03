package com.example.utils.QueueUtils;

import java.util.UUID;

public interface QueueUtils {
    void scheduleGenerateTask(UUID routeId);

    Long getQueueSize();

    void popQueueElement();
}
