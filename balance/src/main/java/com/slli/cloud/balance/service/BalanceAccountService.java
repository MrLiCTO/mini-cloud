package com.slli.cloud.balance.service;

import com.slli.cloud.balance.model.Account;
import com.slli.cloud.balance.model.TradeRecord;
import com.slli.cloud.balance.repository.AccountRepository;
import com.slli.cloud.balance.repository.TradeRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void trade(TradeRecord tradeRecord) throws Exception {
        String user = tradeRecord.getUser();
        Account account = accountRepository.findByUser(user);
        double balance = account.getBalance();
        double charge = tradeRecord.getCharge();
        if(balance<charge){
            throw new Exception("没钱了");
        }
        account.setBalance(balance-charge);
        accountRepository.save(account);
        tradeRecordRepository.save(tradeRecord);
    }
}
