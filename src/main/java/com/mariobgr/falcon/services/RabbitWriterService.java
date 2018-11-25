package com.mariobgr.falcon.services;

import com.mariobgr.falcon.config.RabbitConfig;
import com.mariobgr.falcon.models.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitWriterService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RabbitWriterService.class);

    public boolean addRecord(MessageModel message) {

        try {

            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPIC_NAME, RabbitConfig.EXCHANGE_KEY_NAME, message);
            messagingTemplate.convertAndSend("/topic/falcon", message.toString());

            return true;

        } catch(Exception e) {

            logger.error("Unable to add message " + message.toString() + " to RabbitMQ/STOMP Server", e);

            return false;

        }

    }

}
