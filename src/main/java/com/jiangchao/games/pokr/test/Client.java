package com.jiangchao.games.pokr.test;

import java.net.InetSocketAddress;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.jiangchao.games.pokr.util.Config;

public class Client {
    private static Logger log = Logger.getLogger(Client.class);
    private NioSocketConnector connector;

	public Client() {
		connector = new NioSocketConnector();
	}

	public boolean connect() {
		connector.setHandler(new CHandler());
		ConnectFuture connFuture = connector.connect(new InetSocketAddress(
				Config.serverAddress, Config.port));
		connFuture.awaitUninterruptibly();
		return connFuture.isConnected();
	}

	public static void main(String[] args) {
		Client tc = new Client();
        tc.connect();
        
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		while (true) {
			log.info("client input: " + input);
            Helper.sendToAllUser(input.split(":")[0], Integer.parseInt(input.split(":")[1]));
			input = scanner.nextLine();
		}
	}

}
