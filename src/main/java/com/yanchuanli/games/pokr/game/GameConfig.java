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
    private String name;
    private int smallBlindAmount;
    private int bigBlindAmount;
    private int minHolding;
    private int maxHolding;
    private int maxPlayersCount;

    public GameConfig(Duration bettingDuration, String name, int smallBlindAmount, int bigBlindAmount, int minHolding, int maxHolding, int maxPlayersCount) {
        this.bettingDuration = bettingDuration;
        this.name = name;
        this.smallBlindAmount = smallBlindAmount;
        this.bigBlindAmount = bigBlindAmount;
        this.minHolding = minHolding;
        this.maxHolding = maxHolding;
        this.maxPlayersCount = maxPlayersCount;
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
}
