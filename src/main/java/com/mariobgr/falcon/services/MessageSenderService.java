package com.mariobgr.falcon.services;

import com.mariobgr.falcon.models.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Random;

@Service
public class MessageSenderService {

    @Value("${spring.rabbit.topic}")
    private String rabbitTopic;

    @Value("${spring.rabbit.key}")
    private String rabbitKey;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(MessageSenderService.class);

    public MessageSenderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelay = 5000L)
    public void sendMessage() {

        Random rand = new Random();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long unixTimestamp = System.currentTimeMillis() / 1000L;

        MessageModel message = new MessageModel("This is an automated message", rand.nextInt(100), Long.toString(unixTimestamp), -1);

        rabbitTemplate.convertAndSend(rabbitTopic, rabbitKey, message);
        messagingTemplate.convertAndSend("/topic/falcon", message.toString());

        logger.info("Message automatically sent at " + timestamp.toString());

    }

}
