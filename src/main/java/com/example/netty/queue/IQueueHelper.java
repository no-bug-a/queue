package com.example.netty.queue;


import com.example.netty.entity.QueueBaseEntity;
import com.example.netty.enums.QueueTypeEnum;
import com.sun.istack.internal.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangyi
 * @version 1.0
 * @date 2022/1/19 14:15
 */
public interface IQueueHelper {
    String DELAY_KEY_PREFIX = "INFO_PUBLISH_DELAY:";
    String PRIORITY_KEY_PREFIX = "INFO_PUBLISH_PRIORITY:";

    void add(QueueTypeEnum queueType, QueueBaseEntity entity);

    void remove(QueueTypeEnum queueType, Collection<? extends QueueBaseEntity> collection);

    void remove(QueueTypeEnum queueType, QueueBaseEntity entity);

    void remove(QueueTypeEnum queueType, String deviceId, String salt);

    void remove(QueueTypeEnum queueType, String deviceId, Integer dbId);

    /**
     * 获取延迟队列中每个设备过期的节点
     */
    Map<String, Set<QueueBaseEntity>> getExpired();

    /**
     * 获取优先级队列中每个设备头部节点
     */
    List<QueueBaseEntity> allDeviceFirstNode();

    /**
     * 获取延迟队列偏移时间 (最大时间 - 当前时间)
     */
    int getOffset(String deviceId);

    default int getCurrentSecond(){
        return (int)(System.currentTimeMillis() / 1000);
    }
    default String getKey(QueueTypeEnum queueType, QueueBaseEntity entity){
        return queueType == QueueTypeEnum.DELAY ? DELAY_KEY_PREFIX + entity.getDeviceId()
                : PRIORITY_KEY_PREFIX + entity.getDeviceId();
    }
    default String getKey(QueueTypeEnum queueType, String deviceId){
        return queueType == QueueTypeEnum.DELAY ? DELAY_KEY_PREFIX + deviceId
                : PRIORITY_KEY_PREFIX + deviceId;
    }
}
