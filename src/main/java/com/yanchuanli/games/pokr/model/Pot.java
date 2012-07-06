package com.yanchuanli.games.pokr.model;

import com.yanchuanli.games.pokr.util.Config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-7-6
 */
public class Pot {

    private Set<String> noFoldPlayers;
    private List<Record> history;
    private int money;

    public Pot() {
        money = 0;
        noFoldPlayers = new HashSet<>();
        history = new ArrayList<>();
    }

    public void addRecord(Record record) {
        history.add(record);
        money += record.getBet();
        if (record.getActionType() == Config.ACTION_TYPE_FOLD) {
            noFoldPlayers.remove(record.getUdid());
        } else {
            noFoldPlayers.add(record.getUdid());
        }
    }

    public int getMoney() {
        return money;
    }

    public Set<String> getPlayersUDID() {
        return noFoldPlayers;
    }


}
