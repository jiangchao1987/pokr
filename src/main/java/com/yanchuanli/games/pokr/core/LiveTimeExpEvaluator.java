package com.yanchuanli.games.pokr.core;

import com.yanchuanli.games.pokr.dao.PlayerDao;
import com.yanchuanli.games.pokr.model.EXPTimeLevel;
import com.yanchuanli.games.pokr.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-7-31
 */
public class LiveTimeExpEvaluator {

    private static List<EXPTimeLevel> levels = new ArrayList<>();

    static {
        levels.add(EXPTimeLevel.LEVEL1);
        levels.add(EXPTimeLevel.LEVEL2);
        levels.add(EXPTimeLevel.LEVEL3);
        levels.add(EXPTimeLevel.LEVEL4);
        levels.add(EXPTimeLevel.LEVEL5);
        levels.add(EXPTimeLevel.LEVEL6);
        levels.add(EXPTimeLevel.LEVEL7);
        levels.add(EXPTimeLevel.LEVEL8);
        levels.add(EXPTimeLevel.LEVEL9);
        levels.add(EXPTimeLevel.LEVEL10);
        levels.add(EXPTimeLevel.LEVEL11);
        levels.add(EXPTimeLevel.LEVEL12);
        levels.add(EXPTimeLevel.LEVEL13);
        levels.add(EXPTimeLevel.LEVEL14);
        levels.add(EXPTimeLevel.LEVEL15);
        levels.add(EXPTimeLevel.LEVEL16);
    }

    public static void checkExp(Player player) {
        if (player.getElapsedTimeToday() > 0) {
            int time = player.getElapsedTimeToday();
            for (EXPTimeLevel exp : levels) {
                if (time > exp.getMinTimeInSeconds() && time <= exp.getMaxTimeInSeconds()) {
                    PlayerDao.addExp(player, exp.getExp());
                    break;
                }
            }
        }
    }
}
