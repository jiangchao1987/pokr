package com.yanchuanli.games.pokr.server;

import com.yanchuanli.games.pokr.core.GameEngine;
import com.yanchuanli.games.pokr.game.Game;
import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.util.Memory;
import com.yanchuanli.games.pokr.util.NotificationCenter;
import com.yanchuanli.games.pokr.util.ServerConfig;
import com.yanchuanli.games.pokr.util.Util;
import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class TCPServer {

    private static Logger log = Logger.getLogger(TCPServer.class);
    private static NioSocketAcceptor acceptor;

    public static void main(String[] args) throws IOException {
        GameEngine.start();
        start();

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equalsIgnoreCase("quit")) {
            log.info("cmd:" + input);
            try {
                if (input.startsWith("list")) {
                    String[] cmds = input.split(":");
                    String roomid = cmds[1];
                    Game game = GameEngine.getGame(Integer.parseInt(roomid));
                    game.printUserList();
                } else if (input.startsWith("kick")) {
                    String[] cmds = input.split(":");
                    String udid = cmds[1];
                    Player player = Memory.playersOnServer.get(udid);
                    if (player != null && player.isOnline()) {
                        Util.disconnectUser(player.getSession());
                    }
                } else if (input.startsWith("users")) {
                    for (String udid : Memory.playersOnServer.keySet()) {
                        Player player = Memory.playersOnServer.get(udid);
                        log.debug(player.getName() + "[" + player.getUdid() + "]" + " is sitting at " + player.getSeatIndex() + " in Room " + player.getRoomId() + " with " + player.getMoneyInGame() + " on table!");
                    }
                } else if (input.startsWith("dealer")) {
                    String[] cmds = input.split(":");
                    String roomid = cmds[1];
                    String content = cmds[2];
                    Game game = GameEngine.getGame(Integer.parseInt(roomid));
                    if (game != null) {
                        game.dealerSays(content);
                    }

                } else {
                    log.info("INPUT:" + input);
                    for (String s : Memory.playersOnServer.keySet()) {
                        Player player = Memory.playersOnServer.get(s);


                        List<Player> players = new ArrayList<>();
                        players.add(player);
                        NotificationCenter.chat(players, s);

                    }
                }
            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                log.error(e);
            }


            input = scanner.nextLine();
        }
        log.info("quitting now ...");
        GameEngine.stop();
        shutdown();
        System.exit(1);
//        ServiceCenter.getInstance().stopService();
    }

    public static void start() {
        acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
        acceptor.setHandler(new ServerHandler());

        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
        SocketSessionConfig dcfg = acceptor.getSessionConfig();
        dcfg.setTcpNoDelay(true);
        dcfg.setKeepAlive(true);
        dcfg.setReuseAddress(true);

        try {
            acceptor.bind(new InetSocketAddress(ServerConfig.gameServerPort));
            log.info("TCPServer is listening on " + getIPAddress() + ":" + ServerConfig.gameServerPort);
        } catch (IOException e) {
            log.error(e);
            System.exit(1);
        }

    }

    public static void shutdown() {
        acceptor.unbind();
        acceptor.dispose();
    }

    private static String getIPAddress() {
        String myIp = "UNKNOWN";
        try {
            String hostName = null;
            hostName = InetAddress.getLocalHost().getHostName();
            InetAddress addrs[] = InetAddress.getAllByName(hostName);

            for (InetAddress addr : addrs) {
                if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()) {
                    myIp = addr.getHostAddress();
                }
            }

        } catch (UnknownHostException e) {
            log.error(e);
        }

        return myIp;
    }

}
