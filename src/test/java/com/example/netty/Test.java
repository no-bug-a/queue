package com.example.netty;

import ch.qos.logback.core.util.TimeUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangyi
 * @version 1.0
 * @date 2022/1/13 19:43
 */
public class Test {

    public static void main(String[] args) {
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print(j + "*" + i + "=" + (i * j) + "\t");
            }
            System.out.println();
        }
    }
    private static void sleep(long n) {
        try {
            Thread.sleep(n);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
