package com.yanchuanli.games.pokr.server;

import com.yanchuanli.games.pokr.util.Config;
import com.yanchuanli.games.pokr.util.Memory;
import com.yanchuanli.games.pokr.util.Util;
import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.Executors;

/**
 * Author: Yanchuan Li
 * Date: 5/27/12
 * Email: mail@yanchuanli.com
 */
public class UDPServer {

    private static Logger log = Logger.getLogger(UDPServer.class);

    public static void main(String[] args) throws IOException {
        NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
        acceptor.setHandler(new NetworkServerHandler());

        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
//        chain.addLast("keep-alive", new HachiKeepAliveFilterInMina());
        chain.addLast("logger", new LoggingFilter());
        chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
        DatagramSessionConfig dcfg = acceptor.getSessionConfig();
        dcfg.setReuseAddress(true);


        acceptor.bind(new InetSocketAddress(Config.port));
        log.info("UDPServer listening on port " + Config.port);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equalsIgnoreCase("quit")) {
            log.info("INPUT:" + input);

            for (String s : Memory.sessionsOnServer.keySet()) {
                Util.sendMessage(Memory.sessionsOnServer.get(s), input);
            }
            input = scanner.nextLine();
        }
    }
}
