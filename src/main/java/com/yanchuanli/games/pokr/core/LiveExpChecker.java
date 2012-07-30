package com.yanchuanli.games.pokr.core;

import com.yanchuanli.games.pokr.dao.PlayerDao;
import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.util.ExpConfig;
import com.yanchuanli.games.pokr.util.Memory;
import com.yanchuanli.games.pokr.util.TimeUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-7-30
 */
public class LiveExpChecker implements Runnable {

    private static Logger log = Logger.getLogger(LiveExpChecker.class);
    private boolean stop = false;


    public LiveExpChecker() {

    }


    @Override
    public void run() {
        log.debug("LiveExpChecker started ...");
        while (!stop) {
            int now = TimeUtil.unixtime();
            for (String s : Memory.playersOnServer.keySet()) {
                Player player = Memory.playersOnServer.get(s);
                if (now >= player.getLastTime() + 1) {
                    player.addExp(ExpConfig.expLiveTimeIncrement);
                }
                player.setLastTime(now);
                PlayerDao.updateExpAndLastTime(player, ExpConfig.expLiveTimeIncrement);
            }
            try {
                Thread.sleep(ExpConfig.expCheckInterval.inMillis());
            } catch (InterruptedException e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }
}
