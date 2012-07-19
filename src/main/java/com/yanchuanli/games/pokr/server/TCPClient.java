package com.yanchuanli.games.pokr.server;

import com.yanchuanli.games.pokr.util.Config;
import com.yanchuanli.games.pokr.util.Memory;
import com.yanchuanli.games.pokr.util.Util;
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

        log.debug("connecing to the server...");
        ConnectFuture connFuture = connector.connect(new InetSocketAddress(Config.serverAddress, Config.port));
        //等待连接成功
        connFuture.awaitUninterruptibly();
        log.debug("connected ...");
        Scanner scanner = new Scanner(System.in);
        String input = "";



        while (connFuture.isConnected() && connFuture.getSession().isConnected()) {
            input = scanner.nextLine();

            log.info("INPUT:" + input);

            if (input.startsWith("c")) {
                sendToServer(input, 5);
            } else if (input.startsWith("f")) {
                sendToServer(input, 5);
            } else if (input.startsWith("ca")) {
                sendToServer(input, 5);
            } else if (input.startsWith("r")) {
                sendToServer(input, 5);
            } else if (input.startsWith("a")) {
                sendToServer(input, 5);
            } else if (input.startsWith("j")) {
                String[] cmds = input.split(":");
                sendToServer(cmds[1], 3);
            } else if (input.startsWith("sb")) {
                sendToServer(input.split(":")[1], 4);
            } else if (input.startsWith("li")) {
                sendToServer(String.valueOf(Config.ROOM_LEVEL_BEGINNER), 2);
            } else if (input.startsWith("l")) {
                sendToServer(input.split(":")[1] + "," + input.split(":")[1] + "123," + Config.SRC_IPHONE_GUEST, 0);
                sendToServer("1001", 3);
                sendToServer("1001", 4);
            } else if (input.startsWith("s")) {
                // eg: s:jiangchao
                sendToServer("1001," + input.split(":")[1] + ",ban ge tong kuai!", 17);
            }


        }
    }

    private static void sendToServer(String msg, int status) {
        for (String s : Memory.sessionsOnClient.keySet()) {
            log.info("session:" + s + " status:" + status);
            Util.sendMsg(Memory.sessionsOnClient.get(s), msg, status);
        }
    }
}
