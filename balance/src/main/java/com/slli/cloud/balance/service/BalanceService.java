package com.slli.cloud.balance.service;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.slli.cloud.balance.model.TradeRecord;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;

/**
 * 余额
 *
 * @author 李世龙
 * @create 2017-05-24 19:07
 **/
public class BalanceService {
    @Bean
    public Queue payOne() {
        return new Queue("pay", true);
    }
    @Bean
    public SimpleMessageListenerContainer messageContainer(CachingConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(payOne());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setRetryDeclarationInterval(1000);
        container.setDeclarationRetries(10);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
        container.setMessageListener(new ChannelAwareMessageListener() {
            public void onMessage(Message message, Channel channel) throws Exception {
                byte[] body = message.getBody();
                String str = new String(body);
                TradeRecord tradeRecord = JSON.parseObject(str, TradeRecord.class);
                try{

                }catch (Exception e){

                }
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);

                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费

            }

        });
        return container;
    }
}
