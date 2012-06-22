package com.jiangchao.games.pokr.util;

import com.yanchuanli.games.pokr.basic.Dealer;
import com.yanchuanli.games.pokr.model.Player;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Note: Server Center
 * Author: JiangChao 
 * Date: 2012/6/18/13 
 * Email: chaojiang@candou.com
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

    public void processCommand(IoSession session, String cmd) {
        log.debug("cmd:" + cmd);
        switch (cmd) {
            case "listrooms":
                log.debug("listing rooms ...");
                break;
            case "createroom":
                log.debug("create a room ...");
                createRoom();
                break;
            case "stopdealer":
                log.debug("stop a room ...");
                stopDealer();
            case "start":
                Dealer dealer = dealers.get(0);
                dealer.start();
                break;
            case "join":
                Player p = new Player(String.valueOf(session.getId()), "player" + session.getId());
                p.setSession(session);
                dealers.get(0).addPlayer(p);
                break;
            default:
                Player player = Memory.playersOnServer.get(String.valueOf(session.getId()));
                player.setInput(cmd);
                NotificationCenter.notify(Memory.playersOnServer, cmd, session);
                break;
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
