package com.yanchuanli.games.pokr.model;

import java.util.Random;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class Table {

    private Player[] players;
    private Random random;
    private int playerCount;

    public Table() {
        playerCount = 9;
        players = new Player[playerCount];
    }

    public boolean addPlayer(Player player) {
        boolean result = !isFull();
        if (result) {
            for (int i = 0; i < playerCount; i++) {
                if (players[i] == null) {
                    players[i] = player;
                    break;
                }
            }
        }
        return result;
    }

    public boolean removePlayer(Player player) {
        boolean result = false;
        for (int i = 0; i < playerCount; i++) {
            if (players[i].getUdid().equals(player.getUdid())) {
                players[i] = null;
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean isFull() {
        boolean result = true;
        for (int i = 0; i < playerCount; i++) {
            if (players[i] == null) {
                result = false;
                break;
            }
        }
        return result;
    }

    private void clear() {
        for (int i = 0; i < playerCount; i++) {
            players[i] = null;
        }
    }

    public int size() {
        int result = 0;
        for (int i = 0; i < playerCount; i++) {
            if (players[i] != null) {
                result++;
            }
        }
        return result;
    }

}
