package com.mariobgr.falcon.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class MessageModel implements Serializable {

    public String message;
    public int random;
    public String timestamp;

    public MessageModel(@JsonProperty("message") String message,
                        @JsonProperty("random") int random,
                        @JsonProperty("timestamp") String timestamp) {
        this.message = message;
        this.random = random;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public int getRandom() {
        return random;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "message='" + message + '\'' +
                ", random=" + random +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

}
