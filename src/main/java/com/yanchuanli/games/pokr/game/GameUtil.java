package com.yanchuanli.games.pokr.game;

import com.yanchuanli.games.pokr.basic.PlayerRankComparator;
import com.yanchuanli.games.pokr.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-14
 */
public class GameUtil {

    private static PlayerRankComparator comparator = new PlayerRankComparator();

    public static List<List<Player>> rankPlayers(List<Player> players) {
        List<List<Player>> result = new ArrayList<>();
        Collections.sort(players, comparator);

        int lastRank = 0;
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (player.getBestHandRank() != lastRank) {
                List<Player> a = new ArrayList<>();
                a.add(player);
                result.add(a);
                lastRank = player.getBestHandRank();
            } else {
                result.get(result.size() - 1).add(player);
            }
        }
        return result;
    }

}
