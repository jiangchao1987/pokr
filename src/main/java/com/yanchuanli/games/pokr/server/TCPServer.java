package com.yanchuanli.games.pokr.server;

import com.yanchuanli.games.pokr.core.GameEngine;
import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.util.Memory;
import com.yanchuanli.games.pokr.util.NotificationCenter;
import com.yanchuanli.games.pokr.util.ServerConfig;
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

    public static void main(String[] args) throws IOException {
        NioSocketAcceptor acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
        acceptor.setHandler(new ServerHandler());

        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
        SocketSessionConfig dcfg = acceptor.getSessionConfig();
        dcfg.setReuseAddress(true);
        acceptor.bind(new InetSocketAddress(ServerConfig.gameServerPort));


        log.info("TCPServer is listening on " + getIPAddress() + ":" + ServerConfig.gameServerPort);


        GameEngine.start();

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equalsIgnoreCase("quit")) {

            log.info("INPUT:" + input);
//                Util.sendToAll(input);
            for (String s : Memory.sessionsOnServer.keySet()) {
                Player player = Memory.sessionsOnServer.get(s);
                if (player.getSession().isClosing()) {
                    log.debug("isclosing");
                } else {
                    log.debug("not closing");
                }
                if (player.getSession().isConnected()) {
                    log.debug("connected");
                } else {
                    log.debug("not connected");
                }

                List<Player> players = new ArrayList<>();
                players.add(player);
                NotificationCenter.chat(players, s);

            }


            input = scanner.nextLine();
        }
        log.info("quitting now ...");
        GameEngine.stop();
        acceptor.unbind();
        acceptor.dispose();
//        ServiceCenter.getInstance().stopService();
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
