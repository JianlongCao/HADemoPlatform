package com.cisco.demo.comm;

import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommBucket extends Observable {

    ConcurrentLinkedQueue<String> buckets = new ConcurrentLinkedQueue<>();

    public void set(String cmd) {
        buckets.add(cmd);
        System.out.println("buckets add : " + cmd);
        setChanged();
        notifyObservers();
    }

    public String get() {
        System.out.println("buckets head emlement was taken");
        return buckets.poll();
    }
}