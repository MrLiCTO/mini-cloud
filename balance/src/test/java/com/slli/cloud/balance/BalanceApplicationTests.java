package com.slli.cloud.balance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BalanceApplicationTests {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Test
	//http://www.tuicool.com/articles/AvUnE3J
	//http://blog.csdn.net/mra__s__/article/details/55011530
	public void contextLoads() {
		for(int i=0;i<1000;i++){
			rabbitTemplate.convertAndSend("pay","hello world!"+i);
			rabbitTemplate.convertSendAndReceive("pay","hello world!"+i);
		}
	}

}
