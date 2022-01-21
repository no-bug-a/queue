package com.example.netty;

import java.util.LinkedList;

/**
 * @author zhangyi
 * @version 1.0
 * @date 2022/1/18 11:48
 */
public class LRU<T> {
    private final int MAX_COTUNT = 4;
    private final LinkedList<LRUNode<T>> nodes = new LinkedList<>();

    public T get(String key){
        int index = 0;
        for (LRUNode<T> node : nodes) {
            if (key.equals(node.key)) {
                moveFirst(index);
                return node.value;
            }
            index++;
        }
        return null;
    }

    public void add(String key, T value){
        LRUNode<T> node = new LRUNode<>();
        node.key = key;
        node.value = value;
        addFirst(node);
    }

    private void addFirst(LRUNode<T> node) {
        nodes.addFirst(node);
        while (nodes.size() > MAX_COTUNT) {
            nodes.removeLast();
        }
    }


    private void moveFirst(int index) {
        LRUNode<T> val = nodes.get(index);
        nodes.remove(index);
        nodes.addFirst(val);
    }

    public static class LRUNode<T>{
        private String key;
        private T value;
    }
}
