package com.slli.cloud.common.model;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Mr_Li on 2017/5/21.
 */
@Data
@Entity
@Table(name = "operate_money")
public class OperateMoney {
    private Long id;//主键id
    private String user;//用户
    private String tradeNo;//交易号
    private double money;//金钱
    private String description;//描述
    private int flag;//0:扣钱,1:充钱,
}
