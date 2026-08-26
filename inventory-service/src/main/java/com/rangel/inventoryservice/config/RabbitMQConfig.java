package com.rangel.inventoryservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.config.StatelessRetryOperationsInterceptor;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EVENTS_EXCHANGE = "order.events";
    public static final String STOCK_EVENTS_EXCHANGE = "stock.events";
    public static final String DLX_EXCHANGE = "inventory.dlx";

    public static final String ORDER_CREATED_QUEUE = "inventory-service.order-created.queue";
    public static final String ORDER_CREATED_DLQ = ORDER_CREATED_QUEUE + ".dlq";

    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";
    public static final String ORDER_CREATED_DLQ_ROUTING_KEY = "order.created.dlq";

    @Bean
    public TopicExchange orderEventsExchange() {
        return new TopicExchange(ORDER_EVENTS_EXCHANGE);
    }

    @Bean
    public TopicExchange stockEventsExchange() {
        return new TopicExchange(STOCK_EVENTS_EXCHANGE);
    }

    @Bean
    public TopicExchange inventoryDlxExchange() {
        return new TopicExchange(DLX_EXCHANGE);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE, true);
    }

    @Bean
    public Queue orderCreatedDlq() {
        return new Queue(ORDER_CREATED_DLQ, true);
    }

    @Bean
    public Binding orderCreatedBinding(Queue orderCreatedQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder
                .bind(orderCreatedQueue)
                .to(orderEventsExchange)
                .with(ORDER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding orderCreatedDlqBinding(Queue orderCreatedDlq, TopicExchange inventoryDlxExchange) {
        return BindingBuilder
                .bind(orderCreatedDlq)
                .to(inventoryDlxExchange)
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
                .maxRetries(3)
                .backOffOptions(500, 2.0, 5000)
                .recoverer(new RepublishMessageRecoverer(rabbitTemplate, DLX_EXCHANGE, ORDER_CREATED_DLQ_ROUTING_KEY))
                .build();

        factory.setAdviceChain(retryInterceptor);
        return factory;
    }
}