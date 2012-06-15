package com.jiangchao.games.pokr.game.client;

import java.net.InetSocketAddress;
import java.util.Scanner;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.jiangchao.games.pokr.game.handler.TexasCHandler;
import com.jiangchao.games.pokr.util.Util;
import com.yanchuanli.games.pokr.util.Config;
import com.yanchuanli.games.pokr.util.Memory;

/**
 * Note: Texas DummyClient
 * Author: JiangChao 
 * Date: 2012/6/15/13 
 * Email: chaojiang@candou.com
 */
public class TexasClient {
	NioSocketConnector connector;

	public TexasClient() {
		connector = new NioSocketConnector();
	}

	public boolean connect() {
		System.out.println("[TexasClient] connect now!");
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
			for (String s : Memory.sessionsOnClient.keySet()) {
				Util.sendMessage(Memory.sessionsOnClient.get(s), input);
			}
			input = scanner.nextLine();
		}
	}

}
