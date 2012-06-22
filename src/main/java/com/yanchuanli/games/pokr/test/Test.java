package com.yanchuanli.games.pokr.test;

import com.yanchuanli.games.pokr.model.Player;
import org.apache.log4j.Logger;

import java.net.InetAddress;
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
        Player player1 = new Player("0", "0");
        Player player2 = new Player("1", "1");
        Player player3 = new Player("2", "2");

        players.add(player1);
        players.add(player2);
        players.add(player3);

        for (int i = 0; i < 3; i++) {
            rotateActor();
            if (i == 2) {
                players.remove(actor);
            }
            log.debug(actor.getName());
        }


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
