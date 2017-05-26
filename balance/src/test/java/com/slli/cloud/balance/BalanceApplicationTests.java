package com.slli.cloud.balance;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.slli.cloud.balance.model.Account;
import com.slli.cloud.balance.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BalanceApplicationTests {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    //http://www.tuicool.com/articles/AvUnE3J
    //http://blog.csdn.net/mra__s__/article/details/55011530
    public void contextLoads() {
        /*for(int i=0;i<1000;i++){
            rabbitTemplate.convertAndSend("pay","hello world!"+i);
			rabbitTemplate.convertSendAndReceive("pay","hello world!"+i);
		}*/
        Channel channel = rabbitTemplate.getConnectionFactory().createConnection().createChannel(false);
        //channel.exchangeDeclare();
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {

            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {

            }
        });
        //channel.basicPublish();
    }

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void tes() {
        //rabbitTemplate.convertAndSend("pay","abcdefg");
        Account account = new Account();
        account.setBalance(1000);
        account.setUser("user");
        accountRepository.save(account);

    }

}
