package com.jiangchao.games.pokr.game;

import com.google.code.tempusfugit.temporal.Duration;
import com.jiangchao.games.pokr.util.Util;
import com.yanchuanli.games.pokr.basic.Card;
import com.yanchuanli.games.pokr.basic.Deck;
import com.yanchuanli.games.pokr.basic.PlayerRankComparator;
import com.yanchuanli.games.pokr.model.Action;
import com.yanchuanli.games.pokr.model.Player;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Note: Game Main
 * Author: JiangChao
 * Date: 2012/6/15/13
 * Email: chaojiang@candou.com
 */
public class Game {

    private static Logger log = Logger.getLogger(Game.class);
    private List<Player> players;
    private List<Card> cardsOnTable;
    private Deck deck;
    private int dealerPosition;
    private PlayerRankComparator comparator;
    private int bet;
    private int moneyOnTable;
    private Player actor;
    private int MAX_RAISES = 1000;
    private int MIN_BET = 10;
    private int actorPosition;


    public Game() {
        players = new ArrayList<>();
        cardsOnTable = new ArrayList<>();
        deck = new Deck();
        comparator = new PlayerRankComparator();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void start() {

        sayHello();
        reset();
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
                        gameover();
                    }
                }
            }
        }


    }

    private void rotateDealer() {
        dealerPosition = dealerPosition++ % players.size();
        log.debug("[RotateDealer] current markCurrentDealer:" + dealerPosition);
    }

    private void deal2Cards() {
        for (Player player : players) {
            for (int i = 0; i < 2; i++) {
                Card card = deck.dealCard();
                player.getHand().addCard(card);
            }
            log.debug(player.getName() + " got " + player.getHand().toChineseString());
            Util.sendToAll(player.getName() + " got " + player.getHand().toChineseString());
        }
    }

    private void deal3FlipCards() {
        for (int i = 0; i < 3; i++) {
            Card card = deck.dealCard();
            cardsOnTable.add(card);
        }
        log.debug("OnTable:" + Util.cardsToString(cardsOnTable));
        Util.sendToAll("OnTable:" + Util.cardsToString(cardsOnTable));
    }

    private void dealTurnCard() {
        Card card = deck.dealCard();
        cardsOnTable.add(card);
        log.debug("OnTable-Turn:" + Util.cardsToString(cardsOnTable));
        Util.sendToAll("OnTable-Turn:" + Util.cardsToString(cardsOnTable));
    }

    private void dealRiverCard() {
        Card card = deck.dealCard();
        cardsOnTable.add(card);
        log.debug("OnTable-River:" + Util.cardsToString(cardsOnTable));
        Util.sendToAll("OnTable-River:" + Util.cardsToString(cardsOnTable));
    }

    private void gameover() {

        log.debug("OnTable: " + Util.cardsToString(cardsOnTable));

        for (Player player : players) {
            for (Card card : cardsOnTable) {
                player.getHand().addCard(card);
            }
        }

        Collections.sort(players, comparator);
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            String wininfo = "#" + String.valueOf(i + 1) + " " + player.getName() + " " + player.getBestHandRank() + " " + player.getBestHand().toChineseString();
            log.debug(wininfo);
            Util.sendMessage(player.getSession(), wininfo);
        }

        for (int i = 0; i < players.size(); i++) {
            if (i == 0) {
                log.debug(players.get(i).getName() + " wins!");
                Util.sendMessage(players.get(i).getSession(), "you win");
            } else {
                log.debug(players.get(i).getName() + " loses!");
                Util.sendMessage(players.get(i).getSession(), "you lose");
            }
        }


    }

    private void reset() {
        deck.reset();
        deck.shuffle();
        dealerPosition = 0;
        actorPosition = 0;
        cardsOnTable.clear();
        log.debug("Game has been resetted ...");
    }

    private void doBettingRound() {
        List<Player> activePlayers = getLivePlayers();
        int playersToAct = activePlayers.size();
        actorPosition = dealerPosition;
        bet = 0;
        while (playersToAct > 0) {
            //rotate the actor
            log.debug("playersToAct:" + playersToAct);
            rotateActor();
            Set<Action> allowedActions = getAllowedActions(actor);

            Action action = actor.act(allowedActions, bet, moneyOnTable, Duration.millis(20000), Duration.millis(500));
            log.debug(actor.getName() + " " + action.getVerb());
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
                    if (players.size() == 1) {
                        log.debug(players.get(0).getName() + " win ...");
                        playersToAct = 0;
                    }
                    break;
            }
        }
    }


    public Set<Action> getAllowedActions(Player player) {
//        int actorBet = actor.getBet();
        Set<Action> actions = new HashSet<Action>();
        if (bet == 0) {
            actions.add(Action.CHECK);
            actions.add(Action.RAISE);
        } else {
            /*
            if (actorBet < bet) {
                actions.add(Action.CALL);
                if (player.getMoney() < MAX_RAISES) {
                    actions.add(Action.RAISE);
                }
            } else {
                actions.add(Action.CHECK);
                if (player.getMoney() < MAX_RAISES) {
                    actions.add(Action.RAISE);
                }
            }
            */


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

    }
}
