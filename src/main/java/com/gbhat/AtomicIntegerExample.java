package com.gbhat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class SimpleInteger {
    private volatile int i;

    public SimpleInteger(int initialValue) {
        this.i = initialValue;
    }

    public int incrementAndGet() {
        return ++i;
    }

    public int get() {
        return i;
    }
}

public class AtomicIntegerExample {

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger ai = new AtomicInteger(0);            // Create an AtomicInteger and SimpleInteger shared across threads
        SimpleInteger si = new SimpleInteger(0);
        ExecutorService es = Executors.newFixedThreadPool(4);
        for (int k = 0; k < 4; k++) {                   //Submit 4 jobs to thread pool performing same operation
            es.submit(() -> {
                for (int i1 = 0; i1 < 1000; i1++) {
                    si.incrementAndGet();               // Increment SimpleInteger and AtomicInteger
                    ai.incrementAndGet();
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
        }
        es.shutdown();          // Do not accept new jobs in the threadpool
        es.awaitTermination(6000, TimeUnit.MILLISECONDS);      // Wait for Thread Pool to terminate, assuming it is terminating normally
        System.out.println("Simple Integer count: " + si.get());
        System.out.println("AtomicInteger count: " + ai.get());
    }
}
