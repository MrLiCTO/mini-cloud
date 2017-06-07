package com.slli.cloud.balance.service;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.slli.cloud.balance.model.TradeRecord;
import com.slli.cloud.balance.repository.AccountRepository;
import com.slli.cloud.balance.repository.TradeRecordRepository;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.slli.cloud.common.constants.MQContants.QEUE_PAY;

/**
 * 余额
 *
 * @author 李世龙
 * @create 2017-05-24 19:07
 **/
@Configuration
public class BalanceService {
    @Bean
    public Queue payOne() {
        return new Queue(QEUE_PAY, true);
    }

    @Bean
    public SimpleMessageListenerContainer messageContainer(CachingConnectionFactory connectionFactory, BalanceAccountService balanceAccountService) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(payOne());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setRetryDeclarationInterval(1000);
        container.setDeclarationRetries(10);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
        //container.setAcknowledgeMode(AcknowledgeMode.AUTO); //设置确认模式自动确认
        container.setMessageListener(new ChannelAwareMessageListener() {
            public void onMessage(Message message, Channel channel) throws Exception {

                try {
                    byte[] body = message.getBody();
                    String str = new String(body);
                    TradeRecord tradeRecord = null;
                    tradeRecord = JSON.parseObject(str, TradeRecord.class);

                    balanceAccountService.trade(tradeRecord);
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费
                    System.out.println("消息消费成功，id为："+message.getMessageProperties().getHeaders().get("id"));
                } catch (Exception e) {
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                    System.out.println("消息消费失败，id为：" + message.getMessageProperties().getHeaders().get("id"));
                }


            }

        });
        return container;
    }
}
