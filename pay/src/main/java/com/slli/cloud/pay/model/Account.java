package com.slli.cloud.pay.model;

import javax.persistence.*;

/**
 * Created by Mr_Li on 2017/5/21.
 */
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;//主键id
    private String user;//用户
    private double balance;//金钱

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
