package com.example.netty;

import com.example.netty.comm.Common;
import com.example.netty.entity.QueueBaseEntity;
import com.example.netty.queue.DelayQueue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Random;
import java.util.Set;

@SpringBootTest
class NettyApplicationTests {
    @Autowired
    private DelayQueue delayQueue;
    @Test
    void add() {
    }

    @Test
    void get() {
    }

}
