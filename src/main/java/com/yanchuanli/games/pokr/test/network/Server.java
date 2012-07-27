package com.yanchuanli.games.pokr.test.network;

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
import java.util.Scanner;
import java.util.concurrent.Executors;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-7-2
 */
public class Server {

    private static Logger log = Logger.getLogger(Server.class);

    public static void main(String[] args) throws IOException {
        NioSocketAcceptor acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
        acceptor.setHandler(new MyServerHandler());

        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
        SocketSessionConfig dcfg = acceptor.getSessionConfig();
        dcfg.setReuseAddress(true);
        acceptor.bind(new InetSocketAddress(ServerConfig.gameServerPort));


        log.info("TCPServer is listening on " + getIPAddress() + ":" + ServerConfig.gameServerPort);

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equalsIgnoreCase("quit")) {

            log.info("INPUT:" + input);
            //                Util.sendToAll(input);


            input = scanner.nextLine();
        }
        log.info("quitting now ...");

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
