package com.yanchuanli.games.pokr.game;

import com.google.code.tempusfugit.temporal.Duration;
import com.yanchuanli.games.pokr.basic.Card;
import com.yanchuanli.games.pokr.basic.Deck;
import com.yanchuanli.games.pokr.basic.HandEvaluator;
import com.yanchuanli.games.pokr.basic.PlayerRankComparator;
import com.yanchuanli.games.pokr.dao.PlayerDao;
import com.yanchuanli.games.pokr.dao.RoomDao;
import com.yanchuanli.games.pokr.model.Action;
import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.model.Pot;
import com.yanchuanli.games.pokr.model.Record;
import com.yanchuanli.games.pokr.util.NotificationCenter;
import com.yanchuanli.games.pokr.util.Util;
import org.apache.commons.lang3.exception.ExceptionUtils;
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

    //还活在游戏中的用户
    private List<Player> activePlayers;
    //可以进入游戏的用户
    private List<Player> availablePlayers;
    //进入房间了的用户
    private Map<String, Player> waitingPlayers;
    //本次游戏的所有用户
    private Map<String, Player> allPlayersInGame;


    private List<Card> cardsOnTable;
    private Deck deck;
    private int dealerPosition;
    private PlayerRankComparator comparator;
    private int bet;
    private int moneyOnTable;
    private Player actor;

    private int actorPosition;
    private boolean gaming = false;
    private boolean stop = false;
    private HandEvaluator handEval;
    private Pot pot;


    public Game(GameConfig gc) {
        this.gc = gc;
        activePlayers = new CopyOnWriteArrayList<>();
        availablePlayers = new CopyOnWriteArrayList<>();
        allPlayersInGame = new HashMap<>();
        waitingPlayers = new HashMap<>();
        cardsOnTable = new ArrayList<>();
        pot = new Pot();
        deck = new Deck();
        comparator = new PlayerRankComparator();
        handEval = new HandEvaluator();
        pot = new Pot();
    }

    public void prepareToJoin(Player player) {
        waitingPlayers.put(player.getUdid(), player);
        StringBuilder sb = new StringBuilder();
        if (gaming) {
            for (Player aplayer : activePlayers) {
                sb.append(aplayer.getUdid()).append(",").append(aplayer.getName()).append(",").append(aplayer.getMoney()).append(",").append(aplayer.getCustomAvatar()).append(",").append(aplayer.getAvatar()).append(",").append(aplayer.getSex()).append(",").append(aplayer.getAddress()).append(";");
            }
        } else {
            for (Player aplayer : availablePlayers) {
                sb.append(aplayer.getUdid()).append(",").append(aplayer.getName()).append(",").append(aplayer.getMoney()).append(",").append(aplayer.getCustomAvatar()).append(",").append(aplayer.getAvatar()).append(",").append(aplayer.getSex()).append(",").append(aplayer.getAddress()).append(";");
            }
        }

        NotificationCenter.respondToPrepareToEnter(player.getSession(), sb.toString());
    }

    public void addPlayer(Player player) {
        if (activePlayers.size() + availablePlayers.size() <= gc.getMaxPlayersCount()) {
            waitingPlayers.remove(player.getUdid());

            availablePlayers.add(player);
            PlayerDao.buyIn(player, 10000);
            log.debug("money now:" + player.getMoney());
        } else {
            //TODO 告诉他坐下失败
        }

    }


    public void start() {

        reset();
        // notify every player in game about others
        sayHello();


        // rotate markCurrentDealer position
        rotateDealer();

        deal2Cards();

        // deal 2 cards per player

        doBettingRound(true);

        // pre flop betting round
        // deal 3 flp cards on the table
        if (activePlayers.size() > 1) {
            deal3FlipCards();
            doBettingRound(false);
            // flop the betting round
            // deal the turn card (4th) on the table
            if (activePlayers.size() > 1) {
                dealTurnCard();
                doBettingRound(false);
                if (activePlayers.size() > 1) {
                    dealRiverCard();
                    doBettingRound(false);
                    if (activePlayers.size() > 1) {
                        bet = 0;
                        showdown();
                    }
                } else {
                    log.debug(134);
                    showdown();
                }
            } else {
                log.debug(137);
                showdown();
            }
        } else {
            log.debug(140);
            showdown();
        }


    }

    private void rotateDealer() {
        dealerPosition = dealerPosition++ % activePlayers.size();
        actorPosition = dealerPosition;
        Player dealer = activePlayers.get(actorPosition);

        NotificationCenter.markCurrentDealer(activePlayers, dealer.getUdid());
        int smallBlindIndex = (actorPosition + 1) % activePlayers.size();
        int bigBlindIndex = (actorPosition + 2) % activePlayers.size();
        Player smallBlind = activePlayers.get(smallBlindIndex);
        smallBlind.setSmallBlind(true);
        Player bigBlind = activePlayers.get(bigBlindIndex);
        bigBlind.setBigBlind(true);
        NotificationCenter.markSmallBlind(activePlayers, smallBlind.getUdid());
        NotificationCenter.markBigBlind(activePlayers, bigBlind.getUdid());


        log.debug("[RotateDealer] current markCurrentDealer:" + dealerPosition);
        log.debug("current deal¢er:" + dealer.getName());
        log.debug("current smallblind:" + smallBlind.getName());
        log.debug("current bigblind:" + bigBlind.getName());

        rotateActor();
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

    private void showdown() {


        pot.finish();

        log.debug("OnTable: " + Util.cardsToString(cardsOnTable));

        List<Player> results = new ArrayList<>();

        StringBuilder cardsInfo = new StringBuilder();
        for (Player player : activePlayers) {
            if (player.isAlive()) {
                cardsInfo.append(player.getUdid()).append(",").append(player.getHand().getGIndexes()).append(";");
                for (Card card : cardsOnTable) {
                    player.getHand().addCard(card);
                }
                results.add(player);
            }
        }
        NotificationCenter.show2cards(results, cardsInfo.toString());


        if (results.size() > 1) {
            List<List<Player>> rankedPlayerList = GameUtil.rankPlayers(results);
            for (int i = pot.potsCount() - 1; i >= 0; i--) {
                StringBuilder sb = new StringBuilder();
                Map<String, Integer> playersInThisPot = pot.getPotAtIndex(i);
                List<Player> playersListInThisPot = new ArrayList<>();
                boolean thisIsWinnerGroup = true;
                for (List<Player> players : rankedPlayerList) {
                    for (Player player : players) {
                        if (playersInThisPot.containsKey(player.getUdid())) {

                        } else {
                            thisIsWinnerGroup = false;
                        }
                    }
                    if (thisIsWinnerGroup) {

                        int totalMoney = pot.getMoneyAtIndex(i);
                        int moneyForEveryOne = totalMoney / players.size();
                        log.debug("total money:" + totalMoney);

                        for (Player player : players) {
                            PlayerDao.cashBack(player, moneyForEveryOne);
                            sb.append(player.getUdid()).append(",").append(player.getNameOfBestHand()).append(",").append("2").append(",").append("0_1_2").append(",").append(String.valueOf(moneyForEveryOne)).append(";");
                            playersInThisPot.remove(player.getUdid());
                            playersListInThisPot.add(player);
                        }

                        for (String s : playersInThisPot.keySet()) {
                            for (Player player : activePlayers) {
                                if (player.getUdid().equals(s)) {
                                    sb.append(player.getUdid()).append(",").append(player.getNameOfBestHand()).append(",").append("2").append(",").append("0_1_2").append(",").append(String.valueOf(0)).append(";");
                                    playersListInThisPot.add(player);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
                //每个边池给客户端足够做动画的时间
                NotificationCenter.winorlose(playersListInThisPot, sb.toString());
                try {
                    Thread.sleep(gc.getInactivityCheckInterval().inMillis() * 2);
                } catch (InterruptedException e) {
                    log.error(ExceptionUtils.getStackTrace(e));
                }
            }
        } else {
            if (results.size() == 1) {
                Player player1 = results.get(0);
                if (player1.getHand().size() < 5) {

                } else {
                    player1.setBestHand(handEval.getBest5CardHand(player1.getHand()));
                    player1.setNameOfBestHand(HandEvaluator.nameHandInChinese(player1.getBestHand()));
                }
            } else {

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
        allPlayersInGame.clear();
        pot.clear();
        for (Player player : availablePlayers) {
            player.reset();

        }
        log.debug("Game has been resetted ...");
    }

    private void doBettingRound(boolean preflop) {

        int playersToAct = activePlayers.size();
        actorPosition = dealerPosition;
        bet = 0;

        for (Player player : activePlayers) {
            player.setBetThisRound(0);
        }

        while (playersToAct > 0) {
            //rotate the actor
            try {
                playersToAct--;
                rotateActor();
                log.debug("playersToAct: " + playersToAct + " id: " + actor.getUdid() + " name: " + actor.getName());


                Set<Action> allowedActions = getAllowedActions(actor);

                List<Player> playersToForward = new ArrayList<>();
                for (Player player : this.activePlayers) {
                    if (player != actor) {
                        playersToForward.add(player);
                    }
                }


                Action action = null;

                if (preflop) {
                    if (actor.isSmallBlind()) {
                        action = actor.act(allowedActions, bet, moneyOnTable, gc.getBettingDuration(), gc.getInactivityCheckInterval(), 1, gc.getSmallBlindAmount());
                        actor.setSmallBlind(false);
                        playersToAct++;
                        log.debug("small blind playersToAct:" + playersToAct);
                        // 再让小盲跟一次
                    } else if (actor.isBigBlind()) {
                        action = actor.act(allowedActions, bet, moneyOnTable, gc.getBettingDuration(), gc.getInactivityCheckInterval(), 2, gc.getBigBlindAmount());
                        actor.setBigBlind(false);
                    } else {
                        NotificationCenter.otherPlayerStartAction(playersToForward, actor.getUdid());
                        action = actor.act(allowedActions, bet, moneyOnTable, gc.getBettingDuration(), gc.getInactivityCheckInterval(), 0, 0);
                    }
                } else {

                    if (allowedActions.size() == 1 && allowedActions.contains(Action.CONTINUE)) {
                        action = actor.act(allowedActions, bet, moneyOnTable, gc.getBettingDuration(), gc.getInactivityCheckInterval(), 3, 0);
                    } else {
                        NotificationCenter.otherPlayerStartAction(playersToForward, actor.getUdid());
                        action = actor.act(allowedActions, bet, moneyOnTable, gc.getBettingDuration(), gc.getInactivityCheckInterval(), 0, 0);
                    }

                }

                Record record = new Record(actor.getUdid(), action.getVerbType(), actor.getBetThisTime());
                pot.addRecord(record);

                log.debug(" id: " + actor.getUdid() + " name: " + actor.getName() + " has " + action.getVerb());

                switch (action) {
                    case CHECK:
                        // do nothing
                        break;
                    case CALL:
                        moneyOnTable += actor.getBetThisTime();
                        break;
                    case BET:
                        bet = bet >= actor.getBetThisTime() ? bet : actor.getBetThisTime();
                        moneyOnTable += actor.getBetThisTime();
                        playersToAct = activePlayers.size() - 1;
                        break;
                    case RAISE:
                        bet = bet >= actor.getBetThisTime() ? bet : actor.getBetThisTime();
                        moneyOnTable += actor.getBetThisTime();
                        playersToAct = activePlayers.size() - 1;
                        break;
                    case FOLD:
                        actor.getHand().makeEmpty();
                        this.activePlayers.remove(actor);
                        actorPosition--;
                        if (this.activePlayers.size() == 1) {
                            log.debug(this.activePlayers.get(0).getName() + " win ...");
                            playersToAct = 0;
                        }
                        break;
                    case SMALL_BLIND:
                        bet = actor.getBetThisTime();
                        moneyOnTable += actor.getBetThisTime();
                        break;
                    case BIG_BLIND:
                        bet = actor.getBetThisTime();
                        moneyOnTable += actor.getBetThisTime();
                        break;
                    case ALLIN:
                        if (actor.getBetThisTime() > bet) {
                            playersToAct = activePlayers.size() - 1;
                            bet = actor.getBetThisTime();
                        }
                        moneyOnTable += actor.getBetThisTime();
                        break;
                    case CONTINUE:
                        break;

                }

                //扣钱

                String info = actor.getUdid() + "," + action.getVerb() + ":" + actor.getBetThisTime() + "," + moneyOnTable;

                if (action.getName().equals(Action.SMALL_BLIND.getName())) {
                    NotificationCenter.paySmallBlind(activePlayers, info);
                } else if (action.getName().equals(Action.BIG_BLIND.getName())) {
                    NotificationCenter.payBigBlind(activePlayers, info);
                } else {
                    NotificationCenter.forwardAction(playersToForward, info);
                    playersToForward.clear();
                    playersToForward = null;
                }

                //reset actor's bet
                actor.setBetThisTime(0);
            } catch (IllegalStateException e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }


        }

        pot.buildPotList();
    }


    public Set<Action> getAllowedActions(Player player) {
        Set<Action> actions = new HashSet<Action>();
        if (player.getMoney() != 0) {
            if (bet == 0) {
                actions.add(Action.CHECK);
                actions.add(Action.RAISE);
            } else {
                if (player.getMoney() >= bet) {
                    actions.add(Action.CALL);
                }
                if (player.getMoney() >= bet * 2) {
                    actions.add(Action.RAISE);
                }

                if (player.getMoney() > 0) {
                    actions.add(Action.ALLIN);
                }
            }

            actions.add(Action.FOLD);
        } else {
            actions.add(Action.CONTINUE);
        }

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


    private void sayHello() {
        gaming = true;

        for (Player player : availablePlayers) {
            if (player.isAlive()) {
                if (activePlayers.size() < gc.getMaxPlayersCount()) {
                    activePlayers.add(player);
                    player.setRoomid(gc.getId());
                    allPlayersInGame.put(player.getUdid(), player);
                }
            } else {
                availablePlayers.remove(player);
            }
        }
        String info = "";
        for (Player player : activePlayers) {
            info = info + player.getUdid() + "," + player.getName() + "," + player.getMoney() + "," + player.getCustomAvatar() + "," + player.getAvatar() + "," + player.getSex() + "," + player.getAddress() + ";";
        }


        RoomDao.updateCurrentPlayerCount(gc.getId(), activePlayers.size());
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
                        log.error(ExceptionUtils.getStackTrace(e));
                    }
                    start();
                }
                try {
                    Thread.sleep(gc.getGameCheckInterval().inMillis());
                } catch (InterruptedException e) {
                    log.error(ExceptionUtils.getStackTrace(e));
                }

            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
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
    }

    public void removePlayer(Player player) {
        for (Player aplayer : activePlayers) {
            if (aplayer.getUdid().equals(player.getUdid())) {
                activePlayers.remove(aplayer);
                RoomDao.updateCurrentPlayerCount(gc.getId(), activePlayers.size());
                break;
            }
        }

        for (Player aplayer : availablePlayers) {
            if (aplayer.getUdid().equals(player.getUdid())) {
                availablePlayers.remove(aplayer);
                RoomDao.updateCurrentPlayerCount(gc.getId(), availablePlayers.size());
                break;
            }
        }
    }
}