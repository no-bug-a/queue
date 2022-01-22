package com.example.netty.queue;

import com.example.netty.entity.QueueBaseEntity;
import com.example.netty.enums.QueueTypeEnum;
import com.sun.istack.internal.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author zhangyi
 * @version 1.0
 * @date 2022/1/19 10:02
 */
@Component
public class DelayQueue {

    private static final QueueTypeEnum QUEUE_TYPE = QueueTypeEnum.DELAY;

    @Autowired
    private IQueueHelper queueHelper;

    public void add(QueueBaseEntity entity) {
        QueueBaseEntity endEntity = copyAndModifyEntity(entity);
        queueHelper.add(QUEUE_TYPE, entity);
        queueHelper.add(QUEUE_TYPE, endEntity);
    }

    public void remove(QueueBaseEntity entity) {
        queueHelper.remove(QUEUE_TYPE, entity);
    }

    public void remove(String deviceId, String salt) {
        queueHelper.remove(QUEUE_TYPE, deviceId, salt);
    }

    public void remove(String deviceId, Integer dbId) {
        queueHelper.remove(QUEUE_TYPE, deviceId, dbId);
    }

    public Map<String, Set<QueueBaseEntity>> getExpired() {
        return queueHelper.getExpired();
    }

    private QueueBaseEntity copyAndModifyEntity(QueueBaseEntity entity) {
        QueueBaseEntity endEntity = new QueueBaseEntity();
        endEntity.setDbId(entity.getDbId());
        endEntity.setSalt(entity.getSalt());
        endEntity.setDeviceId(entity.getDeviceId());
        endEntity.setDelay(entity.getDelay());
        int offset = queueHelper.getOffset(entity.getDeviceId());
        entity.setType(1);
        entity.setDelay(offset);
        endEntity.setType(0);
        endEntity.setDelay(offset + endEntity.getDelay());
        return endEntity;
    }
}
