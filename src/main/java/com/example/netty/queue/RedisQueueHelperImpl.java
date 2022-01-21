package com.example.netty.queue;

import com.example.netty.comm.Common;
import com.example.netty.entity.QueueBaseEntity;
import com.example.netty.enums.QueueTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author zhangyi
 * @version 1.0
 * @date 2022/1/19 14:20
 */
@Component
public class RedisQueueHelperImpl implements IQueueHelper{

    @Autowired
    private ZSetOperations<String, QueueBaseEntity> zSetOperations;

    @Override
    public void add(QueueTypeEnum queueType, QueueBaseEntity entity) {
        Assert.isTrue(entity.getSalt() == null && entity.getDbId() == null, "salt 和 dbId 不能同时为空");
        if (queueType == QueueTypeEnum.DELAY) {
            zSetOperations.add(DELAY_KEY_PREFIX + entity.getDeviceId(), entity,  getCurrentSecond() + entity.getDelay());
        } else {
            zSetOperations.add(PRIORITY_KEY_PREFIX + entity.getDeviceId(), entity,  entity.getPriority());
        }
    }

    @Override
    public void remove(QueueTypeEnum queueType, Collection<? extends QueueBaseEntity> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return;
        }
        Object[] array = collection.toArray();
        zSetOperations.remove(getKey(queueType, (QueueBaseEntity)array[0]), array);
    }

    @Override
    public void remove(QueueTypeEnum queueType, QueueBaseEntity entity) {
        zSetOperations.remove(getKey(queueType, entity), entity);
    }

    @Override
    public void remove(QueueTypeEnum queueType, String deviceId, String salt) {
        Set<QueueBaseEntity> queueBaseEntitySet = zSetOperations.range(getKey(queueType, deviceId), 0, -1);
        if (CollectionUtils.isEmpty(queueBaseEntitySet)) {
            return;
        }
        queueBaseEntitySet.forEach(entity -> {
            if (salt.equals(entity.getSalt())) {
                this.remove(queueType, entity);
            }
        });
    }

    @Override
    public void remove(QueueTypeEnum queueType, String deviceId, Integer dbId) {
        Set<QueueBaseEntity> queueBaseEntitySet = zSetOperations.range(getKey(queueType, deviceId), 0, -1);
        if (CollectionUtils.isEmpty(queueBaseEntitySet)) {
            return;
        }
        queueBaseEntitySet.forEach(entity -> {
            if (dbId.equals(entity.getDbId())) {
                this.remove(queueType, entity);
            }
        });
    }

    @Override
    public Map<String, Set<QueueBaseEntity>> getExpired() {
        Map<String, Set<QueueBaseEntity>> deviceBaseEntityMap = new HashMap<>();
        List<String> allDeviceIds = getAllDeviceIds();
        allDeviceIds.forEach(deviceId -> {
            Set<QueueBaseEntity> queueBaseEntities = zSetOperations.rangeByScore(getKey(QueueTypeEnum.DELAY, deviceId), 0, getCurrentSecond());
            if (!CollectionUtils.isEmpty(queueBaseEntities)) {
                deviceBaseEntityMap.put(deviceId, queueBaseEntities);
            }
        });
        if (CollectionUtils.isEmpty(deviceBaseEntityMap)) {
            return deviceBaseEntityMap;
        }
        deviceBaseEntityMap.forEach((deviceId, baseEntitySet) -> this.remove(QueueTypeEnum.DELAY, baseEntitySet));
        return deviceBaseEntityMap;
    }

    @Override
    public List<QueueBaseEntity> allDeviceFirstNode() {
        List<String> allDeviceIds = getAllDeviceIds();
        List<QueueBaseEntity> nodes = new LinkedList<>();
        allDeviceIds.forEach(deviceId -> {
            //优先级队列反过来 score 越大越优先, 所以用 reverseRange 取最后一个
            Set<QueueBaseEntity> entities = zSetOperations.reverseRange(getKey(QueueTypeEnum.PRIORITY, deviceId), 0, 0);
            if (!CollectionUtils.isEmpty(entities)) {
                nodes.addAll(entities);
            }
        });
        return nodes;
    }

    @Override
    public int getOffset(String deviceId) {
        String key = getKey(QueueTypeEnum.DELAY, deviceId);
        Set<ZSetOperations.TypedTuple<QueueBaseEntity>> typedTuples = zSetOperations.reverseRangeWithScores(key, 0, 0);
        if (CollectionUtils.isEmpty(typedTuples)) {
            return 0;
        }
        int offset = 0;
        int currentTimeMillis = (int)(System.currentTimeMillis() / 1000);
        for (ZSetOperations.TypedTuple<QueueBaseEntity> typedTuple : typedTuples) {
            int val = Objects.requireNonNull(typedTuple.getScore()).intValue() - currentTimeMillis;
            if (val > offset) {
                offset = val;
            }
        }
        return offset;
    }

    private List<String> getAllDeviceIds() {
        //TODO 改成缓存中获取
        List<String> allDeviceIds = Common.deviceIds;
        if (CollectionUtils.isEmpty(allDeviceIds)) {
            throw new RuntimeException("设备列表为空");
        }
        return allDeviceIds;
    }
}
