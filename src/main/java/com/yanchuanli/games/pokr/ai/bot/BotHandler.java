package com.yanchuanli.games.pokr.ai.bot;

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

import java.util.List;
import java.util.Map;
import java.util.Random;

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
                    log.debug("[messageReceived] status code: [" + key + "] " + info);
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
                            sendMsg(String.valueOf(player.getRoomId()), Config.TYPE_JOIN_INGAME);
                            room = RoomDao.getRoom(player.getRoomId());
                            while (room.getCurrentPlayerCount() >= room.getMaxPlayersCount()) {
                                Thread.sleep(1000);
                            }
                            int maxBuyinNum = room.getMaxHolding() >= player.getMoney() ? room.getMaxHolding() : player.getMoney();
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
                            log.debug(info);
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
                                    log.debug(player);
                                    break;
                                }
                            }
                            break;
                        case Config.TYPE_HOLE_INGAME:

                            break;
                        case Config.TYPE_OTHERSACTION_INGAME:
                            break;
                        case Config.TYPE_ACTION_INGAME:
                            int timeToThink = (int) (room.getBettingDuration() * 0.8);
                            timeToThink = ran.nextInt(timeToThink) + 1;
                            log.debug("I have to think for " + String.valueOf(timeToThink) + " milli seconds ...");
                            Thread.sleep(timeToThink);

                            String[] infos = info.split(",");
                            String[] actions = infos[2].split("_");
                            String action = actions[ran.nextInt(actions.length)];
                            String input = "";
                            if (action.equals("f")) {
                                input = "f";
                                log.debug("I decide to fold ...");
                            } else if (action.equals("c")) {
                                input = "c";
                                log.debug("I decide to check ...");
                            } else if (action.equals("r")) {
                                input = "r:100";
                            } else if (action.equals("ca")) {
                                input = "ca";
                                log.debug("I decide to call ...");
                            }

                            sendMsg(input, Config.TYPE_ACTION_INGAME);
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
        sendMsg(username + "," + password + "," + Config.SRC_IPHONE_GUEST, Config.TYPE_LOGIN_INGAME);
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
}
