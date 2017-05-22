package com.slli.cloud.balance.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
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
    public SimpleMessageListenerContainer messageContainer(CachingConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(payOne());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
        container.setMessageListener(new ChannelAwareMessageListener() {
            public void onMessage(Message message, Channel channel) throws Exception {
                byte[] body = message.getBody();
                System.out.println("收到消息 : " + new String(body));
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费
            }

        });
        return container;
    }


}
