package com.example.utils.QueueUtils;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RedisQueueUtils implements QueueUtils {
    private final RedisTemplate<String, Object> redisTemplate;
    private final String topic;
    private final String queueName;



    public RedisQueueUtils(@Value("${redis-queue.topic}") String topic, @Value("${redis-queue.name}") String queueName, RedisTemplate<String, Object> redisTemplate, Gson gson) {
        this.topic = topic;
        this.redisTemplate = redisTemplate;
        this.queueName = queueName;
    }

    @Override
    public void scheduleGenerateTask(UUID routeId) {
        redisTemplate.convertAndSend(topic, routeId.toString());
        redisTemplate.opsForList().leftPush(queueName, routeId);
    }

    @Override
    public Long getQueueSize() {
        return redisTemplate.opsForList().size(queueName);
    }

    @Override
    public void popQueueElement() {
        redisTemplate.opsForList().rightPop(queueName);
    }
}
