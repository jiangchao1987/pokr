package com.yanchuanli.games.pokr.core;

import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.util.NotificationCenter;
import com.yanchuanli.games.pokr.util.Util;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class Dealer extends Thread {

    private static Logger log = Logger.getLogger(Dealer.class);

    private boolean started = false;

    private Deck deck;

    private List<Player> players;

    public Dealer() {
        deck = new Deck();
        deck.shuffle();
        players = new ArrayList<>();
    }

    public void run() {
        started = true;

        List<Card> cardsOnDesk = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Card card = deck.dealCard();
            cardsOnDesk.add(card);
        }

        log.info("Table:" + Util.cardsToString(cardsOnDesk));

        for (Player p : players) {
//            Util.sendMessage(p.getSession(), Util.cardsToString(cardsOnDesk) + "\n");
        	NotificationCenter.act(p.getSession(), Util.cardsToString(cardsOnDesk) + "\n");
        }

        round(players, deck, 2);
        log.debug("起手牌 ...");
        log.debug("hand1:" + players.get(0).getHand().toChineseString());
        log.debug("hand2:" + players.get(1).getHand().toChineseString());

        Card card4 = deck.dealCard();
        cardsOnDesk.add(card4);
        log.debug("table:" + Util.cardsToString(cardsOnDesk));
        for (Player p : players) {
//            Util.sendMessage(p.getSession(), Util.cardsToString(cardsOnDesk) + "\n");
        	NotificationCenter.act(p.getSession(), Util.cardsToString(cardsOnDesk) + "\n");
        }

        Card card5 = deck.dealCard();
        cardsOnDesk.add(card5);
        log.debug("table:" + Util.cardsToString(cardsOnDesk));

        compare(players, cardsOnDesk);

    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    private void round(List<Player> players, Deck deck, int cardCount) {
        for (Player player : players) {
            for (int i = 0; i < cardCount; i++) {
                Card card = deck.dealCard();
                player.getHand().addCard(card);
            }
        }
    }

    private void compare(List<Player> players, List<Card> cardOnsTable) {
        HandEvaluator handEval = new HandEvaluator();
        for (Player p : players) {

            for (Card card : cardOnsTable) {
                p.getHand().addCard(card);
            }

            Hand bestHand = handEval.getBest5CardHand(p.getHand());
            p.setBestHand(bestHand);
            log.info(bestHand.toChineseString());
            log.info(handEval.nameHand(bestHand));
//            Util.sendMessage(p.getSession(), bestHand.toChineseString()+ "\n");
//            Util.sendMessage(p.getSession(), handEval.nameHand(bestHand)+ "\n");
            NotificationCenter.act(p.getSession(), bestHand.toChineseString()+ "\n");
            NotificationCenter.act(p.getSession(), handEval.nameHand(bestHand)+ "\n");
        }


        Hand hand1 = players.get(0).getHand();
        Hand hand2 = players.get(1).getHand();
        int result = handEval.compareHands(hand1, hand2);
        switch (result) {
            case 1:
                log.debug("hand1 win");
//                Util.sendMessage(players.get(0).getSession(), "you win");
//                Util.sendMessage(players.get(1).getSession(), "you lose");
                NotificationCenter.act(players.get(0).getSession(), "you win");
                NotificationCenter.act(players.get(1).getSession(), "you lose");
                break;
            case 2:
                log.debug("hand2 win");
//                Util.sendMessage(players.get(1).getSession(), "you lose");
//                Util.sendMessage(players.get(0).getSession(), "you win");
                NotificationCenter.act(players.get(1).getSession(), "you lose");
                NotificationCenter.act(players.get(0).getSession(), "you win");
                break;
            case 0:
                log.debug("tie");
//                Util.sendMessage(players.get(0).getSession(), "tie");
//                Util.sendMessage(players.get(1).getSession(), "tie");
                NotificationCenter.act(players.get(0).getSession(), "tie");
                NotificationCenter.act(players.get(1).getSession(), "tie");
                break;
        }
    }
}
