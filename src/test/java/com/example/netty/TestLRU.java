package com.example.netty;

import java.util.Random;

/**
 * @author zhangyi
 * @version 1.0
 * @date 2022/1/18 12:01
 */
public class TestLRU {
    public static void main(String[] args) {
        String[] keys = {"A", "B", "C", "D"};
        LRU<Integer> lru = new LRU<>();
        for (int i = 0; i < keys.length; i++) {
            lru.add(keys[i], i);
        }
        while (true) {
            int random = new Random().nextInt(4);
            System.out.println(lru.get(keys[random]));
        }
    }
}
