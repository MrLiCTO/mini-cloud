package com.slli.cloud.pay.servie;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.slli.cloud.common.utils.BeanUtils;
import com.slli.cloud.pay.model.Account;
import com.slli.cloud.pay.model.TradeRecord;
import com.slli.cloud.pay.repository.AccountRepository;
import com.slli.cloud.pay.repository.TradeRecordRepository;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

import static com.slli.cloud.common.constants.MQContants.*;

/**
 * 支付
 *
 * @author 李世龙
 * @create 2017-05-25 17:52
 **/
@Service
public class PayService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private TradeRecordRepository tradeRecordRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ConnectionFactory connectionFactory;

    @Transactional(rollbackFor = Exception.class)
    public void senderTx(TradeRecord tradeRecord) throws Exception {//事务模式
        Channel channel = connectionFactory.createConnection().createChannel(true);
        try {
            channel.exchangeDeclare(EXCHANGE_PAY, EXCHANGE_TYPE_PAY);
            channel.queueDeclare(QEUE_PAY, true, false, false, null);
            channel.queueBind(QEUE_PAY, EXCHANGE_PAY, ROUT_KEY_PAY);
            Account one = accountRepository.findOne(1L);
            double balance = one.getBalance();
            double flag = balance - tradeRecord.getCharge();
            if (flag < 0) {
                throw new Exception("没钱了");
            }/*
            channel.addConfirmListener(new ConfirmListener(){
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {

                }

                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {

                }
            });*/
            channel.txSelect();
            channel.basicPublish(EXCHANGE_PAY, ROUT_KEY_PAY, true, MessageProperties.PERSISTENT_BASIC, JSON.toJSONString(tradeRecord).getBytes());

            one.setBalance(flag);
            accountRepository.save(one);
            tradeRecordRepository.save(tradeRecord);
            //int i=1/0;
            channel.txCommit();
        } catch (Exception e) {
            try {
                channel.txRollback();
                System.out.printf("消息回滚了！");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            throw new Exception();
        }
    }

    @Autowired
    private CuratorFramework  curatorFramework;

    @Transactional(rollbackFor = Exception.class)
    public void senderCr(TradeRecord tradeRecord, Long id) throws Exception {//确认模式
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());
        final InterProcessMutex mutex = new InterProcessMutex(curatorFramework, "/mutex-" + id.longValue());
        try {
            mutex.acquire();//基于zookeeper的分布式锁
            Account one = accountRepository.findOne(id);
            double balance = one.getBalance();
            double flag = balance - tradeRecord.getCharge();
            if (flag < 0) {
                throw new Exception("没钱了");
            }
            one.setBalance(flag);
            accountRepository.save(one);
            tradeRecordRepository.save(tradeRecord);
        } catch (Exception e) {
            throw new Exception();
        } finally {
            mutex.release();//释放锁
        }
        TradeRecord tr = new TradeRecord();
        BeanUtils.copyProperties(tradeRecord,tr);
        tr.setId(null);
        //rabbitTemplate.convertAndSend(ROUT_KEY_PAY,tradeRecord,correlationData);
        Message message = MessageBuilder
                .withBody(JSON.toJSONString(tradeRecord).getBytes())
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .setCorrelationIdString(correlationData.getId())
                .build();

        rabbitTemplate.setBeforePublishPostProcessors(new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                String id = correlationData.getId();
                message.getMessageProperties().setHeader("id", id);
                return message;
            }
        });
        rabbitTemplate.convertAndSend(ROUT_KEY_PAY, tr, correlationData);

    }
    /*@RabbitListener(queues=QEUE_PAY)
    public void onMessage(Message message){
        String s = new String(message.getBody());
        System.out.println("-----------------------------------------------------------------------"+s);
    }*/
}
