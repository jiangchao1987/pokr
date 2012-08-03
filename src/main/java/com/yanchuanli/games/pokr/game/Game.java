package com.yanchuanli.games.pokr.game;

import com.google.code.tempusfugit.temporal.Duration;
import com.yanchuanli.games.pokr.basic.Card;
import com.yanchuanli.games.pokr.basic.Deck;
import com.yanchuanli.games.pokr.basic.HandEvaluator;
import com.yanchuanli.games.pokr.basic.PlayerRankComparator;
import com.yanchuanli.games.pokr.dao.PlayerDao;
import com.yanchuanli.games.pokr.dao.RoomDao;
import com.yanchuanli.games.pokr.dto.PlayerDTO;
import com.yanchuanli.games.pokr.model.Action;
import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.model.Pot;
import com.yanchuanli.games.pokr.model.Record;
import com.yanchuanli.games.pokr.util.Config;
import com.yanchuanli.games.pokr.util.DTOUtil;
import com.yanchuanli.games.pokr.util.NotificationCenter;
import com.yanchuanli.games.pokr.util.Util;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
    //进入房间了站着的用户
    private Map<String, Player> standingPlayers;
    //坐下但是等下局的用户
    private Map<String, Player> waitingPlayers;

    private Map<Integer, String> table;


    //本次游戏的所有用户
    private List<Player> allPlayersInGame;
    //本次游戏的所有赢钱用户
    private Set<String> allWinningUsers;


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
    private Random random;


    public Game(GameConfig gc) {
        this.gc = gc;
        activePlayers = new CopyOnWriteArrayList<>();
        allPlayersInGame = new CopyOnWriteArrayList<>();
        allWinningUsers = new HashSet<>();
        waitingPlayers = new ConcurrentHashMap<>();
        standingPlayers = new ConcurrentHashMap<>();
        table = new ConcurrentHashMap<>();
        for (int i = 0; i < gc.getMaxPlayersCount(); i++) {
            table.put(i, Config.EMPTY_SEAT);
        }
        cardsOnTable = new ArrayList<>();
        pot = new Pot();
        deck = new Deck();
        comparator = new PlayerRankComparator();
        handEval = new HandEvaluator();
        pot = new Pot();
        random = new Random();
    }

    public void enterRoom(Player player) {
        player.setRoomid(gc.getId());
        standingPlayers.put(player.getUdid(), player);

        List<PlayerDTO> playerDTOs = new ArrayList<>();
        for (Player aplayer : activePlayers) {
            playerDTOs.add(new PlayerDTO(aplayer, Config.GAMESTATUS_ACTIVE));
        }
        for (String s : waitingPlayers.keySet()) {
            Player aplayer = waitingPlayers.get(s);
            playerDTOs.add(new PlayerDTO(aplayer, Config.GAMESTATUS_WAITING));
        }
        NotificationCenter.respondToPrepareToEnter(player.getSession(), DTOUtil.writeValue(playerDTOs));
        log.debug(DTOUtil.writeValue(playerDTOs));

        allPlayersInGame.add(player);
    }

    public boolean buyIn(Player player, int amount) {
        boolean result = PlayerDao.buyIn(player, amount);
        return result;
    }

    public synchronized void sitDown(Player player, int index) {
        log.debug(player.getName() + ":" + player.getMoneyInGame() + " tries to sit down at " + index + "...");
        boolean sitDownFailed = false;


        if (index >= 0 && index <= gc.getMaxPlayersCount()) {
            if (index != 0 && !table.get(index).equals(Config.EMPTY_SEAT)) {
                log.debug("seat " + index + " is taken already");
                sitDownFailed = true;
                log.debug(table);
            }
        } else {
            log.debug("wrong index:" + index);
            sitDownFailed = true;
        }


        if (gaming) {
            log.debug("gaming ...");
            if (activePlayers.size() < gc.getMaxPlayersCount()) {
                //防止同一个人因黑客多次坐下
                for (Player aplayer : activePlayers) {
                    if (aplayer.getUdid().equals(player.getUdid())) {
                        log.debug(player.getName() + " has already sitted down ...");
                        sitDownFailed = true;
                        break;
                    }
                }
                if (!sitDownFailed) {
                    if (player.getMoneyInGame() > 0) {

                    } else {
                        log.debug(player.getName() + " sitdown failed because of empty pocket ...");
                        sitDownFailed = true;
                    }
                }
            } else {
                sitDownFailed = true;
            }


        } else {
            int freeSitsCount = gc.getMaxPlayersCount() - activePlayers.size();
            if (freeSitsCount > 0) {

            } else {
                log.debug(player.getName() + " sitdown failed because of no seats available ...");
                sitDownFailed = true;
            }

        }

        if (!sitDownFailed) {
            standingPlayers.remove(player.getUdid());
            waitingPlayers.put(player.getUdid(), player);

            if (index == 0) {
                int randomSeat = getNextRandomSeat();
                player.setSeatIndex(randomSeat);
                table.put(randomSeat, player.getUdid());

            } else {
                player.setSeatIndex(index);
                table.put(index, player.getUdid());
            }


            List<PlayerDTO> playerDTOs = new ArrayList<>();
            for (Player aplayer : activePlayers) {
                playerDTOs.add(new PlayerDTO(aplayer, Config.GAMESTATUS_ACTIVE));
            }
            for (String s : waitingPlayers.keySet()) {
                Player aplayer = waitingPlayers.get(s);
                playerDTOs.add(new PlayerDTO(aplayer, Config.GAMESTATUS_WAITING));
            }
            RoomDao.updateCurrentPlayerCount(gc.getId(), activePlayers.size() + waitingPlayers.size());
            NotificationCenter.respondToPrepareToEnter(player.getSession(), DTOUtil.writeValue(playerDTOs));
            log.debug(DTOUtil.writeValue(playerDTOs));
        } else {
            NotificationCenter.sitDownFailed(player);
        }

    }


    private void start() {

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
        try {
            if (activePlayers.size() > 1 && !stop) {
                deal3FlipCards();
                doBettingRound(false);
                // flop the betting round
                // deal the turn card (4th) on the table
                if (activePlayers.size() > 1 && !stop) {
                    dealTurnCard();
                    doBettingRound(false);
                    if (activePlayers.size() > 1 && !stop) {
                        dealRiverCard();
                        doBettingRound(false);
                        if (activePlayers.size() > 1 && !stop) {
                            bet = 0;
                            showdown();
                        }
                    } else {
                        showdown();
                    }
                } else {
                    showdown();
                }
            } else {
                showdown();
            }
        } catch (Exception e) {
            showdown();
        }


    }

    private void rotateDealer() {
        dealerPosition = (dealerPosition + 1) % activePlayers.size();
        actorPosition = dealerPosition;
        Player dealer = activePlayers.get(actorPosition);

        NotificationCenter.markCurrentDealer(allPlayersInGame, dealer.getUdid());
        int smallBlindIndex = (actorPosition + 1) % activePlayers.size();
        int bigBlindIndex = (actorPosition + 2) % activePlayers.size();
        Player smallBlind = activePlayers.get(smallBlindIndex);
        smallBlind.setSmallBlind(true);
        Player bigBlind = activePlayers.get(bigBlindIndex);
        bigBlind.setBigBlind(true);
        NotificationCenter.markSmallBlind(allPlayersInGame, smallBlind.getUdid());
        NotificationCenter.markBigBlind(allPlayersInGame, bigBlind.getUdid());


        log.debug("[RotateDealer] current markCurrentDealer:" + dealerPosition);
        log.debug("current dealer:" + dealer.getName());
        log.debug("current smallblind:" + smallBlind.getName());
        log.debug("current bigblind:" + bigBlind.getName());

        rotateActor();
    }

    private void deal2Cards() {
        for (Player player : activePlayers) {
            for (int i = 0; i < 2; i++) {
                Card card = deck.dealCard();
                player.getHand().addCard(card);
                log.debug(player.getHand().getGIndexes());
            }
//            log.debug(player.getName() + " got " + player.getHand().toChineseString());
            NotificationCenter.deal2Cards(player.getSession(), player.getUdid() + "," + player.getName() + "," + player.getHand().getGIndexes());
        }
        NotificationCenter.deal2CardsOnAllDevices(allPlayersInGame, actor.getUdid());
    }

    private void deal3FlipCards() {
        for (int i = 0; i < 3; i++) {
            Card card = deck.dealCard();
            cardsOnTable.add(card);
        }
        log.debug("OnTable:" + Util.cardsToString(cardsOnTable) + " bet:" + bet + " MoneyOnTable:" + moneyOnTable);
        NotificationCenter.deal3FlipCards(allPlayersInGame, Util.cardsToGIndexes(cardsOnTable) + "," + bet + "," + moneyOnTable);
    }

    private void dealTurnCard() {
        Card card = deck.dealCard();
        cardsOnTable.add(card);
        log.debug("OnTable-Turn:" + Util.cardsToString(cardsOnTable) + " bet:" + bet + " MoneyOnTable:" + moneyOnTable);
        NotificationCenter.dealTurnCard(allPlayersInGame, Util.cardsToGIndexes(cardsOnTable) + "," + bet + "," + moneyOnTable);
    }

    private void dealRiverCard() {
        Card card = deck.dealCard();
        cardsOnTable.add(card);
        log.debug("OnTable-River:" + Util.cardsToString(cardsOnTable) + " bet:" + bet + " MoneyOnTable:" + moneyOnTable);
        NotificationCenter.dealRiverCard(allPlayersInGame, Util.cardsToGIndexes(cardsOnTable) + "," + bet + "," + moneyOnTable);
    }

    private void showdown() {

        log.debug("showdown ...");

        pot.finish();

        log.debug("OnTable: " + Util.cardsToString(cardsOnTable));

        List<Player> results = new ArrayList<>();

        StringBuilder cardsInfo = new StringBuilder();

        for (Player player : activePlayers) {
            if (player.isOnline()) {
                cardsInfo.append(player.getUdid()).append(",").append(player.getHand().getGIndexes()).append(";");
                for (Card card : cardsOnTable) {
                    player.getHand().addCard(card);
                }
                results.add(player);
            }
        }
        log.debug("show2cards:" + cardsInfo.toString());
        NotificationCenter.show2cards(results, cardsInfo.toString());


        if (results.size() > 1) {
            List<List<Player>> rankedPlayerList = GameUtil.rankPlayers(results);
            int[] cardsArray = new int[5];
            for (int i = 0; i < cardsOnTable.size(); i++) {
                cardsArray[i] = cardsOnTable.get(i).getIndex();
            }
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
                            player.addMoney(moneyForEveryOne);
                            PlayerDao.cashBack(player, moneyForEveryOne);
                            PlayerDao.updateMaxWin(player.getUdid(), moneyForEveryOne);
                            PlayerDao.updateWinCount(player);
                            allWinningUsers.add(player.getUdid());
                            sb.append(player.getUdid()).append(",").append(player.getNameOfBestHand()).append(",").append(String.valueOf(player.getGIndexesForOwnCardsUsedInBestFive())).append(",").append(player.getIndexesForUsedCommunityCardsInBestFive(cardsArray)).append(",").append(String.valueOf(moneyForEveryOne)).append(";");
                            playersInThisPot.remove(player.getUdid());
                            playersListInThisPot.add(player);
                        }

                        for (String s : playersInThisPot.keySet()) {
                            for (Player player : activePlayers) {
                                if (player.getUdid().equals(s)) {
                                    sb.append(player.getUdid()).append(",").append(player.getNameOfBestHand()).append(",").append(String.valueOf(player.getGIndexesForOwnCardsUsedInBestFive())).append(",").append(player.getIndexesForUsedCommunityCardsInBestFive(cardsArray)).append(",").append(String.valueOf(0)).append(";");
                                    playersListInThisPot.add(player);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }

                for (Player p : activePlayers) {
                    if (allWinningUsers.contains(p.getUdid())) {

                    } else {
                        PlayerDao.updateLoseCount(p);
                    }
                }

                //每个边池给客户端足够做动画的时间
                NotificationCenter.winorlose(playersListInThisPot, sb.toString());
                try {
                    Thread.sleep(gc.getInactivityCheckInterval().inMillis() * 4);
                } catch (InterruptedException e) {
                    log.error(ExceptionUtils.getStackTrace(e));
                }


            }
        } else {
            if (results.size() == 1) {
                Player player1 = results.get(0);
                StringBuilder sb = new StringBuilder();
                player1.addMoney(pot.getMoney());
                PlayerDao.cashBack(player1, pot.getMoney());
                PlayerDao.updateWinCount(player1);
                PlayerDao.updateWinCount(player1);
                sb.append(player1.getUdid()).append(",").append("").append(",").append("").append(",").append("").append(",").append(String.valueOf(pot.getMoney())).append(";");
                List<Player> playersListInThisPot = new ArrayList<>();
                playersListInThisPot.add(player1);
                NotificationCenter.winorlose(playersListInThisPot, sb.toString());
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
//        activePlayers.clear();
        allWinningUsers.clear();
        pot.clear();
        bet = 0;

        for (Player player : activePlayers) {
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

        while (playersToAct > 0 && !stop) {
            //rotate the actor

            playersToAct--;
            rotateActor();
            log.debug("playersToAct: " + playersToAct + " id: " + actor.getUdid() + " name: " + actor.getName());


            Set<Action> allowedActions = getAllowedActions(actor);

            List<Player> playersToForward = new ArrayList<>();
            for (Player player : allPlayersInGame) {
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
                    this.waitingPlayers.put(actor.getUdid(), actor);
                    actorPosition--;
                    if (this.activePlayers.size() == 1) {
                        log.debug(this.activePlayers.get(0).getName() + " win ...");
                        playersToAct = 0;
                    }
                    PlayerDao.updateLoseCount(actor);
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
                NotificationCenter.paySmallBlind(allPlayersInGame, info);
            } else if (action.getName().equals(Action.BIG_BLIND.getName())) {
                NotificationCenter.payBigBlind(allPlayersInGame, info);
            } else {
                NotificationCenter.forwardAction(playersToForward, info);
                playersToForward.clear();
                playersToForward = null;
            }

            //reset actor's bet
            actor.setBetThisTime(0);


        }

        pot.buildPotList();
    }


    public Set<Action> getAllowedActions(Player player) {
        Set<Action> actions = new HashSet<Action>();
        if (player.getMoneyInGame() != 0) {
            if (bet == 0) {
                actions.add(Action.CHECK);
                actions.add(Action.RAISE);
            } else {
                if (player.getMoneyInGame() >= bet) {
                    actions.add(Action.CALL);
                }
                if (player.getMoneyInGame() >= bet * 2) {
                    actions.add(Action.RAISE);
                }

                if (player.getMoneyInGame() > 0) {
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
            throw new IllegalStateException("No active activePlayers left");
        }
    }


    private void sayHello() {
        gaming = true;
        for (String s : waitingPlayers.keySet()) {
            Player player = waitingPlayers.get(s);
            if (player.isOnline() && player.inRoom(gc.getId())) {
                if (activePlayers.size() < gc.getMaxPlayersCount()) {
                    activePlayers.add(player);
                }
            }
        }

        for (Player player : activePlayers) {
            if (player.getMoneyInGame() <= 0) {
                activePlayers.remove(player);
                standingPlayers.put(player.getUdid(), player);
            }
        }


        waitingPlayers.clear();

        /*String info = "";
        for (Player player : activePlayers) {
            info = info + player.getUdid() + "," + player.getName() + "," + player.getMoneyInGame() + "," + player.getCustomAvatar() + "," + player.getAvatar() + "," + player.getSex() + "," + player.getAddress() + ";";
        }*/


        NotificationCenter.sayHello(allPlayersInGame, DTOUtil.writeValue(DTOUtil.getPlayerDTOList(activePlayers, Config.GAMESTATUS_ACTIVE)));
        log.debug("dto to json: " + DTOUtil.writeValue(DTOUtil.getPlayerDTOList(activePlayers, Config.GAMESTATUS_ACTIVE)));


//        NotificationCenter.sayHello(allPlayersInGame, info);
//        log.debug(info);

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
                if (activePlayers.size() + waitingPlayers.size() >= 2) {
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
                } else {
//                    log.debug("activeplayers:" + activePlayers.size());
//                    log.debug("waitingplayers:" + standingPlayers.size());
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

    public Map<String, Player> getStandingPlayers() {
        return standingPlayers;
    }

    private void checkAvailablePlayers() {
        for (String s : standingPlayers.keySet()) {
            Player player = standingPlayers.get(s);
            if (!player.isOnline()) {
                standingPlayers.remove(s);
            }
        }

        List<Player> brokePlayers = new ArrayList<>();

        for (Player player : activePlayers) {
            if (player.getMoneyInGame() <= 0) {
                activePlayers.remove(player);
                standingPlayers.put(player.getUdid(), player);
                brokePlayers.add(player);
            }
        }

        for (String udid : waitingPlayers.keySet()) {
            Player player = waitingPlayers.get(udid);
            if (player.getMoneyInGame() <= 0) {
                waitingPlayers.remove(udid);
                standingPlayers.put(udid, player);
                brokePlayers.add(player);
            }
        }
        //破产玩家弹窗要求买筹码
        NotificationCenter.youAreBroke(brokePlayers);
        brokePlayers.clear();
    }

    public void removePlayer(Player player) {

        boolean playerRemoved = false;


        for (Player aplayer : activePlayers) {
            if (aplayer.getUdid().equals(player.getUdid())) {
                log.debug(player.getName() + " has left the room " + gc.getName() + " and free the chair " + player.getSeatIndex());
                table.put(player.getSeatIndex(), Config.EMPTY_SEAT);

                player.setRoomid(Integer.MIN_VALUE);
                player.setSeatIndex(0);
                activePlayers.remove(aplayer);

                playerRemoved = true;
                break;
            }
        }


        if (!playerRemoved) {
            for (String s : standingPlayers.keySet()) {
                Player aplayer = standingPlayers.get(s);
                if (aplayer.getUdid().equals(player.getUdid())) {
                    log.debug(player.getName() + " has left the room " + gc.getName() + " and free the chair " + player.getSeatIndex());
                    table.put(player.getSeatIndex(), Config.EMPTY_SEAT);
                    standingPlayers.remove(s);
                    player.setRoomid(Integer.MIN_VALUE);
                    player.setSeatIndex(0);

                    break;
                }
            }
        }

        if (!playerRemoved) {
            for (String s : waitingPlayers.keySet()) {
                Player aplayer = waitingPlayers.get(s);
                if (aplayer.getUdid().equals(player.getUdid())) {
                    log.debug(player.getName() + " has left the room " + gc.getName() + " and free the chair " + player.getSeatIndex());
                    table.put(player.getSeatIndex(), Config.EMPTY_SEAT);
                    waitingPlayers.remove(s);
                    player.setRoomid(Integer.MIN_VALUE);
                    player.setSeatIndex(0);

                    break;
                }
            }
        }


        if (playerRemoved) {
            RoomDao.updateCurrentPlayerCount(gc.getId(), activePlayers.size() + waitingPlayers.size());
        }

        allPlayersInGame.remove(player);

    }


    private int getNextRandomSeat() {
        int randomChairIndex = 0;
        List<Integer> availableChairs = new ArrayList<>();
        for (Integer i : table.keySet()) {
            if (table.get(i).equals(Config.EMPTY_SEAT)) {
                availableChairs.add(i);
            }
        }
        randomChairIndex = availableChairs.get(random.nextInt(availableChairs.size()));
        return randomChairIndex;
    }

    private void leaveTable(Player player) {
        for (int i : table.keySet()) {
            if (table.get(i).equals(player.getUdid())) {
                table.put(i, Config.EMPTY_SEAT);
                break;
            }
        }
    }

}