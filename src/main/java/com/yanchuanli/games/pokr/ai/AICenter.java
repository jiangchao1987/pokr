package com.yanchuanli.games.pokr.ai;

import com.yanchuanli.games.pokr.ai.bot.Bot;
import org.apache.log4j.Logger;

/**
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 10/24/12
 */
public class AICenter {
    private static Logger log = Logger.getLogger(AICenter.class);

    public static void main(String[] args) {

        Bot bota = new Bot("c", "c123", 21689093);
        Thread botaThread = new Thread(bota);
        botaThread.start();

        Bot botb = new Bot("d", "d123", 21689093);
        Thread botbThread = new Thread(botb);
        botbThread.start();
    }
}
