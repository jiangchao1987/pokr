package com.yanchuanli.games.pokr.model;

import com.yanchuanli.games.pokr.core.Hand;
import org.apache.mina.core.session.IoSession;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class Player {

    private String id;
    private String name;
    private IoSession session;
    private Hand hand;
    private Hand bestHand;

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        hand = new Hand();
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
}
