package com.yanchuanli.games.pokr.game.workers;

import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.util.NotificationCenter;

import java.util.List;

/**
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 9/17/12
 */
public class DealerSaysWorker implements Runnable {

    private String content;
    private List<Player> allPlayers;

    public DealerSaysWorker(String content, List<Player> allPlayers) {
        this.content = content;
        this.allPlayers = allPlayers;
    }

    @Override
    public void run() {
        NotificationCenter.dealerSays(allPlayers, content);
    }
}
