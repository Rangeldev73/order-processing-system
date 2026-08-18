package com.rangel.notificationservice.infrastructure.messaging;

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
    public static final String NOTIFICATION_ORDER_CREATED_QUEUE = "notification-service.order-created.queue";
    public static final String NOTIFICATION_PAYMENT_APPROVED_QUEUE = "notification-service.payment-approved.queue";
    public static final String NOTIFICATION_PAYMENT_REJECTED_QUEUE = "notification-service.payment-rejected.queue";

    @Bean
    public TopicExchange orderEventsExchange() {
        return new TopicExchange(ORDER_EVENTS_EXCHANGE);
    }

    @Bean
    public TopicExchange paymentEventsExchange() {
        return new TopicExchange(PAYMENT_EVENTS_EXCHANGE);
    }

    @Bean
    public Queue notificationOrderCreatedQueue() {
        return new Queue(NOTIFICATION_ORDER_CREATED_QUEUE, true);
    }

    @Bean
    public Queue notificationPaymentApprovedQueue() {
        return new Queue(NOTIFICATION_PAYMENT_APPROVED_QUEUE, true);
    }

    @Bean
    public Queue notificationPaymentRejectedQueue() {
        return new Queue(NOTIFICATION_PAYMENT_REJECTED_QUEUE, true);
    }

    @Bean
    public Binding notificationOrderCreatedBinding(Queue notificationOrderCreatedQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder.bind(notificationOrderCreatedQueue).to(orderEventsExchange).with("order.created");
    }

    @Bean
    public Binding notificationPaymentApprovedBinding(Queue notificationPaymentApprovedQueue, TopicExchange paymentEventsExchange) {
        return BindingBuilder.bind(notificationPaymentApprovedQueue).to(paymentEventsExchange).with("payment.approved");
    }

    @Bean
    public Binding notificationPaymentRejectedBinding(Queue notificationPaymentRejectedQueue, TopicExchange paymentEventsExchange) {
        return BindingBuilder.bind(notificationPaymentRejectedQueue).to(paymentEventsExchange).with("payment.rejected");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}