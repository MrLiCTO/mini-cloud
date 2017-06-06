package com.slli.cloud.pay.servie;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.slli.cloud.pay.model.Account;
import com.slli.cloud.pay.model.TradeRecord;
import com.slli.cloud.pay.repository.AccountRepository;
import com.slli.cloud.pay.repository.TradeRecordRepository;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

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
            }
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

}
