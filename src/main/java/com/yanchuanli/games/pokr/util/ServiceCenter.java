package com.yanchuanli.games.pokr.util;

import com.yanchuanli.games.pokr.basic.Dealer;
import com.yanchuanli.games.pokr.core.GameEngine;
import com.yanchuanli.games.pokr.dao.PlayerDao;
import com.yanchuanli.games.pokr.dao.RoomDao;
import com.yanchuanli.games.pokr.game.Game;
import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.model.Room;
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
                case Config.TYPE_LEAVEROOM_INGAME:
                    leaveRoom(session, map.get(key));
                    break;
                case Config.TYPE_CHAT_INGAME:
                    chat(session, map.get(key));
                    break;
            }
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
        List<Player> players = new ArrayList<>();
        for (Player p : game.getActivePlayers()) {
            if (!p.getUdid().equals(cmds[1])) {
                players.add(p);
            }
        }
        NotificationCenter.chat(players, cmds[1] + ": " + cmds[2]);
        players.clear();
        players = null;
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
        Player player = new Player(cmds[1], cmds[2]);
        game.removePlayer(player);
        NotificationCenter.leaveRoom(game.getActivePlayers(), cmds[1] + ",0");
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
        game.addPlayer(newplayer);

        StringBuilder sb = new StringBuilder();
        for (Player player : game.getAvailablePlayers()) {
            sb.append(player.getUdid()).append(",").append(player.getName()).append(",").append(player.getMoney()).append(";");
        }

        NotificationCenter.sayHello(game.getAvailablePlayers(), sb.toString());
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
            sb.append(room.getId() + ",");
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
        String[] msgs = info.split(",");
        Player player = PlayerDao.getPlayer(msgs[0], msgs[1], Integer.parseInt(msgs[2]));
        player.setSession(session);
        Memory.sessionsOnServer.put(String.valueOf(session.getId()), player);

        StringBuffer sb = new StringBuffer();
        sb.append(player.getUdid() + "," + player.getName() + ","
                + player.getMoney() + "," + player.getExp() + ","
                + player.getWinCount() + "," + player.getLoseCount() + ","
                + player.getHistoricalBestHandRank() + ","
                + player.getHistoricalBestHand() + "," + player.getMaxWin()
                + "," + player.getCustomAvatar() + "," + player.getAvatar());
        NotificationCenter.login(session, sb.toString());
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
