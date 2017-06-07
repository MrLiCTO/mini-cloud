package com.slli.cloud.balance.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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
    /*private static String EXCHANGE="pay_exchange";

    private static String QUEUE_KEY_PAY="pay_one_key";

    private static String QUEUE_KEY_BALANCE="balance_key";
    //private static String QUEUE_KEY_BALANCE_1="balance_one_key";

    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }*/

    @Bean
    public Queue payOne() {
        return new Queue("pay", true);
    }

//    @RabbitListener(queues = "pay")
//    public void payListner(Message message, Channel channel)throws Exception{
//        System.out.println("收到消息 : " + new String(message.getBody()));
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费
//    }

    /*@Bean
    public Queue balance() {
        return new Queue("balance", true);
    }*/

    /*@Bean
    public Binding bindingPay() {
        return BindingBuilder.bind(payOne()).to(defaultExchange()).with(RabbitMQConfig.QUEUE_KEY_PAY);
    }*/

    /*@Bean
    public Binding bindingBalance() {
        return BindingBuilder.bind(balance()).to(defaultExchange()).with(RabbitMQConfig.QUEUE_KEY_BALANCE);
    }*/

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
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
