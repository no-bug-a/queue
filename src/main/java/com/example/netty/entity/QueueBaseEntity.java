package com.example.netty.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zhangyi
 * @version 1.0
 * @date 2022/1/19 10:27
 */
@ToString
@Data
public class QueueBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String deviceId;
    private String salt;
    private String name;
    private Integer dbId;
    private Integer delay;
    private Integer priority;
    /**
     * 1 开始
     * 0 结束
     */
    private Integer type;
}
