package com.yanchuanli.games.pokr.game;

import com.google.code.tempusfugit.temporal.Duration;
import com.yanchuanli.games.pokr.basic.Card;
import com.yanchuanli.games.pokr.basic.Deck;
import com.yanchuanli.games.pokr.basic.PlayerRankComparator;
import com.yanchuanli.games.pokr.model.Action;
import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.util.NotificationCenter;
import com.yanchuanli.games.pokr.util.Util;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-13
 */

public class Game implements Runnable {

    private GameConfig gc;

    private static Logger log = Logger.getLogger(Game.class);
    private List<Player> players;
    private List<Card> cardsOnTable;
    private Deck deck;
    private int dealerPosition;
    private PlayerRankComparator comparator;
    private int bet;
    private int moneyOnTable;
    private Player actor;

    private int MIN_BET = 10;
    private int actorPosition;
    private boolean gaming = false;
    private boolean stop = false;


    public Game(GameConfig gc) {
        this.gc = gc;
        players = new ArrayList<>();
        cardsOnTable = new ArrayList<>();
        deck = new Deck();
        comparator = new PlayerRankComparator();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        for (Player aplayer : players) {
            if (aplayer.getGlobalId() == player.getGlobalId()) {
                players.remove(aplayer);
                break;
            }
        }
    }

    public void start() {

        sayHello();
        reset();
        // rotate dealer position
        rotateDealer();

        // post the big blind and small blind
        rotateActor();
        postSmallBlind();

        rotateActor();
        postBigBlind();

        // deal 2 cards per player
        deal2Cards();
        doBettingRound();

        // pre flop betting round
        // deal 3 flp cards on the table
        if (players.size() > 1) {
            deal3FlipCards();
            doBettingRound();
            // flop the betting round
            // deal the turn card (4th) on the table
            if (players.size() > 1) {

                dealTurnCard();
                doBettingRound();
                if (players.size() > 1) {

                    dealRiverCard();
                    doBettingRound();
                    if (players.size() > 1) {
                        bet = 0;
                        shutdown();
                    }
                }
            }
        }


    }

    private void rotateDealer() {
        dealerPosition = dealerPosition++ % players.size();
        log.debug("[RotateDealer] current dealer:" + dealerPosition);
    }

    private void deal2Cards() {
        for (Player player : players) {
            for (int i = 0; i < 2; i++) {
                Card card = deck.dealCard();
                player.getHand().addCard(card);
            }
            log.debug(player.getName() + " got " + player.getHand().toChineseString());
            NotificationCenter.deal2Cards(player.getSession(), player.getId() + "," + player.getName() + "," + player.getHand().getGIndexes());
        }
    }

    private void deal3FlipCards() {
        for (int i = 0; i < 3; i++) {
            Card card = deck.dealCard();
            cardsOnTable.add(card);
        }
        log.debug("OnTable:" + Util.cardsToString(cardsOnTable) + " bet:" + bet + " MoneyOnTable:" + moneyOnTable);
        NotificationCenter.deal3FlipCards(players, Util.cardsToGIndexes(cardsOnTable) + "," + bet + "," + moneyOnTable);
    }

    private void dealTurnCard() {
        Card card = deck.dealCard();
        cardsOnTable.add(card);
        log.debug("OnTable-Turn:" + Util.cardsToString(cardsOnTable) + " bet:" + bet + " MoneyOnTable:" + moneyOnTable);
        NotificationCenter.dealTurnCard(players, Util.cardsToGIndexes(cardsOnTable) + "," + bet + "," + moneyOnTable);
    }

    private void dealRiverCard() {
        Card card = deck.dealCard();
        cardsOnTable.add(card);
        log.debug("OnTable-River:" + Util.cardsToString(cardsOnTable) + " bet:" + bet + " MoneyOnTable:" + moneyOnTable);
        NotificationCenter.dealRiverCard(players, Util.cardsToGIndexes(cardsOnTable) + "," + bet + "," + moneyOnTable);
    }

    private void shutdown() {

        log.debug("OnTable: " + Util.cardsToString(cardsOnTable));

        for (Player player : players) {
            for (Card card : cardsOnTable) {
                player.getHand().addCard(card);
            }
        }

        Collections.sort(players, comparator);

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            String wininfo = "#" + String.valueOf(i + 1) + " " + player.getName() + " " + player.getHandRank() + " " + player.getBestHand().toChineseString() + " " + player.getNameOfHand();
            log.debug(wininfo);
            sb.append(player.getId() + "," + player.getName() + "," + player.getNameOfHand() + ";");
        }
        NotificationCenter.gameover(players, sb.toString());

        for (int i = 0; i < players.size(); i++) {
            if (i == 0) {
                log.debug(players.get(i).getName() + " wins!");
                NotificationCenter.winorlose(players.get(i).getSession(), players.get(i).getId() + "," + players.get(i).getName() + ",1", 10);
            } else {
                log.debug(players.get(i).getName() + " loses!");
                NotificationCenter.winorlose(players.get(i).getSession(), players.get(i).getId() + "," + players.get(i).getName() + ",0", 10);
            }
        }
        gaming = false;
    }

    private void reset() {
        deck.reset();
        deck.shuffle();
        dealerPosition = 0;
        actorPosition = 0;
        cardsOnTable.clear();
        for (Player player : players) {
            player.getHand().makeEmpty();
            player.getBestHand().makeEmpty();
        }
        log.debug("Game has been resetted ...");
    }

    private void doBettingRound() {
        List<Player> activePlayers = getLivePlayers();
        int playersToAct = activePlayers.size();
        actorPosition = dealerPosition;
        bet = 0;
        while (playersToAct > 0) {
            //rotate the actor

            rotateActor();
            log.debug("playersToAct: " + playersToAct + " id: " + actor.getId()
                    + " name: "
                    + actor.getName());
            Set<Action> allowedActions = getAllowedActions(actor);

            Action action = actor.act(allowedActions, MIN_BET, bet, moneyOnTable);
            log.debug(" id: " + actor.getId() + " name: " + actor.getName()
                    + " action: " + action.getVerb());
            playersToAct--;

            switch (action) {
                case CHECK:
                    // do nothing
                    break;
                case CALL:
                    moneyOnTable += actor.getBet();
                    break;
                case BET:
                    bet = actor.getBet();
                    moneyOnTable += actor.getBet();
                    playersToAct = activePlayers.size() - 1;
                    break;
                case RAISE:
                    bet = actor.getBet();
                    moneyOnTable += actor.getBet();
                    playersToAct = activePlayers.size() - 1;
                    break;
                case FOLD:
                    actor.getHand().makeEmpty();
                    players.remove(actor);
                    actorPosition--;
                    if (players.size() == 1) {
                        log.debug(players.get(0).getName() + " win ...");
                        playersToAct = 0;
                        NotificationCenter.winorlose(players.get(0).getSession(), players.get(0).getId() + "," + players.get(0).getName() + ",1", 10);
                    }
                    break;
            }
        }
    }


    public Set<Action> getAllowedActions(Player player) {
        Set<Action> actions = new HashSet<Action>();
        if (bet == 0) {
            actions.add(Action.CHECK);
            actions.add(Action.RAISE);
        } else {
            actions.add(Action.CALL);
            if (player.getMoney() > bet) {
                actions.add(Action.RAISE);
            }

        }
        actions.add(Action.FOLD);
        return actions;
    }

    private void rotateActor() {
        if (players.size() > 0) {
            do {
                actorPosition = (actorPosition + 1) % players.size();
                actor = players.get(actorPosition);
            } while (!players.contains(actor));

        } else {
            // Should never happen.
            throw new IllegalStateException("No active players left");
        }
    }

    private List<Player> getLivePlayers() {
        List<Player> activePlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.isAlive()) {
                activePlayers.add(player);
            }
        }
        return activePlayers;
    }

    private void postSmallBlind() {

    }

    private void postBigBlind() {

    }

    private void sayHello() {
        gaming = true;
        String info = "";
        for (Player player : players) {
            info = info + player.getId() + "," + player.getName() + "," + player.getMoney() + ";";
        }
        NotificationCenter.sayHello(players, info);
    }


    public String getName() {
        return gc.getName();
    }

    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public void run() {
        while (!stop) {
            if (players.size() >= 2) {

                try {
                    log.debug("game will start in 3 seconds ...");
                    Thread.sleep(Duration.seconds(1).inMillis());
                    log.debug("game will start in 2 seconds ...");
                    Thread.sleep(Duration.seconds(1).inMillis());
                    log.debug("game will start in 1 seconds ...");
                    Thread.sleep(Duration.seconds(1).inMillis());
                } catch (InterruptedException e) {
                    log.error(e);
                }
                start();
            } else {
                try {
                    Thread.sleep(gc.getInactivityCheckInterval().inMillis());
                } catch (InterruptedException e) {
                    log.error(e);
                }
            }
        }
    }
}