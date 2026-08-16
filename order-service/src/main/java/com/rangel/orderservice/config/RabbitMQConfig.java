package com.rangel.orderservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EVENTS_EXCHANGE = "order.events";
    public static final String PAYMENT_EVENTS_EXCHANGE = "payment.events";
    public static final String PAYMENT_APPROVED_QUEUE = "order-service.payment-approved-queue";
    public static final String PAYMENT_REJECTED_QUEUE = "order-service.payment-rejected-queue";

    @Bean
    public TopicExchange orderEventsExchange() {
        return new TopicExchange(ORDER_EVENTS_EXCHANGE);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public TopicExchange paymentEventsExchange() {
        return new TopicExchange(PAYMENT_EVENTS_EXCHANGE);
    }

    @Bean
    public Queue paymentApprovedQueue() {
        return new Queue(PAYMENT_APPROVED_QUEUE, true);
    }

    @Bean
    public Queue paymentRejectedQueue() {
        return new Queue(PAYMENT_REJECTED_QUEUE, true);
    }

    @Bean
    public Binding paymentApprovedBinding(Queue paymentApprovedQueue, TopicExchange paymentEventsExchange) {
        return BindingBuilder.bind(paymentApprovedQueue).to(paymentEventsExchange).with("payment.approved");
    }

    @Bean
    public Binding paymentRejectedBinding(Queue paymentRejectedQueue, TopicExchange paymentEventsExchange) {
        return BindingBuilder.bind(paymentRejectedQueue).to(paymentEventsExchange).with("payment.rejected");
    }
}