package com.jiangchao.games.pokr.game.server;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.Executors;

import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.jiangchao.games.pokr.game.StartGame;
import com.jiangchao.games.pokr.game.handler.TexasSHandler;
import com.jiangchao.games.pokr.util.Config;
import com.jiangchao.games.pokr.util.Memory;
import com.yanchuanli.games.pokr.util.ServiceCenter;

/**
 * Note: Texas MainServer
 * Author: JiangChao 
 * Date: 2012/6/15/13 
 * Email: chaojiang@candou.com
 */
public class TexasServer {
	NioSocketAcceptor acceptor;

	public TexasServer() {
		acceptor = new NioSocketAcceptor();
	}

	public boolean bind() {
		System.out.println("[TexasServer] bind now!");
		try {
			acceptor.setHandler(new TexasSHandler());

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
		System.out.println("[TexasServer] unbind now!");
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
		TexasServer ts = new TexasServer();
		ts.bind();
		
		// 启动Texas游戏
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		while (input.equalsIgnoreCase("start")) {
			System.out.println("[TexasServer] "
					+ Memory.playersOnServer.keySet().size());

			new StartGame().init();
			break;
		}
	}

}
