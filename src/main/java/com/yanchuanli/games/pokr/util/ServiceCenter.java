package com.yanchuanli.games.pokr.util;

import com.yanchuanli.games.pokr.basic.Dealer;
import com.yanchuanli.games.pokr.dao.PlayerDao;
import com.yanchuanli.games.pokr.model.Player;
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
                case Config.TYPE_LEAVE_INGAME:
                    leave(session, map.get(key));
                    break;
            }
        }
    }

    private void leave(IoSession session, String info) {
    }

    /**
     * 加入指定id的房间
     * 
     * @param session 当前用户session
     * @param info 当前房间id
     */
    private void join(IoSession session, String info) {
        List<Player> players = new ArrayList<>();
        for (String s : Memory.sessionsOnServer.keySet()) {
            players.add(Memory.sessionsOnServer.get(s));
        }

        StringBuffer sb = new StringBuffer();
        for (Player player : players) {
            sb.append(player.getId() + "," + player.getName() + "," + player.getMoney() + ";");
        }

        NotificationCenter.sayHello(players, sb.toString());
    }

    /**
     * 列出指定等级的房间列表
     * 
     * @param session 当前用户session
     * @param info 初级房1/中级房2/高级房3/VIP房4
     */
    private void listRooms(IoSession session, String info) {
    	int type = Integer.parseInt(info);
        StringBuffer sb = new StringBuffer();
        switch (type) {
            case Config.ROOM_LEVEL_BEGINNER:
                sb.append("1,2,3");
                break;
            case Config.ROOM_LEVEL_PROFESSIONAL:
                sb.append("4,5,6,7,8,9");
                break;
            case Config.ROOM_LEVEL_MASTER:
                sb.append("10,11,12");
                break;
            case Config.ROOM_LEVEL_VIP:
            	sb.append("13,14,15,16");
            	break;
        }
        NotificationCenter.list(session, sb.toString());
    }

    /**
     * 游戏主要操作命令,c,ca,f,r:200
     * 
     * @param session 当前用户session
     * @param info 游戏操作命令
     */
    private void action(IoSession session, String info) {
        Player player = Memory.sessionsOnServer.get(String.valueOf(session.getId()));
        player.setInput(info);
    }

    /**
     * 登录游戏
     * 
     * @param session 当前用户session
     * @param info udid,source[0|1|...]
     */
    private void login(IoSession session, String info) {
        Player player = PlayerDao.getPlayer(info.split(",")[0], Integer.parseInt(info.split(",")[1]));

        player.setSession(session);
        Memory.sessionsOnServer.put(String.valueOf(session.getId()), player);

        StringBuffer sb = new StringBuffer();
        sb.append(player.getId() + "," + player.getName() + "," + player.getMoney());
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
