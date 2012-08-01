package com.yanchuanli.games.pokr.test.concurrent.forkjoin;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-8-1
 */
public class SortTask extends RecursiveAction {

    private final long[] array;
    private final int lo;
    private final int hi;
    private static Logger log = Logger.getLogger(SortTask.class);

    private int THRESHOLD = 30;

    public SortTask(long[] array) {
        this(array, 0, array.length - 1);
    }

    public SortTask(long[] array, int lo, int hi) {
        this.array = array;
        this.lo = lo;
        this.hi = hi;
    }


    @Override
    protected void compute() {
        log.info("computing ...");
        if (hi - lo < THRESHOLD) {
            sequentiallySort(array, lo, hi);
        } else {
            int pivot = partition(array, lo, hi);
            this.invokeAll(new SortTask(array, lo, pivot - 1), new SortTask(array, pivot + 1, hi));
        }

    }

    private int partition(long[] array, int lo, int hi) {
        long x = array[hi];
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (array[j] <= x) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, hi);
        return i + 1;
    }

    private void swap(long[] array, int i, int j) {
        if (i != j) {
            long temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    private void sequentiallySort(long[] array, int lo, int hi) {
        Arrays.sort(array, lo, hi + 1);
    }

    public static void main(String[] args) throws Exception {
        long[] array = new long[100];

        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            array[i] = random.nextInt(1000);
        }

        ForkJoinTask sort = new SortTask(array);
        ForkJoinPool pool = new ForkJoinPool();

        pool.submit(sort);
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);

        for (int i = 0; i < array.length; i++) {
            log.debug(array[i] + " , ");
        }

    }
}
