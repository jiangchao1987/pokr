package com.jiangchao.games.pokr.test;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.jiangchao.games.pokr.util.Config;
import com.jiangchao.games.pokr.util.ServiceCenter;

public class Server {
	private static Logger log = Logger.getLogger(Server.class);
	private NioSocketAcceptor acceptor;

	public Server() {
		acceptor = new NioSocketAcceptor();
	}

	public boolean bind() {
		try {
			acceptor.setHandler(new SHandler());

			acceptor.getFilterChain().addLast("threadPool",
					new ExecutorFilter(Executors.newCachedThreadPool()));
			acceptor.getSessionConfig().setReuseAddress(true);
			acceptor.bind(new InetSocketAddress(Config.port));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean unbind() {
		try {
			acceptor.unbind();
			acceptor.dispose();
			ServiceCenter.getInstance().stopService();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws InterruptedException {
		Server ts = new Server();
		ts.bind();

		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		while (!input.equalsIgnoreCase("quit")) {
			log.info("server input: " + input);
			Helper.sendToAllUser(input.split(":")[0], Integer.parseInt(input.split(":")[1]));
			input = scanner.nextLine();
		}
	}

}
