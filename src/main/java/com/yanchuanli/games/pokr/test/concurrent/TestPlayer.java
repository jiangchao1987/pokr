package com.yanchuanli.games.pokr.test.concurrent;

import org.apache.log4j.Logger;

import java.util.Scanner;
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
        while (!stop) {

        }
    }

    public synchronized void next() {
        state.incrementAndGet();
        timer = new MyTimerTask(5, this);
        log.debug("current state:" + String.valueOf(state.intValue()));
    }


    public void setStop() {
        stop = true;
    }

    public static void main(String[] args) {
        TestPlayer tp = new TestPlayer();
        tp.start();

        log.debug("system started ...");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if(!input.equalsIgnoreCase("quit")){
            log.debug(input);
            tp.next();
        }
        tp.setStop();
//        while (!input.equalsIgnoreCase("quit")) {
//            tp.next();
//            input = scanner.nextLine();
//        }
    }
}
