package com.yanchuanli.games.pokr.test.ai;

import com.yanchuanli.games.pokr.ai.bot.Bot;
import com.yanchuanli.games.pokr.conf.Configure;

/**
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 10/25/12
 */
public class TestBot {
    public static void main(String[] args) {
        Bot bot = new Bot("d", "d123", Integer.parseInt(Configure.getProperty("10111")));
        Thread botThread = new Thread(bot);
        botThread.start();
    }
}
