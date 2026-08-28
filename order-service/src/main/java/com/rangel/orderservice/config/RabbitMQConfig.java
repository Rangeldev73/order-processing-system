package com.rangel.orderservice.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.StatelessRetryOperationsInterceptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EVENTS_EXCHANGE = "order.events";
    public static final String PAYMENT_EVENTS_EXCHANGE = "payment.events";
    public static final String STOCK_EVENTS_EXCHANGE = "stock.events";
    public static final String DLX_EXCHANGE = "order.dlx";

    public static final String PAYMENT_APPROVED_QUEUE = "order-service.payment-approved-queue";
    public static final String PAYMENT_REJECTED_QUEUE = "order-service.payment-rejected-queue";
    public static final String STOCK_RESERVED_QUEUE = "order-service.stock-reserved-queue";
    public static final String STOCK_FAILED_QUEUE = "order-service.stock-failed-queue";
    public static final String PAYMENT_APPROVED_DLQ = PAYMENT_APPROVED_QUEUE + ".dlq";
    public static final String PAYMENT_REJECTED_DLQ = PAYMENT_REJECTED_QUEUE + ".dlq";
    public static final String STOCK_RESERVED_DLQ = STOCK_RESERVED_QUEUE + ".dlq";
    public static final String STOCK_FAILED_DLQ = STOCK_FAILED_QUEUE + ".dlq";

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

    @Bean
    public TopicExchange orderDlxExchange() {
        return new TopicExchange(DLX_EXCHANGE);
    }

    @Bean
    public Queue paymentApprovedDlq() { return new Queue(PAYMENT_APPROVED_DLQ, true); }

    @Bean
    public Queue paymentRejectedDlq() { return new Queue(PAYMENT_REJECTED_DLQ, true); }

    @Bean
    public Queue stockReservedDlq() { return new Queue(STOCK_RESERVED_DLQ, true); }

    @Bean
    public Queue stockFailedDlq() { return new Queue(STOCK_FAILED_DLQ, true); }

    @Bean
    public Binding paymentApprovedDlqBinding(Queue paymentApprovedDlq, TopicExchange orderDlxExchange) {
        return BindingBuilder.bind(paymentApprovedDlq).to(orderDlxExchange).with(PAYMENT_APPROVED_DLQ);
    }

    @Bean
    public Binding paymentRejectedDlqBinding(Queue paymentRejectedDlq, TopicExchange orderDlxExchange) {
        return BindingBuilder.bind(paymentRejectedDlq).to(orderDlxExchange).with(PAYMENT_REJECTED_DLQ);
    }

    @Bean
    public Binding stockReservedDlqBinding(Queue stockReservedDlq, TopicExchange orderDlxExchange) {
        return BindingBuilder.bind(stockReservedDlq).to(orderDlxExchange).with(STOCK_RESERVED_DLQ);
    }

    @Bean
    public Binding stockFailedDlqBinding(Queue stockFailedDlq, TopicExchange orderDlxExchange) {
        return BindingBuilder.bind(stockFailedDlq).to(orderDlxExchange).with(STOCK_FAILED_DLQ);
    }

    private SimpleRabbitListenerContainerFactory buildRetryableFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter,
            RabbitTemplate rabbitTemplate,
            String dlqRoutingKey) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);

        StatelessRetryOperationsInterceptor retryInterceptor = RetryInterceptorBuilder.stateless()
                .maxRetries(3)
                .backOffOptions(500, 2.0, 5000)
                .recoverer(new RepublishMessageRecoverer(rabbitTemplate, DLX_EXCHANGE, dlqRoutingKey))
                .build();

        factory.setAdviceChain(retryInterceptor);
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory paymentApprovedContainerFactory(
            ConnectionFactory cf, MessageConverter mc, RabbitTemplate rt) {
        return buildRetryableFactory(cf, mc, rt, PAYMENT_APPROVED_DLQ);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory paymentRejectedContainerFactory(
            ConnectionFactory cf, MessageConverter mc, RabbitTemplate rt) {
        return buildRetryableFactory(cf, mc, rt, PAYMENT_REJECTED_DLQ);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory stockReservedContainerFactory(
            ConnectionFactory cf, MessageConverter mc, RabbitTemplate rt) {
        return buildRetryableFactory(cf, mc, rt, STOCK_RESERVED_DLQ);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory stockFailedContainerFactory(
            ConnectionFactory cf, MessageConverter mc, RabbitTemplate rt) {
        return buildRetryableFactory(cf, mc, rt, STOCK_FAILED_DLQ);
    }
}