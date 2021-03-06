package com.yanchuanli.games.pokr.test.concurrent;

import com.google.code.tempusfugit.concurrency.Interrupter;
import com.google.code.tempusfugit.temporal.Duration;
import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-4
 */

public class TestPlayer extends Thread {

    private static Logger log = Logger.getLogger(TestPlayer.class);

    private boolean stop;

    private AtomicInteger state;

    private MyTimerTask timer;

    public TestPlayer() {
        stop = false;
        state = new AtomicInteger(0);
    }

    @Override
    public void run() {
        while (!stop && !interrupted()) {
            log.debug("running");

        }
        log.info("thread ended ...");
    }

    public synchronized void next() {
        state.incrementAndGet();
        timer = new MyTimerTask(5, this);
        log.debug("current state:" + String.valueOf(state.intValue()));
    }


    public void setStop() {
        stop = true;
    }

    public static void main(String[] args) throws InterruptedException {
        Centre.tp.start();

        log.info("system started ...");
        /*
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (!input.equalsIgnoreCase("quit")) {
            log.debug(input);
            Centre.tp.next();
        }
        Centre.tp.setStop();
//        while (!input.equalsIgnoreCase("quit")) {
//            tp.next();
//            input = scanner.nextLine();
//        }
*/

        Duration duration = Duration.seconds(3);
        Interrupter interrupter = Interrupter.interrupt(Centre.tp).after(duration);
        try {
            while (!currentThread().isInterrupted()) {
                // some long running process
//                log.info("I am watching ...");
//                Thread.sleep(1000);
            }
        } finally {
            interrupter.cancel();
        }
    }
}
