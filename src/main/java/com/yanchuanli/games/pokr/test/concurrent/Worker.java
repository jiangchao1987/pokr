package com.yanchuanli.games.pokr.test.concurrent;

import com.google.code.tempusfugit.temporal.Condition;
import com.google.code.tempusfugit.temporal.Duration;
import com.google.code.tempusfugit.temporal.Timeout;
import com.google.code.tempusfugit.temporal.WaitFor;
import org.apache.log4j.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-8
 */

public class Worker implements Runnable {

    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;
    private boolean step1;
    private Logger log = Logger.getLogger(Worker.class);
    private int id;

    private Condition condition1;

    Worker(int id, CountDownLatch startSignal, CountDownLatch doneSignal) {
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
        this.id = id;
        step1 = false;
        condition1 = new Condition() {
            @Override
            public boolean isSatisfied() {
                return step1;
            }
        };
    }

    @Override
    public void run() {
        try {
            startSignal.await();
            doWork();
            doneSignal.countDown();
            log.info("worker #" + getId() + " has finished his job ...");
        } catch (InterruptedException ex) {
            log.error(ex);
        }
    }

    private void doWork() {
        log.info("worker #" + getId() + " starts working ...");
        step1();

    }

    public int getId() {
        return id;
    }

    private void step1() {
        Duration duration = Duration.seconds(3);
        try {
            WaitFor.waitOrTimeout(condition1, Timeout.timeout(duration));
        } catch (InterruptedException | TimeoutException e) {
            if (e instanceof TimeoutException) {
                log.info("worker #" + getId() + " timed out ...");
                finishStep1();
            } else {
                log.error(e);
            }
        }
        while (!step1) {

        }
        log.info("worker #" + getId() + " has finished step1 ...");

    }

    public void finishStep1() {
        step1 = true;
    }

}
