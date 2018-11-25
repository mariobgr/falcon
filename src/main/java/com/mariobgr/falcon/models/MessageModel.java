package com.mariobgr.falcon.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageModel implements Serializable {

    public Integer id;
    public String message;
    public int random;
    public String timestamp;

    public MessageModel(@JsonProperty("message") String message,
                        @JsonProperty("random") int random,
                        @JsonProperty("timestamp") String timestamp,
                        @JsonProperty("id") Integer id) {
        this.id = id;
        this.message = message;
        this.random = random;
        this.timestamp = timestamp;
    }

    public Integer getId() { return id; }

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
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", random=" + random +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

}
