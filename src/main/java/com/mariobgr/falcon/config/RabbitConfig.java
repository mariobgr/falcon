package com.mariobgr.falcon.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableAutoConfiguration
public class RabbitConfig {

    public static final String EXCHANGE_TOPIC_NAME = "${spring.rabbit.topic}";
    public static final String EXCHANGE_QUEUE_NAME = "${spring.rabbit.queue}";
    public static final String EXCHANGE_KEY_NAME = "${spring.rabbit.key}";


    @Bean
    public TopicExchange exchangeTopic() {
        return new TopicExchange(EXCHANGE_TOPIC_NAME);
    }

    @Bean
    public Queue exchangeQueue() {
        return new Queue(EXCHANGE_QUEUE_NAME);
    }

    @Bean
    public Binding exchangeBinding() {
        return BindingBuilder.bind(exchangeQueue()).to(exchangeTopic()).with(EXCHANGE_KEY_NAME);
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
