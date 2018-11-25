package com.mariobgr.falcon.controllers;

import com.mariobgr.falcon.models.MessageModel;
import com.mariobgr.falcon.services.RabbitWriterService;
import com.mariobgr.falcon.services.RedisWriterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private RabbitWriterService rabbitWriter;

    @Autowired
    private RedisWriterService redisWriter;

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @ResponseBody
    @RequestMapping(value="/sendRequest", method= RequestMethod.POST)
    public String sendMessage(@RequestBody MessageModel messageBody) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        MessageModel message = new MessageModel(
                messageBody.getMessage(),
                messageBody.getRandom(),
                messageBody.getTimestamp()
        );

        logger.info("Message " + messageBody.getMessage() + " sent to api at " + timestamp.toString());

        if(rabbitWriter.addRecord(message)) {

            logger.info("Message " + message.getMessage() + " saved to RabbitMQ at " + timestamp.toString());

        }


        if(redisWriter.addRecord(message)) {

            logger.info("Message " + message.getMessage() + " saved to Redis Server at " + timestamp.toString());

        }

        return "Request Received!";

    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handle(HttpMessageNotReadableException e) {

        logger.error("HttpMessageNotReadableException occurred.", e);

        return "Bad Request!";

    }

}
