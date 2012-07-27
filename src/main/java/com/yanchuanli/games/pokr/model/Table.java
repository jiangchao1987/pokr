package com.yanchuanli.games.pokr.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
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
    private Random ran;
    private int counter;
    private static Logger log = Logger.getLogger(Table.class);
    public int dealerPosition = 0;

    public Table() {
        playerCount = 9;
        players = new Player[playerCount];
        ran = new Random();
        counter = 0;
    }

    public void rotateDealer() {
        dealerPosition = (dealerPosition + 1) % size();
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

    public void joinTable(Player player) {
        List<Integer> availableChairs = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            if (players[i] == null) {
                availableChairs.add(i);
            }
        }
        int randomChairIndex = availableChairs.get(ran.nextInt(availableChairs.size()));
        joinTableAtIndex(player, randomChairIndex);
    }

    public void joinTableAtIndex(Player player, int index) {
        players[index] = player;
        log.debug(player.getName() + " is sitted at " + index);
    }

    public void resetCounter() {
        counter = 0;
    }

    public Player nextPlayer() {
        Player player = null;
        int size = size();
        if (size > 0) {
            while (player == null) {
                player = players[counter];
                counter = (counter + 1) % playerCount;
            }
        }
        return player;
    }

    public void printTableInfo() {
        for (int i = 0; i < playerCount; i++) {
            if (players[i] != null) {
                log.debug(i + ":" + players[i].getName());
            }
        }
    }
}
