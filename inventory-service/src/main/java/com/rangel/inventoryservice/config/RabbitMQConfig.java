package com.rangel.inventoryservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EVENTS_EXCHANGE = "order.events";
    public static final String STOCK_EVENTS_EXCHANGE = "stock.events";
    public static final String ORDER_CREATED_QUEUE = "inventory-service.order-created.queue";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    @Bean
    public TopicExchange orderEventsExchange() {
        return new TopicExchange(ORDER_EVENTS_EXCHANGE);
    }

    @Bean
    public TopicExchange stockEventsExchange() {
        return new TopicExchange(STOCK_EVENTS_EXCHANGE);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE, true);
    }

    @Bean
    public Binding orderCreatedBinding(Queue orderCreatedQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder
                .bind(orderCreatedQueue)
                .to(orderEventsExchange)
                .with(ORDER_CREATED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}