package com.slli.cloud.pay.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ配置
 *
 * @author 李世龙
 * @create 2017-05-22 18:07
 **/
@Component
public class RabbitMQConfig {
    @Bean
    public Queue payOne() {
        return new Queue("pay", true);
    }
}
