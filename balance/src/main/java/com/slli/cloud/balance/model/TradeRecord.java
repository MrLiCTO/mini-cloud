package com.slli.cloud.balance.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Mr_Li on 2017/5/21.
 */
@Data
@Entity
@Table(name = "order")
public class TradeRecord {
    @Id
    @Column(unique = true, nullable = false)
    private Long id;//主键id
    private String user;//用户
    private String tradeNo;//交易号
    private double charge;//金钱
    private String description;//描述
    private int flag;
}
