package com.yanchuanli.games.pokr.game.workers;

import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.util.NotificationCenter;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 8/28/12
 */
public class AddFriendRequestWorker implements Runnable {

    private Map<String, Player> waitingPlayers;
    private List<Player> activePlayers;
    private Player fromPlayer;
    private Player toPlayer;
    private static Logger log = Logger.getLogger(AddFriendRequestWorker.class);

    public AddFriendRequestWorker(Map<String, Player> waitingPlayers, List<Player> activePlayers, Player fromPlayer, Player toPlayer) {
        this.waitingPlayers = waitingPlayers;
        this.activePlayers = activePlayers;
        this.fromPlayer = fromPlayer;
        this.toPlayer = toPlayer;
    }

    @Override
    public void run() {
        boolean toPlayerIsOnSeat = waitingPlayers.keySet().contains(toPlayer.getUdid()) || activePlayers.contains(toPlayer);
        if (toPlayerIsOnSeat) {
            log.debug(fromPlayer.getName() + " wants to add " + toPlayer.getName() + " as his friend.");
            NotificationCenter.forwardAddFriendRequest(toPlayer, fromPlayer.getName() + "," + fromPlayer.getUdid());
        }
    }
}
