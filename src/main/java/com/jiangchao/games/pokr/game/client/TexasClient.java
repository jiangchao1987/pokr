package com.jiangchao.games.pokr.game.client;

import java.net.InetSocketAddress;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.jiangchao.games.pokr.game.handler.TexasCHandler;
import com.jiangchao.games.pokr.util.Config;
import com.jiangchao.games.pokr.util.Util;

/**
 * Note: Texas DummyClient
 * Author: JiangChao 
 * Date: 2012/6/15/13 
 * Email: chaojiang@candou.com
 */
public class TexasClient {
    private static Logger log = Logger.getLogger(TexasClient.class);
    private NioSocketConnector connector;

	public TexasClient() {
		connector = new NioSocketConnector();
	}

	public boolean connect() {
		connector.setHandler(new TexasCHandler());
		ConnectFuture connFuture = connector.connect(new InetSocketAddress(
				Config.serverAddress, Config.port));
		connFuture.awaitUninterruptibly();
		return connFuture.isConnected();
	}

	public static void main(String[] args) {
		TexasClient tc = new TexasClient();
        tc.connect();
        
        // 通过cmd玩Texas
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		while (true) {
			log.info("client input: " + input);
            Util.sendToAll(input);
			input = scanner.nextLine();
		}
	}

}
