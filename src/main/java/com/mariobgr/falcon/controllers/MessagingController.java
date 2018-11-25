package com.mariobgr.falcon.controllers;

import com.mariobgr.falcon.models.MessageModel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class MessagingController {

    @MessageMapping("/message")
    @SendTo("/topic/falcon")
    public MessageModel send(@RequestBody MessageModel messageBody) throws Exception {

        return messageBody;
    }

}