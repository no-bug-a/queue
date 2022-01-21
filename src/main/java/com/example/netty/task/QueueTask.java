package com.example.netty.task;

import com.example.netty.entity.QueueBaseEntity;
import com.example.netty.queue.DelayQueue;
import com.example.netty.queue.PriorityQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangyi
 * @version 1.0
 * @date 2022/1/19 9:55
 */
@Slf4j
@Component
public class QueueTask {
    @Autowired
    private DelayQueue delayQueue;
    @Autowired
    private PriorityQueue priorityQueue;

    @Scheduled(cron = "0/5 * * * * ?")
    public void delay(){
        Map<String, Set<QueueBaseEntity>> delayQueueExpiredMap = delayQueue.getExpired();
        if (CollectionUtils.isEmpty(delayQueueExpiredMap)) {
            return;
        }
        System.out.println("---------------- 延迟队列 ----------------");
        System.out.println(delayQueueExpiredMap);
        //优先级队列处理出队数据
        priorityQueue.delayHandle(delayQueueExpiredMap);
    }

    @Scheduled(cron = "2/5 * * * * ?")
    public void priority(){
        List<QueueBaseEntity> queueBaseEntities = priorityQueue.allDeviceFirstNode();
        if (CollectionUtils.isEmpty(queueBaseEntities)) {
            return;
        }
        System.out.println("---------------- 优先级队列 ----------------");
        System.out.println(queueBaseEntities);
    }
}
