package com.slli.cloud.pay;

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

	@Test
	//http://www.tuicool.com/articles/AvUnE3J
	//http://blog.csdn.net/mra__s__/article/details/55011530
	public void contextLoads() {
		rabbitTemplate.convertAndSend("pay","abcdefg");
	}

}
