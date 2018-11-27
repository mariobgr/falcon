package com.mariobgr.falcon.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableAutoConfiguration
public class RabbitConfig {

    @Value("${spring.rabbit.topic}")
    private String rabbitTopic;

    @Value("${spring.rabbit.queue}")
    private String rabbitQueue;

    @Value("${spring.rabbit.key}")
    private String rabbitKey;



    @Bean
    public TopicExchange exchangeTopic() {
        return new TopicExchange(rabbitTopic);
    }

    @Bean
    public Queue exchangeQueue() {
        return new Queue(rabbitQueue);
    }

    @Bean
    public Binding exchangeBinding() {
        return BindingBuilder.bind(exchangeQueue()).to(exchangeTopic()).with(rabbitKey);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
