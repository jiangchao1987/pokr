package com.yanchuanli.games.pokr.model;

import com.yanchuanli.games.pokr.core.Hand;
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


    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        hand = new Hand();
        handRank = Integer.MIN_VALUE;
        alive = true;
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
        log.debug("allowed actions:" + actionStr);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.startsWith("c")) {
            result = Action.CHECK;
        } else if (input.startsWith("f")) {
            result = Action.FOLD;
        } else {
            result = Action.BET;
        }
        return result;
    }
}
