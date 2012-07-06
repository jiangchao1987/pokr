package com.yanchuanli.games.pokr.test;

import com.yanchuanli.games.pokr.model.Player;
import org.apache.log4j.Logger;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        Set<String> test = new HashSet<>();
        String a = "123";
        test.add(a);
        test.add("123");
        test.remove("456");
        log.debug(test.size());
    }

    private static void rotateActor() {
        if (players.size() > 0) {
            do {
                actorPosition = (actorPosition + 1) % players.size();
                actor = players.get(actorPosition);
            } while (!players.contains(actor));

        } else {
            // Should never happen.
            throw new IllegalStateException("No active players left");
        }
    }
}
