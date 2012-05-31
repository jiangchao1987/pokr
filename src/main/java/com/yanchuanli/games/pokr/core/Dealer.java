package com.yanchuanli.games.pokr.core;

import com.yanchuanli.games.pokr.util.Memory;
import com.yanchuanli.games.pokr.util.Util;
import org.apache.log4j.Logger;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class Dealer extends Thread {

    private static Logger log = Logger.getLogger(Dealer.class);

    private boolean started = false;

    public Dealer() {

    }

    public void run() {
        started = true;
        while (!Thread.currentThread().isInterrupted() && started) {

            for (String s : Memory.sessionsOnServer.keySet()) {
                if (s.equalsIgnoreCase("1")) {
                    Util.sendMessage(Memory.sessionsOnServer.get(s), "hello ");
                }
            }
        }

    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
