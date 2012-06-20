package com.yanchuanli.games.pokr.util;

import com.yanchuanli.games.pokr.model.Player;
import org.apache.mina.core.session.IoSession;

import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-16
 */
public class NotificationCenter {

   /* public static void notifiAllPlayersOnTable(List<Player> players, String info) {
        for (Player player : players) {
            notifyPlayer(player, info);
        }
    }

    public static void notifyPlayer(Player player, String info) {
        if (!Config.offlineDebug) {
            Util.sendMsg(player.getSession(), info, Config.TYPE_ROOM_INGAME);
        }
    }*/
	
	public static void winorlose(IoSession session, String info, int money) {
    	notifyOneOnTable(session, info, Config.TYPE_WINORLOSE_INGAME);
    }
	
	public static void gameover(List<Player> players, String info) {
		notifyAllOnTable(players, info, Config.TYPE_GAMEOVER_INGAME);
	}
	
	public static void dealRiverCard(List<Player> players, String info) {
		notifyAllOnTable(players, info, Config.TYPE_CARD_INGAME);
	}
	
	public static void dealTurnCard(List<Player> players, String info) {
		notifyAllOnTable(players, info, Config.TYPE_CARD_INGAME);
	}
	
	public static void deal3FlipCards(List<Player> players, String info) {
		notifyAllOnTable(players, info, Config.TYPE_CARD_INGAME);
	}
	
	public static void doBettingRound(List<Player> players, String info) {
		notifyAllOnTable(players, info, Config.TYPE_CARD_INGAME);
	}
	
	public static void act(IoSession session, String info) {
		notifyOneOnTable(session, info, Config.TYPE_ACTION_INGAME);
	}
	
	public static void deal2Cards(IoSession session, String info) {
		notifyOneOnTable(session, info, Config.TYPE_HOLE_INGAME);
	}
    
	public static void sayHello(List<Player> players, String info) {
		notifyAllOnTable(players, info, Config.TYPE_USER_INGAME);
	}
    
    public static void login(IoSession session, String info) {
    	notifyOneOnTable(session, info, Config.TYPE_LOGIN_INGAME);
    }
    
    /** 通知桌面上所有玩家。*/
    public static void notifyAllOnTable(List<Player> players, String info, int type) {
    	for (Player player : players) {
    		notifyOneOnTable(player.getSession(), info, type);
        }
    }
    
    /** 通知指定玩家。*/
    public static void notifyOneOnTable(IoSession session, String info, int type) {
    	Util.sendMsg(session, info, type);
    }

}
