package com.mariobgr.falcon.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariobgr.falcon.dao.MessageDao;
import com.mariobgr.falcon.models.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageReceiverService {

    @Autowired
    MessageDao messageDao;

    private static final Logger logger = LoggerFactory.getLogger(MessageReceiverService.class);

    @RabbitListener(queues = "${spring.rabbit.queue}")
    public void consumeMessage(Message message) {

        try {

            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            MessageModel messageModel = objectMapper.readValue(message.getBody(), MessageModel.class);

            logger.info("Message received : " + message);

            if(messageDao.save(messageModel) > 0) {

                logger.info("Message " + messageModel.getMessage() + " saved to MySQL at " + messageModel.getTimestamp());

            }

        } catch (Exception e) {

            logger.error("Error receiving message : " + message, e);

        }



    }

}
