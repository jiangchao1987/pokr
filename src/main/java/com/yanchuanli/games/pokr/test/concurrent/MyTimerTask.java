package com.yanchuanli.games.pokr.test.concurrent;

import org.apache.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-4
 */
public class MyTimerTask {

    private static Logger log = Logger.getLogger(MyTimerTask.class);

    private Timer timer;

    private TestPlayer tt;

    public MyTimerTask(int seconds, TestPlayer tt) {
        timer = new Timer();
        this.tt = tt;
        timer.schedule(new RemindTask(), seconds * 1000);
        log.debug("timer started ...");
    }

    class RemindTask extends TimerTask {
        public void run() {
            log.debug("times up");
            timer.cancel(); //Terminate the timer thread
            tt.setStop();
        }
    }

    public void cancel() {
        timer.cancel();
    }

}
