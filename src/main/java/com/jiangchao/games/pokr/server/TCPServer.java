package com.jiangchao.games.pokr.server;

import com.jiangchao.games.pokr.util.NotificationCenter;
import com.jiangchao.games.pokr.util.ServiceCenter;
import com.jiangchao.games.pokr.util.Util;
import com.yanchuanli.games.pokr.model.MiniPlayerProtos.MiniPlayer;
import com.yanchuanli.games.pokr.model.MiniRoomProtos.MiniRoom;
import com.yanchuanli.games.pokr.util.Config;
import com.jiangchao.games.pokr.util.Memory;
import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;

/**
 * Copyright Candou.com Author: Yanchuan Li Email: mail@yanchuanli.com Date:
 * 12-5-31
 */
public class TCPServer {

	private static Logger log = Logger.getLogger(TCPServer.class);

	public static void main(String[] args) throws IOException {
		NioSocketAcceptor acceptor = new NioSocketAcceptor();
		acceptor.setHandler(new NetworkServerHandler());

		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		chain.addLast("threadPool",
				new ExecutorFilter(Executors.newCachedThreadPool()));
		SocketSessionConfig dcfg = acceptor.getSessionConfig();
		dcfg.setReuseAddress(true);

		acceptor.bind(new InetSocketAddress(Config.port));
		log.info("TCPServer listening on port " + Config.port);
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		while (!input.equalsIgnoreCase("quit")) {
			log.info("INPUT:" + input);
			System.out.println(Memory.sessionsOnServer.keySet().size());
			if (!Memory.sessionsOnServer.keySet().isEmpty()) {
				for (String s : Memory.sessionsOnServer.keySet()) {
					// Util.sendMessage(Memory.sessionsOnServer.get(s), input);
					NotificationCenter.sendMiniRoom(
							Memory.sessionsOnServer.get(s), getDummyRoomData());
				}
			}
			input = scanner.nextLine();
		}
		log.info("quitting now ...");
		acceptor.unbind();
		acceptor.dispose();
		ServiceCenter.getInstance().stopService();
	}

	private static MiniRoom getDummyRoomData() {
		List<MiniPlayer> miniPlayers = new ArrayList<MiniPlayer>();
		MiniPlayer miniPlayer1 = MiniPlayer.newBuilder().setId("1000")
				.setName("player-1000").setMoney(1000).setBet(200)
				.setInput("c").build();
		MiniPlayer miniPlayer2 = MiniPlayer.newBuilder().setId("1001")
				.setName("player-1001").setMoney(1001).setBet(200)
				.setInput("c").build();
		MiniPlayer miniPlayer3 = MiniPlayer.newBuilder().setId("1002")
				.setName("player-1002").setMoney(1002).setBet(200)
				.setInput("c").build();
		miniPlayers.add(miniPlayer1);
		miniPlayers.add(miniPlayer2);
		miniPlayers.add(miniPlayer3);

		MiniRoom miniRoom = MiniRoom.newBuilder().setId("1").setName("room-1")
				.addAllMiniPlayers(miniPlayers).build();
		return miniRoom;
	}

}
