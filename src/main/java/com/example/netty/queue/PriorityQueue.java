package com.example.netty.queue;

import com.example.netty.entity.QueueBaseEntity;
import com.example.netty.enums.QueueTypeEnum;
import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangyi
 * @version 1.0
 * @date 2022/1/19 11:52
 */
@Component
public class PriorityQueue {

    private static final QueueTypeEnum QUEUE_TYPE = QueueTypeEnum.PRIORITY;

    @Autowired
    private IQueueHelper queueHelper;

    public void add(QueueBaseEntity entity) {
        queueHelper.add(QUEUE_TYPE, entity);
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

    public void update(QueueBaseEntity entity) {
        queueHelper.remove(QUEUE_TYPE, entity.getDeviceId(), entity.getSalt());
        queueHelper.add(QUEUE_TYPE, entity);
    }

    public List<QueueBaseEntity> allDeviceFirstNode(){
       return queueHelper.allDeviceFirstNode();
    }

    public void delayHandle(Map<String, Set<QueueBaseEntity>> delayQueueExpiredMap) {
        delayQueueExpiredMap.forEach((deviceId, delayQueueExpireds) -> delayQueueExpireds.forEach(queueBaseEntity -> {
            if (queueBaseEntity.getType() == 1) {
                this.add(queueBaseEntity);
            } else {
                this.remove(deviceId, queueBaseEntity.getSalt());
                //TODO 调用后置处理
            }
        }));
    }
}
