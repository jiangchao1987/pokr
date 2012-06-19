package com.yanchuanli.games.pokr.model;

import com.yanchuanli.games.pokr.core.Hand;
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
    private String id;
    private String name;
    private IoSession session;
    private Hand hand;
    private Hand bestHand;
    private int handRank;
    private boolean alive;
    private int money;
    private int betThisTime;
    private int betThisRound;
    private String input;
    private String nameOfHand;
    private String avatar;


    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        hand = new Hand();
        this.handRank = Integer.MIN_VALUE;
        this.alive = true;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Action act(Set<Action> actions, int minBet, int currentBet) {

        String actionStr = "";
        Action result;

        for (Action action : actions) {
            actionStr = actionStr + action.getVerb() + " ";
        }
        log.debug("allowed actions for \"" + getName() + "\" :" + actionStr);
        NotificationCenter.notifyPlayer(this, actionStr);

        if (Config.offlineDebug) {
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine();
        } else {
            int counter = 0;
            while (getInput() == null && counter < 30) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    log.error(e);
                }
            }
        }

        if (input.startsWith("ca")) {
            int diff = currentBet - betThisRound;
            betThisRound = currentBet;
            money -= diff;
            setBetThisTime(diff);
            result = Action.CALL;
        } else if (input.startsWith("c")) {
            result = Action.CHECK;
        } else if (input.startsWith("f")) {
            result = Action.FOLD;
        } else {
            String[] inputs = input.split(":");
            setBetThisTime(Integer.parseInt(inputs[1]));
            result = Action.RAISE;
            money -= betThisTime;
            betThisRound += betThisTime;
        }

        input = null;

        return result;


    }

    public int getBetThisTime() {
        return betThisTime;
    }

    public void setBetThisTime(int betThisTime) {
        this.betThisTime = betThisTime;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getBetThisRound() {
        return betThisRound;
    }

    public void setBetThisRound(int betThisRound) {
        this.betThisRound = betThisRound;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", bestHand=" + bestHand.toChineseString() +
                ", handRank=" + handRank +
                ", alive=" + alive +
                ", money=" + money +
                ", betThisTime=" + betThisTime +
                '}';
    }
}
