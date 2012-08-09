package com.yanchuanli.games.pokr.util;

import com.yanchuanli.games.pokr.basic.Dealer;
import com.yanchuanli.games.pokr.core.GameEngine;
import com.yanchuanli.games.pokr.dao.EventDao;
import com.yanchuanli.games.pokr.dao.PlayerDao;
import com.yanchuanli.games.pokr.dao.RoomDao;
import com.yanchuanli.games.pokr.game.Game;
import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.model.Room;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class ServiceCenter {

    private static Logger log = Logger.getLogger(ServiceCenter.class);

    private static ServiceCenter instance;

    private List<Dealer> dealers;

    private ExecutorService dealerPool;


    static {
        instance = new ServiceCenter();
    }

    public ServiceCenter() {
        dealerPool = Executors.newFixedThreadPool(5);
        dealers = new ArrayList<Dealer>();
        dealers.add(new Dealer());
    }

    public static ServiceCenter getInstance() {
        return instance;
    }


    public void processCommand(IoSession session, Map<Integer, String> map) {
        Set<Integer> sets = map.keySet();
        for (Integer key : sets) {
            log.debug("status code: [" + key + "]");
            switch (key) {
                case Config.TYPE_LOGIN_INGAME:
//				Util.sendMsg(session, "1", 0);
                    login(session, map.get(key));
                    break;
                case Config.TYPE_ACTION_INGAME:
                    action(session, map.get(key));
                    break;
                case Config.TYPE_LIST_INGAME:
                    listRooms(session, map.get(key));
                    break;
                case Config.TYPE_JOIN_INGAME:
                    join(session, map.get(key));
                    break;
                case Config.TYPE_USERSTANDBY_INGAME:
                    userStandBy(session, map.get(key));
                    break;
                case Config.TYPE_LEAVEROOM_INGAME:
                    leaveRoom(session, map.get(key));
                    break;
                case Config.TYPE_CHAT_INGAME:
                    chat(session, map.get(key));
                    break;
                case Config.TYPE_BUYIN_INGAME:
                    buyIn(session, map.get(key));
                    break;
                case Config.TYPE_STANDUP_INGAME:
                    standUp(session, map.get(key));
                    break;
                case Config.TYPE_ADDFRIENDREQUEST:
                    addFriendRequest(session, map.get(key));
                    break;
            }
        }
    }

    /**
     * 房间中加好友
     *
     * @param session
     * @param info    房间id,目标用户udid
     */
    public void addFriendRequest(IoSession session, String info) {
        String[] cmds = info.split(",");
        String roomid = cmds[0];
        String targetUdid = cmds[1];
        Game game = GameEngine.getGame(Integer.parseInt(roomid));
        Player targetPlayer = Memory.playersOnServer.get(targetUdid);
        Player fromPlayer = Memory.sessionsOnServer.get(String.valueOf(session.getId()));
        game.forwardAddFriendRequest(fromPlayer, targetPlayer);
    }

    /**
     * 房间中已坐下用户站起
     *
     * @param session
     * @param info    房间id,用户udid
     */
    public void standUp(IoSession session, String info) {
        String[] cmds = info.split(",");
        Game game = GameEngine.getGame(Integer.parseInt(cmds[0]));
        Player newplayer = Memory.sessionsOnServer.get(String.valueOf(session.getId()));
        game.standUp(newplayer);
    }

    /**
     * 游戏中购买筹码
     *
     * @param session
     * @param info    用户id,money,房间id
     */
    public void buyIn(IoSession session, String info) {
        log.debug("buyin:" + info);
        String[] cmds = info.split(",");
        Game game = GameEngine.getGame(Integer.parseInt(cmds[2]));
        Player newplayer = Memory.sessionsOnServer.get(String.valueOf(session.getId()));
        log.debug(newplayer.getName() + " tries to buyin " + Integer.parseInt(cmds[1]) + " in game " + Integer.parseInt(cmds[2]));
        boolean result = game.buyIn(newplayer, Integer.parseInt(cmds[1]));
        if (result) {
            log.debug("buyin success");
            NotificationCenter.buyIn(session, String.valueOf(Config.RESULT_BUYINSUCCESS));
        } else {
            log.debug("buyin failed");
        }
    }

    /**
     * 房间中聊天
     *
     * @param session
     * @param info    房间id,用户udid,chat内容
     */
    private void chat(IoSession session, String info) {
        String[] cmds = info.split(",");
        Game game = GameEngine.getGame(Integer.parseInt(cmds[0]));
        if (game != null) {
            Player player = Memory.sessionsOnServer.get(String.valueOf(session.getId()));
            game.chat(player, cmds[2]);
        }

    }

    /**
     * 离开房间
     *
     * @param session
     * @param info    房间id,用户udid,用户名
     */
    public void leaveRoom(IoSession session, String info) {
        String[] cmds = info.split(",");
        Game game = GameEngine.getGame(Integer.parseInt(cmds[0]));
        if (game != null) {
            Player player = Memory.sessionsOnServer.get(String.valueOf(session.getId()));
            log.debug("Player " + player.getName() + " is leaving " + cmds[0]);
            game.removePlayer(player);
        }
    }

    /**
     * 加入指定id的房间
     *
     * @param session 当前用户session
     * @param info    当前房间id
     */
    private void join(IoSession session, String info) {
        Game game = GameEngine.getGame(Integer.parseInt(info));
        Player newplayer = Memory.sessionsOnServer.get(String.valueOf(session.getId()));
        game.enterRoom(newplayer);
    }

    /**
     * 某个用户的头像下载完毕
     *
     * @param session 当前用户session
     * @param info    当前房间id,座位id （座位id=0时，由系统分配座位）
     */
    private void userStandBy(IoSession session, String info) {
        String[] cmds = info.split(",");
        Game game = GameEngine.getGame(Integer.parseInt(cmds[0]));
        Player newplayer = Memory.sessionsOnServer.get(String.valueOf(session.getId()));
        log.debug("user" + newplayer.getName() + "stand by");
        game.sitDown(newplayer, Integer.parseInt(cmds[1]));
    }

    /**
     * 列出指定等级的房间列表
     *
     * @param session 当前用户session
     * @param info    初级房1/中级房2/高级房3/VIP房4
     */
    private void listRooms(IoSession session, String info) {
        int type = Integer.parseInt(info);
        List<Room> rooms = RoomDao.getRooms(type);
        StringBuffer sb = new StringBuffer();
        for (Room room : rooms) {
            sb.append(room.getId()).append(",");
        }
        String result = sb.toString();
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        NotificationCenter.list(session, result);
    }

    /**
     * 游戏主要操作命令,c,ca,f,r:200
     *
     * @param session 当前用户session
     * @param info    游戏操作命令
     */
    private void action(IoSession session, String info) {
        Player player = Memory.sessionsOnServer.get(String.valueOf(session.getId()));
        player.setInput(info);
    }

    /**
     * 登录游戏
     *
     * @param session 当前用户session
     * @param info    udid,password,source[0|1|...]
     */
    private void login(IoSession session, String info) {
        try {
            String[] msgs = info.split(",");
            String udid = String.valueOf(msgs[0]);

            Player player;
            if (Memory.playersOnServer.containsKey(udid)) {
                player = Memory.playersOnServer.get(udid);
                Util.disconnectUser(player.getSession());
            }

            player = PlayerDao.getPlayer(udid, msgs[1], Integer.parseInt(msgs[2]));
            if (player != null) {
                Memory.playersOnServer.put(udid, player);
            }

            if (player != null) {
                log.debug(player.getName() + " has logged in ...");
                player.setSession(session);
                player.setOnline(true);
                Memory.sessionsOnServer.put(String.valueOf(session.getId()), player);

                PlayerDao.updateOnlineStatus(player);


                EventDao.insertLoginEvent(player);

                StringBuffer sb = new StringBuffer();
                sb.append(player.getUdid()).append(",").append(player.getName()).append(",").append(
                        player.getMoney()).append(",").append(player.getExp() + ","
                        + player.getWinCount() + "," + player.getLoseCount() + ","
                        + player.getHistoricalBestHandRank() + ","
                        + player.getHistoricalBestHand() + "," + player.getMaxWin()
                        + "," + player.getCustomAvatar() + "," + player.getAvatar()
                        + "," + player.getSex() + "," + player.getAddress() + "," + player.getLevel());
                NotificationCenter.login(session, sb.toString());
            } else {
                // 用户名密码验证失败则断掉连接
                session.close(true);
            }

        } catch (Exception e) {
            //对于非法连接，立刻断掉。
            session.close(true);
            log.error(ExceptionUtils.getStackTrace(e));
        }


    }

    private void createRoom() {
        Dealer dealer = dealers.get(0);
        if (!dealer.isStarted()) {
            dealerPool.submit(dealer);
        }
    }

    private void stopDealer() {
        Dealer dealer = dealers.get(0);
        dealer.setStarted(false);

    }

    public void stopService() {
        dealerPool.shutdown();
    }
}
