package com.mariobgr.falcon.services;

import com.mariobgr.falcon.config.RabbitConfig;
import com.mariobgr.falcon.models.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Random;

@Service
public class MessageSenderService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(MessageSenderService.class);

    public MessageSenderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelay = 5000L)
    public void sendMessage() {

        Random rand = new Random();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        MessageModel message = new MessageModel("This is a message", rand.nextInt(), timestamp.toString(), -1);

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPIC_NAME, RabbitConfig.EXCHANGE_KEY_NAME, message);

        logger.info("Message automatically sent at " + timestamp.toString());

    }

}
