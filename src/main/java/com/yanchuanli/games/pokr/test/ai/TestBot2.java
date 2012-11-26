package com.yanchuanli.games.pokr.test.ai;

import com.yanchuanli.games.pokr.ai.bot.Bot;

/**
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 10/26/12
 */
public class TestBot2 {
    public static void main(String[] args) {
        Bot bot = new Bot("f", "f123", 21689083);
        Thread botThread = new Thread(bot);
        botThread.start();
    }
}
