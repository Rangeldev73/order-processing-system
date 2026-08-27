package com.rangel.paymentservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
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
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig {

    public static final String PAYMENT_EVENTS_EXCHANGE = "payment.events";
    public static final String EXCHANGE_NAME = "order.events";
    public static final String QUEUE_NAME = "payment-service.order-created-queue";
    public static final String ROUTING_KEY = "order.created";

    public static final String DLX_EXCHANGE = "payment.dlx";
    public static final String ORDER_CREATED_DLQ = QUEUE_NAME + ".dlq";
    public static final String ORDER_CREATED_DLQ_ROUTING_KEY = "order.created.dlq";

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Queue orderCreatedDlq() {
        return new Queue(ORDER_CREATED_DLQ, true);
    }

    @Bean
    public TopicExchange orderEventsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public TopicExchange paymentEventsExchange() {
        return new TopicExchange(PAYMENT_EVENTS_EXCHANGE);
    }

    @Bean
    public TopicExchange paymentDlxExchange() {
        return new TopicExchange(DLX_EXCHANGE);
    }

    @Bean
    public Binding orderCreatedBinding(Queue orderCreatedQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder
                .bind(orderCreatedQueue)
                .to(orderEventsExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding orderCreatedDlqBinding(Queue orderCreatedDlq, TopicExchange paymentDlxExchange) {
        return BindingBuilder
                .bind(orderCreatedDlq)
                .to(paymentDlxExchange)
                .with(ORDER_CREATED_DLQ_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter,
            RabbitTemplate rabbitTemplate) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);

        StatelessRetryOperationsInterceptor retryInterceptor = RetryInterceptorBuilder.stateless()
                .maxRetries(5)
                .backOffOptions(1000, 2.0, 10000)
                .recoverer(new RepublishMessageRecoverer(rabbitTemplate, DLX_EXCHANGE, ORDER_CREATED_DLQ_ROUTING_KEY))
                .build();

        factory.setAdviceChain(retryInterceptor);
        return factory;
    }
}