package com.yanchuanli.games.pokr.util;

import com.yanchuanli.games.pokr.core.Dealer;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
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

    public void processCommand(String cmd) {
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
        }
    }

    private void createRoom() {
        Dealer dealer = dealers.get(0);
        if (!dealer.isStarted()) {
            dealerPool.submit(dealer);
        }
    }

    private void stopDealer(){
        Dealer dealer = dealers.get(0);
        dealer.setStarted(false);

    }

    public void stopService(){
        dealerPool.shutdown();
    }
}
