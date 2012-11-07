package com.yanchuanli.games.pokr.game.workers;

import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.util.NotificationCenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 11/6/12
 */
public class VoiceChatThreadWorker implements Runnable {
    private Player player;
    private String content;
    private List<Player> allPlayers;

    public VoiceChatThreadWorker(Player player, String content, List<Player> allPlayers) {
        this.player = player;
        this.content = content;
        this.allPlayers = allPlayers;
    }

    @Override
    public void run() {
        List<Player> players = new ArrayList<>();
        for (Player p : allPlayers) {
            if (!p.getUdid().equals(player.getUdid())) {
                players.add(p);
            }
        }
        NotificationCenter.voicechat(players, player.getUdid() + ": " + content);
        players.clear();
        players = null;
    }
}
