package com.mariobgr.falcon.services;

import com.mariobgr.falcon.models.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisWriterService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RedisWriterService.class);

    public boolean addRecord(MessageModel message) {

        try {

            ValueOperations values = redisTemplate.opsForValue();
            values.set(message.getTimestamp() + ":" + message.getRandom(), message.toString());

            return true;

        } catch(Exception e) {

            logger.error("Unable to add message " + message.toString() + " to Redis Server", e);

            return false;

        }

    }

}
