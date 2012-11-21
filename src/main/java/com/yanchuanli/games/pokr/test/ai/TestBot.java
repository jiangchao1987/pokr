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
        Bot bot;
        if (args.length == 3) {
            bot = new Bot(args[0], args[1], Integer.parseInt(args[2]));
        } else {
            bot = new Bot("f", "f123", Integer.parseInt(Configure.getProperty("room_bot")));
        }

        Thread botThread = new Thread(bot);
        botThread.start();
    }

}