package com.yanchuanli.games.pokr.test;

import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.model.Table;
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


        Table table = new Table();
        Player a = new Player("a", "a");
        Player b = new Player("b", "b");
        Player c = new Player("c", "c");
        table.joinTable(a);
        table.joinTable(b);
        table.joinTable(c);

        for (int i = 0; i < 6; i++) {
            Player p = table.nextPlayer();
            log.debug(p);
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
