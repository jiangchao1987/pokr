package com.jiangchao.games.pokr.server;

import com.yanchuanli.games.pokr.util.Config;
import com.yanchuanli.games.pokr.util.Memory;
import com.jiangchao.games.pokr.util.Util;
import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class TCPClient {

    private static Logger log = Logger.getLogger(TCPClient.class);

    public static void main(String[] args) {
        NioSocketConnector connector = new NioSocketConnector();
        connector.setHandler(new ClientHandler());

        DefaultIoFilterChainBuilder chain = connector.getFilterChain();
//        chain.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));

        log.debug("connecing to the server...");
        ConnectFuture connFuture = connector.connect(new InetSocketAddress(Config.serverAddress, Config.port));
        //等待连接成功
        connFuture.awaitUninterruptibly();
        if (connFuture.isConnected()) {
            log.debug("connected ...");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            while (!input.equalsIgnoreCase("quit")) {
                log.info("INPUT:" + input);
                for (String s : Memory.sessionsOnClient.keySet()) {
                    log.info("session:" + s);
                    Util.sendMessage(Memory.sessionsOnClient.get(s), input);
                }
                input = scanner.nextLine();
            }
        } else {
            log.debug("not connected");
        }
    }
}
