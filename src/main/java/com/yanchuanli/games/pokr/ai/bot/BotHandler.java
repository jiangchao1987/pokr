package com.yanchuanli.games.pokr.ai.bot;

import com.yanchuanli.games.pokr.basic.Card;
import com.yanchuanli.games.pokr.dao.RoomDao;
import com.yanchuanli.games.pokr.dto.PlayerDTO;
import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.model.Room;
import com.yanchuanli.games.pokr.util.Config;
import com.yanchuanli.games.pokr.util.Util;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.*;

/**
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 10/24/12
 */
public class BotHandler extends IoHandlerAdapter {

    private static Logger log = Logger.getLogger(BotHandler.class);
    private IoSession session;
    private String username;
    private String password;
    private Player player;
    private Random ran;
    private Room room;
    private double probabilityToFold = 0.15;
    private double probabilityToAllIn = 0.3;
    private double probabilityToRaise = 0.6;
    private double probabilityToCall = 0.5;
    private double probabilityToCheck = 0.8;
    private double probabilityToFoldAfterPreflop = 0.25;


    public BotHandler(String username, String password) {
        this(username, password, Integer.MIN_VALUE);
    }

    public BotHandler(String username, String password, int roomID) {
        this.username = username;
        this.password = password;
        player = new Player();
        player.setUdid(username);
        ran = new Random();
        player.setRoomId(roomID);
    }

    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        this.session = session;

        log.debug("session created ...");
    }


    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        if (message instanceof IoBuffer) {
            IoBuffer buffer = (IoBuffer) message;
            List<Map<Integer, String>> list = Util.ioBufferToString(session.getId(), buffer);
            for (Map<Integer, String> map : list) {
                for (Integer key : map.keySet()) {

                    String info = map.get(key);
                    log.debug("[" + key + "]" + info);
                    switch (key) {
                        case Config.TYPE_LOGIN_INGAME:
                            initPlayer(info);
                            listRooms();
                            break;
                        case Config.TYPE_LIST_INGAME:
                            if (player.getRoomId() == Integer.MIN_VALUE) {
                                String[] roomIDs = info.split(",");
                                int selectedRoom = Integer.parseInt(roomIDs[ran.nextInt(roomIDs.length)]);
                                player.setRoomId(selectedRoom);
                            }
                            log.debug("entering room " + player.getRoomId());
                            sendMsg(String.valueOf(player.getRoomId()), Config.TYPE_JOIN_INGAME);
                            room = RoomDao.getRoom(player.getRoomId());
                            while (room.getCurrentPlayerCount() >= room.getMaxPlayersCount()) {
                                Thread.sleep(1000);
                            }
                            int maxBuyinNum = room.getMaxHolding() >= player.getMoney() ? player.getMoney() : room.getMaxHolding();
                            int buyinMoney = ran.nextInt(maxBuyinNum);
                            while (buyinMoney < room.getMinHolding()) {
                                buyinMoney = ran.nextInt(maxBuyinNum);
                            }

                            log.debug("I would buyin " + String.valueOf(buyinMoney) + " chips ...");
                            buyin(buyinMoney);
                            break;
                        case Config.TYPE_BUYIN_INGAME:
                            if (info.equals(String.valueOf(Config.RESULT_BUYINSUCCESS))) {
                                standBy();
                            }
                            break;
                        case Config.TYPE_JOIN_INGAME:
                            ObjectMapper mapper = new ObjectMapper();
                            PlayerDTO[] players = mapper.readValue(info, PlayerDTO[].class);
                            for (PlayerDTO aplayer : players) {
                                if (aplayer.getUdid().equals(player.getUdid())) {
                                    player.setMoneyInGame(aplayer.getMoneyInGame());
                                    player.setName(aplayer.getName());
                                    player.setCustomAvatar(aplayer.getCustomAvatar());
                                    player.setAvatar(aplayer.getAvatar());
                                    player.setSex(aplayer.getSex());
                                    player.setAddress(aplayer.getAddress());
                                    player.setSeatIndex(aplayer.getSeatIndex());
                                    player.setMoney(aplayer.getMoney());
                                    player.setLevel(aplayer.getLevel());
                                    log.debug("Player data updated ...");
                                    break;
                                }
                            }
                            break;
                        case Config.TYPE_SHOWBROKENPLAYERS_INGAME:
                            String[] udids = info.split(";");
                            for (String udid : udids) {
                                if (udid.equals(username)) {
                                    log.debug("I am broken and I will buy chips now ...");
                                    Thread.sleep(5000);
                                    room = RoomDao.getRoom(player.getRoomId());
                                    while (room.getCurrentPlayerCount() >= room.getMaxPlayersCount()) {
                                        Thread.sleep(1000);
                                    }
                                    maxBuyinNum = room.getMaxHolding() >= player.getMoney() ? player.getMoney() : room.getMaxHolding();
                                    buyinMoney = ran.nextInt(maxBuyinNum);
                                    while (buyinMoney < room.getMinHolding()) {
                                        buyinMoney = ran.nextInt(maxBuyinNum);
                                    }

                                    log.debug("I would buyin " + String.valueOf(buyinMoney) + " chips ...");
                                    buyin(buyinMoney);
                                    break;
                                }
                            }
                            break;
                        case Config.TYPE_HOLE_INGAME:
                            String[] cardsInfo = info.split(",");
                            String[] cards = cardsInfo[2].split("_");
                            if (cards.length == 2) {
                                Card cardA = new Card(Integer.parseInt(cards[0]));
                                Card cardB = new Card(Integer.parseInt(cards[1]));
                                log.debug("I've got " + cardA.toChineseString() + " and " + cardB.toChineseString());
                            }
                            break;
                        case Config.TYPE_OTHERSACTION_INGAME:
                            break;
                        case Config.TYPE_ACTION_INGAME:
                            int timeToThink = (int) (room.getBettingDuration() * 0.2);
                            timeToThink = ran.nextInt(timeToThink) + 1;
                            timeToThink = 2000;
                            log.debug("I have to think for " + String.valueOf(timeToThink) + " milli seconds ...");
                            Thread.sleep(timeToThink);
                            log.debug("action!");
                            String[] infos = info.split(",");
                            String[] actions = infos[2].split("_");
                            List<String> allowedactions = new ArrayList<>();
                            for (String ac : actions) {
                                allowedactions.add(ac);
                            }

                            String action = "f";
                            if (allowedactions.contains("c")) {
                                action = "c";
                            } else if (allowedactions.contains("ca")) {
                                action = "ca";
                            }


                            if (allowedactions.contains("r") && doable(probabilityToRaise)) {
                                action = "r";
                            } else if (allowedactions.contains("ca") && doable(probabilityToCall)) {
                                action = "ca";
                            } else if (allowedactions.contains("c") && doable(probabilityToCheck)) {
                                action = "c";
                            } else if (allowedactions.contains("a") && doable(probabilityToAllIn)) {
                                action = "a";
                            }


                            String input = "";

                            switch (action) {
                                case "f":
                                    input = "f";
                                    log.debug("I decide to fold ...");
                                    break;
                                case "c":
                                    input = "c";
                                    log.debug("I decide to check ...");
                                    break;
                                case "r":
                                    int raiseMin = Integer.parseInt(infos[4]);
                                    int a = (int) (Math.floor((player.getMoneyInGame() - raiseMin) / 100) * 0.5);
                                    int b = ran.nextInt(a);
                                    if (b == 0) {
                                        b = b + 1;
                                    }
                                    b = b * 100;
                                    b = b + raiseMin;
                                    input = "r:" + String.valueOf(b);
                                    log.debug("I decide to raise " + String.valueOf(b) + "...");
                                    player.setMoneyInGame(player.getMoneyInGame() - b);
                                    break;
                                case "ca":
                                    input = "ca";
                                    String[] cainfos = info.split(",");
                                    player.setMoneyInGame(player.getMoneyInGame() - Integer.parseInt(cainfos[4]));
                                    log.debug("I decide to call ...");
                                    break;
                                case "a":
                                    input = "a";
                                    player.setMoneyInGame(0);
                                    log.debug("I decide to all in ...");
                                    break;
                            }


                            sendMsg(input, Config.TYPE_ACTION_INGAME);
                            break;
                        case Config.TYPE_YOUAREBROKE_INGAME:
                            sendMsg(String.valueOf(room.getId()) + "," + player.getUdid() + "," + player.getName(), Config.TYPE_LEAVEROOM_INGAME);
                            Thread.sleep(5000);
                            listRooms();
                            break;
                        case Config.TYPE_SMALLBLIND_INGAME:
                            String[] smallBlindInfos = info.split(",");
                            String[] smallBlindAction = smallBlindInfos[1].split(":");
                            log.debug(smallBlindInfos[0] + " has given " + smallBlindAction[1] + " as SmallBlind");
                            if (smallBlindInfos[0].equals(player.getUdid())) {
                                player.setMoneyInGame(player.getMoneyInGame() - Integer.parseInt(smallBlindAction[1]));
                            }
                            break;
                        case Config.TYPE_BIGBLIND_INGAME:
                            String[] bigBlindInfos = info.split(",");
                            String[] bigBlindAction = bigBlindInfos[1].split(":");
                            log.debug(bigBlindInfos[0] + " has given " + bigBlindAction[1] + " as BigBlind");
                            if (bigBlindInfos[0].equals(player.getUdid())) {
                                player.setMoneyInGame(player.getMoneyInGame() - Integer.parseInt(bigBlindInfos[1]));
                            }
                            break;

                        case Config.TYPE_HEARTBEAT_MANAGE:
                            log.debug("respond to heartbeat ...");
                            sendMsg("", Config.TYPE_HEARTBEAT_MANAGE);
                            break;
                    }
                }
            }
        } else {
            log.info("[messageReceived]illegal");
        }
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        log.debug("session opened ...");
        login();
    }


    private void login() {
        log.debug("tries login ...");
        sendMsg(username + "," + password + "," + Config.SRC_BOT, Config.TYPE_LOGIN_INGAME);
    }

    private void buyin(int buyin) {
        sendMsg(username + "," + String.valueOf(buyin) + "," + String.valueOf(player.getRoomId()), Config.TYPE_BUYIN_INGAME);
    }

    private void standBy() {
        sendMsg(String.valueOf(player.getRoomId()) + ",0", Config.TYPE_USERSTANDBY_INGAME);
    }

    private void listRooms() {
        sendMsg(String.valueOf(Config.NORMAL_ROOM_LEVEL_BEGINNER), Config.TYPE_LIST_INGAME);
    }

    private void chat(String msg) {
        sendMsg(String.valueOf(player.getRoomId()) + "," + String.valueOf(player.getUdid()) + "," + msg, Config.TYPE_CHAT_INGAME);
    }

    private void sendMsg(String msg, int type) {
        Util.sendMsg(session, msg, type);
    }

    private void initPlayer(String info) {
        String[] infos = info.split(",");
        player.setName(infos[1]);
        player.setMoney(Integer.parseInt(infos[2]));
        player.setExp(Integer.parseInt(infos[3]));
        player.setWinCount(Integer.parseInt(infos[4]));
        player.setLoseCount(Integer.parseInt(infos[5]));
        player.setHistoricalBestHandRank(Integer.parseInt(infos[6]));
        player.setHistoricalBestHand(infos[7]);
        player.setMaxWin(Integer.parseInt(infos[8]));
        player.setCustomAvatar(Integer.parseInt(infos[9]));
        player.setAvatar(infos[10]);
        player.setSex(Integer.parseInt(infos[11]));
        player.setAddress(infos[12]);
        player.setLevel(Integer.parseInt(infos[13]));
        player.setSession(session);
        player.setOnline(true);
        log.debug(player.toString());
    }

    private boolean doable(double probability) {
        return ran.nextDouble() <= probability;
    }
}
