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
        levels.add(EXPTimeLevel.LEVEL17);
        levels.add(EXPTimeLevel.LEVEL18);
        levels.add(EXPTimeLevel.LEVEL19);
        levels.add(EXPTimeLevel.LEVEL20);
        levels.add(EXPTimeLevel.LEVEL21);
        levels.add(EXPTimeLevel.LEVEL22);
        levels.add(EXPTimeLevel.LEVEL23);
        levels.add(EXPTimeLevel.LEVEL24);
        levels.add(EXPTimeLevel.LEVEL25);
        levels.add(EXPTimeLevel.LEVEL26);
        levels.add(EXPTimeLevel.LEVEL27);
        levels.add(EXPTimeLevel.LEVEL28);
    }

    public static void checkExp(Player player) {
        if (player.getElapsedTimeToday() > 0) {
            int time = player.getElapsedTimeToday();
            int startLevel = player.getTimeLevelToday();
            for (int i = startLevel; i < levels.size(); i++) {
                EXPTimeLevel exp = levels.get(i);
                if (time > exp.getMinTimeInSeconds() && time <= exp.getMaxTimeInSeconds()) {
                    if (exp.getLevel() == player.getTimeLevelToday()) {

                    } else {
                        player.setTimeLevelToday(exp.getLevel());
                        PlayerDao.addExp(player, exp.getExp());
                        PlayerDao.updateTimeLevelToday(player);
                        break;
                    }

                }
            }
        }
    }
}
