package com.mariobgr.falcon.services;

import com.mariobgr.falcon.config.RabbitConfig;
import com.mariobgr.falcon.models.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitWriterService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RabbitWriterService.class);

    public boolean addRecord(MessageModel message) {

        try {

            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPIC_NAME, RabbitConfig.EXCHANGE_KEY_NAME, message);

            return true;

        } catch(Exception e) {

            logger.error("Unable to add message " + message.toString() + " to RabbitMQ Server", e);

            return false;

        }

    }

}
