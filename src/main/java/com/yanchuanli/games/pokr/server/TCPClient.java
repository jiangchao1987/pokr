package com.yanchuanli.games.pokr.server;

import com.yanchuanli.games.pokr.util.Config;
import com.yanchuanli.games.pokr.util.Memory;
import com.yanchuanli.games.pokr.util.ServerConfig;
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
        ConnectFuture connFuture = connector.connect(new InetSocketAddress(ServerConfig.gameServerAddress, ServerConfig.gameServerPort));
        //等待连接成功
        connFuture.awaitUninterruptibly();
        log.debug("connected ...");
        log.info("请登入游戏服务器");
        Scanner scanner = new Scanner(System.in);
        String input = "";
        String username = "";
        String roomname = "1001";
        String buyin = "10000";

        while (connFuture.isConnected() && connFuture.getSession().isConnected()) {
            input = scanner.nextLine();

            log.debug("INPUT:" + input);

            if (input.startsWith("c")) {
                sendToServer(input, Config.TYPE_ACTION_INGAME);
            } else if (input.startsWith("f")) {
                sendToServer(input, Config.TYPE_ACTION_INGAME);
            } else if (input.startsWith("ca")) {
                sendToServer(input, Config.TYPE_ACTION_INGAME);
            } else if (input.startsWith("r")) {
                sendToServer(input, Config.TYPE_ACTION_INGAME);
            } else if (input.startsWith("a")) {
                sendToServer(input, Config.TYPE_ACTION_INGAME);
            } else if (input.startsWith("j")) {
                String[] cmds = input.split(":");
                roomname = cmds[1];
                sendToServer(roomname, Config.TYPE_JOIN_INGAME);
            } else if (input.startsWith("sb")) {
                sendToServer(roomname + ",0", Config.TYPE_USERSTANDBY_INGAME);
            } else if (input.startsWith("li")) {
                sendToServer(String.valueOf(Config.NORMAL_ROOM_LEVEL_BEGINNER), Config.TYPE_LIST_INGAME);
            } else if (input.startsWith("l")) {
                //login
                String[] cmds = input.split(":");
                username = cmds[1];
                sendToServer(username + "," + username + "123," + Config.SRC_IPHONE_GUEST, Config.TYPE_LOGIN_INGAME);

            } else if (input.startsWith("s")) {
                // eg: s:jiangchao
                sendToServer("1001," + input.split(":")[1] + ",ban ge tong kuai!", Config.TYPE_CHAT_INGAME);
            } else if (input.startsWith("buyin")) {
                String[] cmds = input.split(":");
                buyin = cmds[1];
                sendToServer(username + "," + buyin + "," + roomname, Config.TYPE_BUYIN_INGAME);
            }


        }
    }

    private static void sendToServer(String msg, int status) {
        for (String s : Memory.sessionsOnClient.keySet()) {
            log.debug("session:" + s + " status:" + status);
            Util.sendMsg(Memory.sessionsOnClient.get(s), msg, status);
        }
    }
}
