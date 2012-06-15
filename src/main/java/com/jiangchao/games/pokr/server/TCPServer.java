package com.jiangchao.games.pokr.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.yanchuanli.games.pokr.util.Config;
import com.yanchuanli.games.pokr.util.Memory;
import com.jiangchao.games.pokr.util.Util;

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
            System.out.println(Memory.sessionsOnServer.keySet().size());
//                log.info("session:" + s);
//                for (int index = 0; index < 60000; index ++) {
//                	log.info("running!");
                	if (!Memory.sessionsOnServer.keySet().isEmpty()) {
                		for (String s : Memory.sessionsOnServer.keySet()) {
                        	Util.sendMessage(Memory.sessionsOnServer.get(s), input);
                        }
                	}
//                	try {
//						Thread.sleep(5 * 1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//                }
            input = scanner.nextLine();
        }
//        log.info("quitting now ...");
//        acceptor.unbind();
//        acceptor.dispose();
//        ServiceCenter.getInstance().stopService();
    }

}
