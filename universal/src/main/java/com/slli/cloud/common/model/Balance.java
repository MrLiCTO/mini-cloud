package com.slli.cloud.common.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Mr_Li on 2017/5/21.
 */
@Data
@Entity
@Table(name = "balance")
public class Balance {
    private Long id;//主键id
    private String user;//用户
    private double balance;//金钱
}
