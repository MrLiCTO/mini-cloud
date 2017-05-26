package com.slli.cloud.balance.repository;

import com.slli.cloud.balance.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Mr_Li on 2017/5/21.
 */
public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByUser(String user);
}
