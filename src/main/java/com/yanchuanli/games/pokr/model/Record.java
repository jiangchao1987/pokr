package com.yanchuanli.games.pokr.model;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-7-6
 */
public class Record {
    private String udid;
    private int actionType;
    private int bet;
    private long timestamp;

    public Record(String udid, int actionType, int bet) {
        this.udid = udid;
        this.actionType = actionType;
        this.bet = bet;
        this.timestamp = System.currentTimeMillis() / 1000L;
    }

    public String getUdid() {
        return udid;
    }

    public int getActionType() {
        return actionType;
    }

    public int getBet() {
        return bet;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
