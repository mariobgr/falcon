package com.mariobgr.falcon.controllers;

import com.mariobgr.falcon.dao.MessageDao;
import com.mariobgr.falcon.models.MessageModel;
import com.mariobgr.falcon.services.RabbitWriterService;
import com.mariobgr.falcon.services.RedisWriterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private RabbitWriterService rabbitWriter;

    @Autowired
    private RedisWriterService redisWriter;

    @Autowired
    private MessageDao messageDao;

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @RequestMapping(value="/sendRequest", method= RequestMethod.POST)
    public Map sendMessage(@RequestBody MessageModel messageBody) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        MessageModel message = new MessageModel(
                messageBody.getMessage(),
                messageBody.getRandom(),
                messageBody.getTimestamp(),
                -1
        );

        logger.info("Message " + messageBody.getMessage() + " sent to API at " + timestamp.toString());

        if(rabbitWriter.addRecord(message)) {

            logger.info("Message " + message.getMessage() + " saved to RabbitMQ at " + timestamp.toString());

        }


        if(redisWriter.addRecord(message)) {

            logger.info("Message " + message.getMessage() + " saved to Redis Server at " + timestamp.toString());

        }

        Map<String, String> response = new HashMap<>();
        response.put("success", "Request Received!");
        response.put("error", "false");

        return response;

    }

    @RequestMapping(value="/getAll", method= RequestMethod.GET, produces = "application/json")
    public List<Map<String, Object>> sendMessage(@RequestParam(value = "page", required = false, defaultValue = "0") int page) {

        page = Math.max(0, page - 1);

        return messageDao.getAll(page);

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class, NumberFormatException.class})
    public Map handle(Exception e) {

        logger.error(e.getClass().getCanonicalName() + " occurred!", e);

        Map<String, String> response = new HashMap<>();
        response.put("success", "false");
        response.put("error", "Bad Request!");

        return response;

    }

}
