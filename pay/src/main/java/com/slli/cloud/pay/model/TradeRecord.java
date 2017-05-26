package com.slli.cloud.pay.model;

import javax.persistence.*;

/**
 * Created by Mr_Li on 2017/5/21.
 */
@Entity
@Table(name = "trade_record")
public class TradeRecord {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;//主键id
    private String user;//用户
    private String tradeNo;//交易号
    private double charge;//金钱
    private String description;//描述
    private int flag;

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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
