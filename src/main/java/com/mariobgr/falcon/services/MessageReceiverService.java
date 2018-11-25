package com.mariobgr.falcon.services;

import com.mariobgr.falcon.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageReceiverService {

    private static final Logger logger = LoggerFactory.getLogger(MessageReceiverService.class);

    @RabbitListener(queues = RabbitConfig.EXCHANGE_QUEUE_NAME)
    public void consumeMessage(Message message) {

        logger.info("Message received : " + message);

    }

}
