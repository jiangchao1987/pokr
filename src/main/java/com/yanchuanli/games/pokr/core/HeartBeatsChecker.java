package com.yanchuanli.games.pokr.core;

import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.server.ServerConfig;
import com.yanchuanli.games.pokr.util.Config;
import com.yanchuanli.games.pokr.util.Memory;
import com.yanchuanli.games.pokr.util.NotificationCenter;
import com.yanchuanli.games.pokr.util.Util;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 11/26/12
 */
public class HeartBeatsChecker implements Runnable {

    private static Logger log = Logger.getLogger(HeartBeatsChecker.class);
    private boolean stop = false;


    private static ExecutorService pool;

    public HeartBeatsChecker() {
        pool = Executors.newCachedThreadPool();
    }

    @Override
    public void run() {
        log.debug("HeartBeatsChecker started ...");
        // send the first heartbeat packt
        while (!stop) {
            for (String udid : Memory.playersOnServer.keySet()) {
                Player player = Memory.playersOnServer.get(udid);
                if (player.isOnline()) {
                    pool.execute(new HeatBeatSenderThread(player));
                    Memory.heartbeatsMap.put(udid, Config.HEARTBEAT_SENT);
                }
            }
            try {
                Thread.sleep(ServerConfig.heartbeatCheckWaitingInterval);
            } catch (InterruptedException e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
            // send the user the 2nd heartbeat packet when he doesn't respond for the 1st
            for (String udid : Memory.playersOnServer.keySet()) {
                Integer heartbeatStatus = Memory.heartbeatsMap.get(udid);
                if (heartbeatStatus != null && heartbeatStatus != Config.HEARTBEAT_CONFIRMED) {
                    Player player = Memory.playersOnServer.get(udid);
                    if (player.isOnline()) {
                        pool.execute(new HeatBeatSenderThread(player));
                        Memory.heartbeatsMap.put(udid, Config.HEARTBEAT_RESENT);
                    }
                }
            }

            try {
                Thread.sleep(ServerConfig.heartbeatCheckWaitingInterval);
            } catch (InterruptedException e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }

            for (String udid : Memory.playersOnServer.keySet()) {
                Integer heartbeatStatus = Memory.heartbeatsMap.get(udid);
                if (heartbeatStatus != null && heartbeatStatus != Config.HEARTBEAT_CONFIRMED) {
                    log.debug("player " + udid + " hasnot respond his heartbeat and will be disconnected ...");
                    Player player = Memory.playersOnServer.get(udid);
                    Util.disconnectUser(player.getSession());
                }
            }

            Memory.heartbeatsMap.clear();

            try {
                Thread.sleep(ServerConfig.heattbeatDurationInterval);
            } catch (InterruptedException e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }

    public class HeatBeatSenderThread implements Runnable {

        private Player player;

        public HeatBeatSenderThread(Player player) {
            this.player = player;
        }

        @Override
        public void run() {
            NotificationCenter.sendHeartBeat(player.getSession());
        }
    }

}

