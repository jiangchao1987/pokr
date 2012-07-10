package com.yanchuanli.games.pokr.test;

import com.yanchuanli.games.pokr.model.Player;
import org.apache.log4j.Logger;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class Test {

    private static Logger log = Logger.getLogger(Test.class);
    private static List<Player> players = new ArrayList<>();
    private static int actorPosition = 1;
    private static Player actor;

    public static void main(String[] args) throws UnknownHostException {

        List<String> test = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            test.add(String.valueOf(i));
        }

        for (int i = test.size() - 1; i >= 0; i--) {
            log.debug(test.get(i));
        }
    }

}
