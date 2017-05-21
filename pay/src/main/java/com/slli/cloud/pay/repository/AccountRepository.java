package com.slli.cloud.pay.repository;

import com.slli.cloud.pay.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Mr_Li on 2017/5/21.
 */
public interface AccountRepository extends JpaRepository<Account,Long> {
}
