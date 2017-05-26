package com.slli.cloud.pay;

import com.slli.cloud.pay.model.TradeRecord;
import com.slli.cloud.pay.repository.AccountRepository;
import com.slli.cloud.pay.servie.PayService;
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
        TradeRecord tradeRecord = new TradeRecord();
        tradeRecord.setUser("user");
        tradeRecord.setCharge(400);
        tradeRecord.setDescription("交易");
        tradeRecord.setTradeNo(System.currentTimeMillis()+"");
        tradeRecord.setFlag(1);
        payService.senderTx(tradeRecord);
        Thread.sleep(10000);
    }

}
