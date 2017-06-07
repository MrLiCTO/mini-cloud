package com.slli.cloud.pay.config;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.slli.cloud.common.constants.MQContants.*;

/**
 * RabbitMQ配置
 *
 * @author 李世龙
 * @create 2017-05-22 18:07
 **/
@Component
public class RabbitMQConfig {
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(EXCHANGE_PAY);
    }
    @Bean
    public Queue queue() {
        return new Queue(QEUE_PAY, true); //队列持久

    }
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(defaultExchange()).with(ROUT_KEY_PAY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        template.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause)->{
            System.out.println("---------------------------------------回调id:" + correlationData);
            if (ack) {
                System.out.println("-----------------------------------消息成功消费");
            } else {
                System.out.println("------------------------------------消息消费失败:" + cause);
            }
        });
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
}
