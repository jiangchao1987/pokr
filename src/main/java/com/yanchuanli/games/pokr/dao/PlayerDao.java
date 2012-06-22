package com.yanchuanli.games.pokr.dao;

import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.util.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-22
 */
public class PlayerDao {

    private static Map<String, Player> players;
    private static int globalid = 0;

    static {
        players = new HashMap<>();
    }

    public static Player getPlayer(String username, int src) {
        switch (src) {
            case Config.SRC_IPHONE_TOURIST:
                break;
            case Config.SRC_IPAD_TOURIST:
                break;
        }
        if (!players.containsKey(username)) {
            Player player = new Player(username, username);
            player.setMoney(10000);
            players.put(username, player);
        }
        return players.get(username);

    }


}
