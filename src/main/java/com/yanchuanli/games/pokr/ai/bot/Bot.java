package com.yanchuanli.games.pokr.ai.bot;

import com.yanchuanli.games.pokr.server.ServerConfig;
import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

/**
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 9/28/12
 */
public class Bot implements Runnable {

    private static Logger log = Logger.getLogger(Bot.class);
    private int id;
    private String username;
    private String password;
    private int roomId;
    private boolean stop;
    private NioSocketConnector connector;
    private BotHandler bh;


    public Bot(String username, String password, int roomId) {
        this.username = username;
        this.password = password;
        this.roomId = roomId;
        stop = false;
    }

    public Bot(String username, String password) {
        this(username, password, Integer.MIN_VALUE);
    }

    @Override
    public void run() {
        init();
    }

    private void init() {
        connector = new NioSocketConnector();
        bh = new BotHandler(username, password, roomId);
        connector.setHandler(bh);

        DefaultIoFilterChainBuilder chain = connector.getFilterChain();

        log.debug("connecing to the server...");
        ConnectFuture connFuture = connector.connect(new InetSocketAddress(ServerConfig.gameServerAddress, ServerConfig.gameServerPort));
        connFuture.awaitUninterruptibly();
    }

    public static void main(String[] args) {
        Bot bot = new Bot("c", "c123", 21688693);
        Thread botThread = new Thread(bot);
        botThread.start();
    }
}
