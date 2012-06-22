package com.yanchuanli.games.pokr.game;

import com.google.code.tempusfugit.temporal.Duration;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-14
 */
public class GameConfig {

    private Duration bettingDuration;
    private Duration inactivityCheckInterval;
    private String name;
    private int smallBlindAmount;
    private int bigBlindAmount;
    private int minHolding;
    private int maxHolding;
    private int maxPlayersCount;
    private int id;

    public GameConfig(int id, String name, int smallBlindAmount, int bigBlindAmount, int minHolding, int maxHolding, int maxPlayersCount, Duration bettingDuration, Duration inactivityCheckInterval) {
        this.bettingDuration = bettingDuration;
        this.inactivityCheckInterval = inactivityCheckInterval;
        this.name = name;
        this.smallBlindAmount = smallBlindAmount;
        this.bigBlindAmount = bigBlindAmount;
        this.minHolding = minHolding;
        this.maxHolding = maxHolding;
        this.maxPlayersCount = maxPlayersCount;
        this.id = id;
    }

    public Duration getBettingDuration() {
        return bettingDuration;
    }

    public String getName() {
        return name;
    }

    public int getSmallBlindAmount() {
        return smallBlindAmount;
    }

    public int getBigBlindAmount() {
        return bigBlindAmount;
    }

    public int getMinHolding() {
        return minHolding;
    }

    public int getMaxHolding() {
        return maxHolding;
    }

    public int getMaxPlayersCount() {
        return maxPlayersCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Duration getInactivityCheckInterval() {
        return inactivityCheckInterval;
    }

    public void setInactivityCheckInterval(Duration inactivityCheckInterval) {
        this.inactivityCheckInterval = inactivityCheckInterval;
    }
}
