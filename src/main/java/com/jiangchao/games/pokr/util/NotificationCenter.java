package com.jiangchao.games.pokr.util;

import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.yanchuanli.games.pokr.model.Player;

/**
 * Note: Notification Center
 * Author: JiangChao 
 * Date: 2012/6/18/13 
 * Email: chaojiang@candou.com
 */
public class NotificationCenter {

	public static void notify(Map<String, Player> playerMap, String input, IoSession session) {
		for (String s : playerMap.keySet()) {
			Player player = playerMap.get(s);
			if (player.getSession().getId() == session.getId()) {
				player.setInput(input);
			}
		}
	}
}
