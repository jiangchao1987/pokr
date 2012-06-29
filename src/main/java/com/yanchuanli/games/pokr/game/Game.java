package com.yanchuanli.games.pokr.game;

import com.google.code.tempusfugit.temporal.Duration;
import com.yanchuanli.games.pokr.basic.Card;
import com.yanchuanli.games.pokr.basic.Deck;
import com.yanchuanli.games.pokr.basic.PlayerRankComparator;
import com.yanchuanli.games.pokr.dao.PlayerDao;
import com.yanchuanli.games.pokr.model.Action;
import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.util.NotificationCenter;
import com.yanchuanli.games.pokr.util.Util;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-13
 */

public class Game implements Runnable {

    private GameConfig gc;

    private static Logger log = Logger.getLogger(Game.class);
    private List<Player> activePlayers;
    private List<Player> availablePlayers;
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
        activePlayers = new CopyOnWriteArrayList<>();
        availablePlayers = new CopyOnWriteArrayList<>();
        cardsOnTable = new ArrayList<>();
        deck = new Deck();
        comparator = new PlayerRankComparator();
    }

    public void addPlayer(Player player) {
        availablePlayers.add(player);
    }


    public void start() {

        reset();
        sayHello();

        // rotate markCurrentDealer position
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
        if (activePlayers.size() > 1) {
            deal3FlipCards();
            doBettingRound();
            // flop the betting round
            // deal the turn card (4th) on the table
            if (activePlayers.size() > 1) {

                dealTurnCard();
                doBettingRound();
                if (activePlayers.size() > 1) {

                    dealRiverCard();
                    doBettingRound();
                    if (activePlayers.size() > 1) {
                        bet = 0;
                        shutdown();
                    }
                }
            }
        }


    }

    private void rotateDealer() {
        dealerPosition = dealerPosition++ % activePlayers.size();
        actorPosition = dealerPosition;
        Player dealer = activePlayers.get(dealerPosition);

        NotificationCenter.markCurrentDealer(activePlayers, dealer.getUdid());
        int smallBlindIndex = (actorPosition + 1) % activePlayers.size();
        int bigBlindIndex = (actorPosition + 2) % activePlayers.size();
        Player smallBlind = activePlayers.get(smallBlindIndex);
        Player bigBlind = activePlayers.get(bigBlindIndex);
        NotificationCenter.markSmallBlind(activePlayers, smallBlind.getUdid());
        NotificationCenter.markBigBlind(activePlayers, bigBlind.getUdid());
        log.debug("[RotateDealer] current markCurrentDealer:" + dealerPosition);
    }

    private void deal2Cards() {
        for (Player player : activePlayers) {
            for (int i = 0; i < 2; i++) {
                Card card = deck.dealCard();
                player.getHand().addCard(card);
            }
            log.debug(player.getName() + " got " + player.getHand().toChineseString());
            NotificationCenter.deal2Cards(player.getSession(), player.getUdid() + "," + player.getName() + "," + player.getHand().getGIndexes());
        }
        NotificationCenter.deal2CardsOnAllDevices(activePlayers, actor.getUdid());
    }

    private void deal3FlipCards() {
        for (int i = 0; i < 3; i++) {
            Card card = deck.dealCard();
            cardsOnTable.add(card);
        }
        log.debug("OnTable:" + Util.cardsToString(cardsOnTable) + " bet:" + bet + " MoneyOnTable:" + moneyOnTable);
        NotificationCenter.deal3FlipCards(activePlayers, Util.cardsToGIndexes(cardsOnTable) + "," + bet + "," + moneyOnTable);
    }

    private void dealTurnCard() {
        Card card = deck.dealCard();
        cardsOnTable.add(card);
        log.debug("OnTable-Turn:" + Util.cardsToString(cardsOnTable) + " bet:" + bet + " MoneyOnTable:" + moneyOnTable);
        NotificationCenter.dealTurnCard(activePlayers, Util.cardsToGIndexes(cardsOnTable) + "," + bet + "," + moneyOnTable);
    }

    private void dealRiverCard() {
        Card card = deck.dealCard();
        cardsOnTable.add(card);
        log.debug("OnTable-River:" + Util.cardsToString(cardsOnTable) + " bet:" + bet + " MoneyOnTable:" + moneyOnTable);
        NotificationCenter.dealRiverCard(activePlayers, Util.cardsToGIndexes(cardsOnTable) + "," + bet + "," + moneyOnTable);
    }

    private void shutdown() {

        log.debug("OnTable: " + Util.cardsToString(cardsOnTable));

        List<Player> results = new ArrayList<>();

        for (Player player : activePlayers) {
            for (Card card : cardsOnTable) {
                player.getHand().addCard(card);
            }
            results.add(player);
        }


        Collections.sort(results, comparator);

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < results.size(); i++) {
            Player player = results.get(i);
            String wininfo = "#" + String.valueOf(i + 1) + " " + player.getName() + " " + player.getBestHandRank() + " " + player.getBestHand().toChineseString() + " " + player.getNameOfHand();
            log.debug(wininfo);
            sb.append(player.getUdid() + "," + player.getName() + "," + player.getNameOfHand() + ";");
        }
        NotificationCenter.gameover(results, sb.toString());

        for (int i = 0; i < results.size(); i++) {
            if (i == 0) {
                Player winner = results.get(i);
                log.debug(winner.getName() + " wins!");
                PlayerDao.updateBestHandOfPlayer(winner);
                PlayerDao.updateMaxWin(winner.getUdid(), moneyOnTable);
                NotificationCenter.winorlose(results.get(i).getSession(), results.get(i).getUdid() + "," + results.get(i).getName() + ",1", 10);
            } else {
                log.debug(results.get(i).getName() + " loses!");
                NotificationCenter.winorlose(results.get(i).getSession(), results.get(i).getUdid() + "," + results.get(i).getName() + ",0", 10);
            }
        }

        results.clear();
        gaming = false;
    }

    private void reset() {
        deck.reset();
        deck.shuffle();

        moneyOnTable = 0;
        cardsOnTable.clear();
        activePlayers.clear();
        for (Player player : availablePlayers) {
            player.reset();

        }
        log.debug("Game has been resetted ...");
    }

    private void doBettingRound() {
        int playersToAct = activePlayers.size();
        actorPosition = dealerPosition;
        bet = 0;
        while (playersToAct > 0) {
            //rotate the actor

            rotateActor();
            log.debug("playersToAct: " + playersToAct + " id: " + actor.getUdid()
                    + " name: "
                    + actor.getName());

            Set<Action> allowedActions = getAllowedActions(actor);
            List<Player> playersToForward = new ArrayList<>();
            for (Player player : this.activePlayers) {
                if (player != actor) {
                    playersToForward.add(player);
                }
            }

            NotificationCenter.otherPlayerStartAction(playersToForward, actor.getUdid());
            Action action = actor.act(allowedActions, MIN_BET, bet, moneyOnTable, gc.getBettingDuration(), gc.getInactivityCheckInterval());

            log.debug(" id: " + actor.getUdid() + " name: " + actor.getName()
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
                    this.activePlayers.remove(actor);
                    actorPosition--;
                    if (this.activePlayers.size() == 1) {
                        log.debug(this.activePlayers.get(0).getName() + " win ...");
                        playersToAct = 0;
                        NotificationCenter.winorlose(this.activePlayers.get(0).getSession(), this.activePlayers.get(0).getUdid() + "," + this.activePlayers.get(0).getName() + ",1", 10);
                    }
                    break;
            }
            String info = actor.getUdid() + "," + action.getVerb() + ":" + actor.getBet() + "," + moneyOnTable;

            NotificationCenter.forwardAction(playersToForward, info);
            playersToForward.clear();
            playersToForward = null;
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
        if (activePlayers.size() > 0) {
            do {
                actorPosition = (actorPosition + 1) % activePlayers.size();
                actor = activePlayers.get(actorPosition);
            } while (!activePlayers.contains(actor));

        } else {
            // Should never happen.
            throw new IllegalStateException("No active activePlayers left");
        }
    }


    private void postSmallBlind() {
        actor.setBet(gc.getSmallBlindAmount());
        moneyOnTable += actor.getBet();
        String info = actor.getUdid() + "," + Action.SMALL_BLIND.getVerb() + ":" + actor.getBet() + "," + moneyOnTable;
        NotificationCenter.forwardAction(activePlayers, info);
    }

    private void postBigBlind() {
        actor.setBet(gc.getBigBlindAmount());
        moneyOnTable += actor.getBet();
        String info = actor.getUdid() + "," + Action.BIG_BLIND.getVerb() + ":" + actor.getBet() + "," + moneyOnTable;
        NotificationCenter.forwardAction(activePlayers, info);
    }

    private void sayHello() {
        gaming = true;
        for (Player player : availablePlayers) {
            if (player.isAlive()) {
                activePlayers.add(player);
            } else {
                availablePlayers.remove(player);
            }
        }
        String info = "";
        for (Player player : activePlayers) {
            info = info + player.getUdid() + "," + player.getName() + "," + player.getMoney() + ";";
        }
        NotificationCenter.sayHello(activePlayers, info);

    }


    public String getName() {
        return gc.getName();
    }

    public List<Player> getActivePlayers() {
        return activePlayers;
    }

    @Override
    public void run() {
        try {
            while (!stop) {

                checkAvailablePlayers();
                if (availablePlayers.size() >= 2) {
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

                }
                try {
                    Thread.sleep(gc.getGameCheckInterval().inMillis());
                } catch (InterruptedException e) {
                    log.error(e);
                }

            }
        } catch (Exception e) {
            log.error(e);
        }

    }

    public void stopGame() {
        stop = true;
    }

    public List<Player> getAvailablePlayers() {
        return availablePlayers;
    }

    private void checkAvailablePlayers() {
        for (Player player : availablePlayers) {
            if (!player.isAlive()) {
                availablePlayers.remove(player);
            }
        }
        log.debug(availablePlayers.size() + " players are waiting in " + gc.getName() + " ...");
    }

    public void removePlayer(Player player) {
        for (Player aplayer : activePlayers) {
            if (aplayer.getUdid().equals(player.getUdid())) {
                activePlayers.remove(aplayer);
                break;
            }
        }
    }
}