package com.slli.cloud.pay.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by Mr_Li on 2017/5/21.
 */
@Data
@Entity
@Table(name = "account")
public class Account {
    @Id
    @Column(unique = true, nullable = false)
    private Long id;//主键id
    private String user;//用户
    private double balance;//金钱
}
