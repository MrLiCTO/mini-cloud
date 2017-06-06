package com.slli.cloud.pay.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.slli.cloud.common.constants.MQContants.EXCHANGE_PAY;
import static com.slli.cloud.common.constants.MQContants.QEUE_PAY;
import static com.slli.cloud.common.constants.MQContants.ROUT_KEY_PAY;

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
}
