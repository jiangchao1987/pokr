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
                    list(session, map.get(key));
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

    private void list(IoSession session, String info) {
        StringBuffer sb = new StringBuffer();
        switch (info) {
            case "1":
                sb.append("1,2,3");
                break;
            case "2":
                sb.append("4,5,6,7,8,9");
                break;
            case "3":
                sb.append("10,11,12");
                break;
        }
        NotificationCenter.list(session, sb.toString());
    }

    private void action(IoSession session, String info) {
        Player player = Memory.sessionsOnServer.get(String.valueOf(session.getId()));
        player.setInput(info);
    }

    private void login(IoSession session, String info) {
        Player player = PlayerDao.getPlayer(info.split(",")[0], Integer.parseInt(info.split(",")[1]));
        player.setMoney(10000);
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
