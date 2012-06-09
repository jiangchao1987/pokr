package com.yanchuanli.games.pokr.test.concurrent;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-8
 */
public class Driver {

    private static Logger log = Logger.getLogger(Driver.class);

    private static int workersCount = 5;

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch doneSignal = new CountDownLatch(workersCount);
        List<Worker> workers = new ArrayList<>();
        List<CountDownLatch> startSignals = new ArrayList<>();

        for (int i = 0; i < workersCount; ++i) { // create and start threads
            CountDownLatch startSignal = new CountDownLatch(1);
            startSignals.add(startSignal);
            Worker w = new Worker(i, startSignal, doneSignal);
            workers.add(w);
            new Thread(w).start();
        }

        log.info("workers ready ...");


        doneSignal.await();           // wait for all to finish

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (!input.equalsIgnoreCase("quit")) {
            log.debug("input:" + input);
            if (input.equalsIgnoreCase("start")) {
                for (int i = 0; i < startSignals.size(); i++) {

                }
            } else {
                int id = Integer.parseInt(input);
                if (id == 0) {
                    workers.get(id).finishStep1();
                }
            }

        }
    }
}
