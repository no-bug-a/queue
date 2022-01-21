package com.example.netty.controller;

import com.example.netty.entity.QueueBaseEntity;
import com.example.netty.queue.DelayQueue;
import com.example.netty.queue.PriorityQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author zhangyi
 * @version 1.0
 * @date 2022/1/19 15:29
 */
@RestController
@RequestMapping("/queue")
public class QueueController {
    @Autowired
    private DelayQueue delayQueue;
    @Autowired
    private PriorityQueue priorityQueue;

    private static final String SUCCESS_MSG = "success";

    @PostMapping("/delay")
    public String delay(@RequestBody QueueBaseEntity entity) {
        String salt = UUID.randomUUID().toString();
        entity.setSalt(salt);
        delayQueue.add(entity);
        return SUCCESS_MSG;
    }
    @PostMapping("/priority")
    public String priority(@RequestBody QueueBaseEntity entity) {
        String salt = UUID.randomUUID().toString();
        entity.setSalt(salt);
        priorityQueue.add(entity);
        return SUCCESS_MSG;
    }
    @PutMapping("/priority")
    public String priorityUpdate(@RequestBody QueueBaseEntity entity) {
        priorityQueue.update(entity);
        return SUCCESS_MSG;
    }
    @DeleteMapping("/{type}")
    public String priorityDel(@PathVariable Integer type, @RequestBody QueueBaseEntity entity) {
        if (type == 1) {
            delayQueue.remove(entity.getDeviceId(), entity.getSalt());
        } else {
            priorityQueue.remove(entity.getDeviceId(), entity.getSalt());
        }
        return SUCCESS_MSG;
    }
}
