package com.yanchuanli.games.pokr.model;

import com.google.code.tempusfugit.temporal.Duration;
import com.yanchuanli.games.pokr.basic.Hand;
import com.yanchuanli.games.pokr.util.Config;
import com.yanchuanli.games.pokr.util.NotificationCenter;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import java.util.Scanner;
import java.util.Set;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */

public class Player {

    private static Logger log = Logger.getLogger(Player.class);
    private int globalId;
    private String udid;
    private String name;
    private IoSession session;
    private Hand hand;
    private Hand bestHand;
    private int handRank;
    private boolean alive;
    private int money;
    private int bet;
    private String input;
    private String nameOfHand;
    private int exp;
    private int winCount;
    private int loseCount;
    private int historicalBestHandRank;
    private String historicalBestHand;
    private int maxWin;
    private String avatar;

    public Player(String id, String name) {
        this.udid = id;
        this.name = name;
        hand = new Hand();
        this.handRank = Integer.MIN_VALUE;
        this.alive = true;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IoSession getSession() {
        return session;
    }

    public void setSession(IoSession session) {
        this.session = session;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public Hand getBestHand() {
        return bestHand;
    }

    public void setBestHand(Hand bestHand) {
        this.bestHand = bestHand;
    }

    public int getHandRank() {
        return handRank;
    }

    public void setHandRank(int handRank) {
        this.handRank = handRank;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    public int getLoseCount() {
        return loseCount;
    }

    public void setLoseCount(int loseCount) {
        this.loseCount = loseCount;
    }

    public int getHistoricalBestHandRank() {
        return historicalBestHandRank;
    }

    public void setHistoricalBestHandRank(int historicalBestHandRank) {
        this.historicalBestHandRank = historicalBestHandRank;
    }

    public String getHistoricalBestHand() {
        return historicalBestHand;
    }

    public void setHistoricalBestHand(String historicalBestHand) {
        this.historicalBestHand = historicalBestHand;
    }

    public int getMaxWin() {
        return maxWin;
    }

    public void setMaxWin(int maxWin) {
        this.maxWin = maxWin;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Action act(Set<Action> actions, int minBet, int currentBet, int moneyOnTable, Duration bettingDuration, Duration inactivityCheckInterval) {
        int counter = 0;
        int sleepCount = (int) (bettingDuration.inMillis() / inactivityCheckInterval.inMillis());
        log.debug(bettingDuration.inMillis() + "/" + inactivityCheckInterval.inMillis() + "=" + sleepCount);
        String actionStr = "";
        Action result;

        for (Action action : actions) {
            actionStr = actionStr + action.getVerb() + "_";
        }
        log.debug(actionStr);

        // notify this user for allowed actions
        NotificationCenter.act(this.getSession(), this.getUdid() + "," + this.getName() + "," + actionStr + "," + moneyOnTable);

        if (Config.offlineDebug) {
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine();
        } else {

            while (getInput() == null && counter < sleepCount) {
                try {
                    Thread.sleep(inactivityCheckInterval.inMillis());
                    counter++;
//                    log.debug("waiting for " + name + " ...");
                } catch (InterruptedException e) {
                    log.error(e);
                }
            }
        }

        if (counter >= sleepCount && input == null) {
            result = Action.FOLD;
        } else {
            log.debug(name + " input:[" + input + "]");
            if (input.startsWith("ca")) {
                money -= currentBet;
                setBet(currentBet);
                result = Action.CALL;
            } else if (input.startsWith("c")) {
                result = Action.CHECK;
            } else if (input.startsWith("f")) {
                result = Action.FOLD;
            } else if (input.startsWith("r")) {
                String[] inputs = input.split(":");
                setBet(Integer.parseInt(inputs[1]));
                result = Action.RAISE;
                money -= bet;
            } else {
                String[] inputs = input.split(":");
                setBet(Integer.parseInt(inputs[1]));
                result = Action.BET;
                money -= bet;
            }
        }


        input = null;

        return result;


    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public void win(int bet) {
        this.money += bet;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getNameOfHand() {
        return nameOfHand;
    }

    public void setNameOfHand(String nameOfHand) {
        this.nameOfHand = nameOfHand;
    }

    public int getGlobalId() {
        return globalId;
    }

    public void setGlobalId(int globalId) {
        this.globalId = globalId;
    }

    @Override
    public String toString() {
        return "Player [globalId=" + globalId + ", udid=" + udid + ", name=" + name
                + ", session=" + session + ", hand=" + hand + ", bestHand="
                + bestHand + ", handRank=" + handRank + ", alive=" + alive
                + ", money=" + money + ", bet=" + bet + ", input=" + input
                + ", nameOfHand=" + nameOfHand + ", exp=" + exp + ", winCount="
                + winCount + ", loseCount=" + loseCount
                + ", historicalBestHandRank=" + historicalBestHandRank
                + ", historicalBestHand=" + historicalBestHand + ", maxWin="
                + maxWin + ", avatar=" + avatar + "]";
    }

//    @Override
//    public String toString() {
//        return "Player{" +
//                "udid='" + udid + '\'' +
//                ", name='" + name + '\'' +
//                ", bestHand=" + bestHand.toChineseString() +
//                ", handRank=" + handRank +
//                ", alive=" + alive +
//                ", money=" + money +
//                ", bet=" + bet +
//                '}';
//    }

}
