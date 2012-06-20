package com.jiangchao.games.pokr.util;

import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.yanchuanli.games.pokr.model.MiniRoomProtos.MiniRoom;
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
	
	public static void sendMiniRoom(IoSession session, MiniRoom miniRoom) {
		notifyOneOnTable(session, Util.miniRoomToByteArray(3, miniRoom));
	}
	
	/** 通知指定玩家。*/
    public static void notifyOneOnTable(IoSession session, byte[] byteArray) {
    	Util.sendMsg(session, byteArray);
    }
}
