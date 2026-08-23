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
    public static final String STOCK_EVENTS_EXCHANGE = "stock.events";

    public static final String PAYMENT_APPROVED_QUEUE = "order-service.payment-approved-queue";
    public static final String PAYMENT_REJECTED_QUEUE = "order-service.payment-rejected-queue";
    public static final String STOCK_RESERVED_QUEUE = "order-service.stock-reserved-queue";
    public static final String STOCK_FAILED_QUEUE = "order-service.stock-failed-queue";

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

    @Bean
    public TopicExchange stockEventsExchange() {
        return new TopicExchange(STOCK_EVENTS_EXCHANGE);
    }

    @Bean
    public Queue stockReservedQueue() {
        return new Queue(STOCK_RESERVED_QUEUE, true);
    }

    @Bean
    public Queue stockFailedQueue() {
        return new Queue(STOCK_FAILED_QUEUE, true);
    }

    @Bean
    public Binding stockReservedBinding(Queue stockReservedQueue, TopicExchange stockEventsExchange) {
        return BindingBuilder.bind(stockReservedQueue).to(stockEventsExchange).with("stock.reserved");
    }

    @Bean
    public Binding stockFailedBinding(Queue stockFailedQueue, TopicExchange stockEventsExchange) {
        return BindingBuilder.bind(stockFailedQueue).to(stockEventsExchange).with("stock.reservation-failed");
    }
}