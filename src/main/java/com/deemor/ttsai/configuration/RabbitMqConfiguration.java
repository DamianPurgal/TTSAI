package com.deemor.ttsai.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    public static final String REQUEST_TO_ALERT_QUEUE_NAME = "requestToAlertQueue";

    @Bean
    public Queue myQueue() {
        return new Queue(REQUEST_TO_ALERT_QUEUE_NAME, true);
    }

}
