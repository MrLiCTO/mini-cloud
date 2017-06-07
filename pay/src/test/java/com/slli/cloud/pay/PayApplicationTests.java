package com.slli.cloud.pay;

import com.slli.cloud.pay.model.TradeRecord;
import com.slli.cloud.pay.repository.AccountRepository;
import com.slli.cloud.pay.servie.PayService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PayApplicationTests {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private AccountRepository accountRepository;

    @Autowired
    private PayService payService;

	@Test
	//http://www.tuicool.com/articles/AvUnE3J
	//http://blog.csdn.net/mra__s__/article/details/55011530
	public void contextLoads() throws Exception {
		//rabbitTemplate.convertAndSend("pay","abcdefg");
		/*Account account = new Account();
		account.setBalance(1000);
		account.setUser("user");
		accountRepository.save(account);*/
        while (true){
            TradeRecord tradeRecord = new TradeRecord();
            tradeRecord.setUser("user");
            tradeRecord.setCharge(400);
            tradeRecord.setDescription("交易");
            tradeRecord.setTradeNo(System.currentTimeMillis()+"");
            tradeRecord.setFlag(1);
            //payService.senderTx(tradeRecord);
            payService.senderCr(tradeRecord,1L);
            Thread.sleep(1000);
        }
    }
    @Autowired
    private CuratorFramework curatorFramework;
    @Test
	public void testChannel() throws Exception {
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/super/c1","c1内容".getBytes());
        /*rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause)->{
            System.out.println(" 回调id:" + correlationData);
            if (ack) {
                System.out.println("消息成功消费");
            } else {
                System.out.println("消息消费失败:" + cause);
            }
        });
        TradeRecord tradeRecord = new TradeRecord();
        tradeRecord.setUser("user");
        tradeRecord.setCharge(400);
        tradeRecord.setDescription("交易");
        tradeRecord.setTradeNo(System.currentTimeMillis()+"");
        tradeRecord.setFlag(1);
        rabbitTemplate.convertAndSend(MQContants.ROUT_KEY_PAY,tradeRecord);

        rabbitTemplate.setRecoveryCallback(new RecoveryCallback<TradeRecord>(){
            @Override
            public TradeRecord recover(RetryContext retryContext) throws Exception {
                return null;
            }
        });
        rabbitTemplate.setAfterReceivePostProcessors(new MessagePostProcessor(){
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                return null;
            }
        });
        rabbitTemplate.receiveAndReply(new ReceiveAndReplyCallback<TradeRecord,Object>(){
            @Override
            public Object handle(TradeRecord payload) {
                return null;
            }
        });*/
    }

}
