package com.slli.cloud.pay.servie;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.slli.cloud.pay.model.TradeRecord;
import com.slli.cloud.pay.repository.TradeRecordRepository;
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
    @Transactional(rollbackFor = Exception.class)
    public void senderTx(TradeRecord tradeRecord) throws Exception {//事务模式
        Channel channel = rabbitTemplate.getConnectionFactory().createConnection().createChannel(true);
        try {
            channel.exchangeDeclare(EXCHANGE_PAY,EXCHANGE_TYPE_PAY);
            channel.queueDeclare(QEUE_PAY,true, false, false, null);
            channel.queueBind(QEUE_PAY,EXCHANGE_PAY,ROUT_KEY_PAY);
            tradeRecordRepository.save(tradeRecord);
            channel.txSelect();
            channel.basicPublish(EXCHANGE_PAY,ROUT_KEY_PAY,true, MessageProperties.PERSISTENT_BASIC, JSON.toJSONString(tradeRecord).getBytes());

            channel.txCommit();
        } catch (IOException e) {
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
