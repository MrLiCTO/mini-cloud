package com.slli.cloud.balance.service;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.slli.cloud.balance.model.Account;
import com.slli.cloud.balance.model.TradeRecord;
import com.slli.cloud.balance.repository.AccountRepository;
import com.slli.cloud.balance.repository.TradeRecordRepository;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.slli.cloud.common.constants.MQContants.QEUE_PAY;

/**
 * @author 李世龙
 * @create 2017-05-26 18:35
 **/
@Service
public class BalanceAccountService {
    @Autowired
    private TradeRecordRepository tradeRecordRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CuratorFramework curatorFramework;

    @Transactional(rollbackFor = Exception.class)
    public void trade(TradeRecord tradeRecord) throws Exception {
        String user = tradeRecord.getUser();
        Account account = accountRepository.findByUser(user);
        InterProcessMutex mutex = new InterProcessMutex(curatorFramework, "/mutex-" + account.getId().longValue());
        try {
            mutex.acquire();
            account = accountRepository.findByUser(user);
            double balance = account.getBalance();
            double charge = tradeRecord.getCharge();
            if (balance < charge) {
                throw new Exception("没钱了");
            }
            account.setBalance(balance - charge);
            accountRepository.save(account);
            tradeRecordRepository.save(tradeRecord);
        }catch (Exception e){
            throw new Exception();
        }finally {
            mutex.release();
        }
    }

   /* @RabbitListener(queues = QEUE_PAY)
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            byte[] body = message.getBody();
            String str = new String(body);
            TradeRecord tradeRecord = null;
            tradeRecord = JSON.parseObject(str, TradeRecord.class);

            trade(tradeRecord);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费
            System.out.println("消息消费成功，id为：" + message.getMessageProperties().getCorrelationIdString());
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            System.out.println("消息消费失败，id为：" + message.getMessageProperties().getCorrelationIdString());
        }
    }*/
}
