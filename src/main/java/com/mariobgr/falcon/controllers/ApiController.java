package com.mariobgr.falcon.controllers;

import com.mariobgr.falcon.config.RabbitConfig;
import com.mariobgr.falcon.models.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.Random;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @ResponseBody
    @RequestMapping(value="/sendRequest", method= RequestMethod.POST)
    public String sendMessage(@RequestParam(value = "message", required = false, defaultValue = "0") String messageBody) {

        Random rand = new Random();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        MessageModel message = new MessageModel(messageBody, rand.nextInt(), timestamp.toString());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPIC_NAME, RabbitConfig.EXCHANGE_KEY_NAME, message);
        logger.info("Message sent from api " + timestamp.toString());

        return "ok";

    }

}
