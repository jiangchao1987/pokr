package com.yanchuanli.games.pokr.server;

import com.yanchuanli.games.pokr.util.ServiceCenter;
import com.yanchuanli.games.pokr.util.Config;
import com.yanchuanli.games.pokr.util.Memory;
import com.yanchuanli.games.pokr.util.Util;
import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
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
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
        acceptor.setHandler(new NetworkServerHandler());

        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
        SocketSessionConfig dcfg = acceptor.getSessionConfig();
        dcfg.setReuseAddress(true);


        acceptor.bind(new InetSocketAddress(Config.port));
        log.info("TCPServer listening on port " + Config.port);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equalsIgnoreCase("quit")) {
            log.info("INPUT:" + input);
            for (String s : Memory.sessionsOnServer.keySet()) {
                log.info("session:" + s);
                Util.sendMessage(Memory.sessionsOnServer.get(s), input);
            }
            input = scanner.nextLine();
        }
        log.info("quitting now ...");
        acceptor.unbind();
        acceptor.dispose();
        ServiceCenter.getInstance().stopService();
    }

}
