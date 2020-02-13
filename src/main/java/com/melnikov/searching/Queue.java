package com.melnikov.searching;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class Queue {

    public LinkedBlockingQueue<File> queue = new LinkedBlockingQueue<File>();

    @PostConstruct
    private void setStart() throws InterruptedException{
        File[] files = File.listRoots();
        for(File file : files) queue.put(file);
    }

    synchronized void put(File file) throws InterruptedException {
        queue.put(file);
        notifyAll();
    }

    synchronized File get() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        return queue.take();
    }
}
