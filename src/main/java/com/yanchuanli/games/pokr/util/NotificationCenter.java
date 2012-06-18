package com.yanchuanli.games.pokr.util;

import com.yanchuanli.games.pokr.model.Player;

import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-16
 */
public class NotificationCenter {

    public static void notifiAllPlayersOnTable(List<Player> players, String info) {
        for (Player player : players) {
            notifyPlayer(player, info);
        }
    }

    public static void notifyPlayer(Player player, String info) {
        if (!Config.offlineDebug) {
            Util.sendMessage(player.getSession(), info);
        }
    }

}
