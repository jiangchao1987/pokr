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
	
	/** 
	 * 通知所有玩家当前轮到的发牌玩家id
	 * 
	 * @param players 所有玩家
	 * @param info 用户id
	 */
	public static void rotate(List<Player> players, String info) {
		notifyAllOnTable(players, info, Config.TYPE_ROTATE_INGAME);
	}

    /*
     * sample msg: id,action:[bet],moneyontable
     */
    public static void forwardAction(List<Player> players, String info) {
        notifyAllOnTable(players, info, Config.TYPE_OTHERSACTION_INGAME);
    }

    public static void list(IoSession session, String info) {
        notifyOneOnTable(session, info, Config.TYPE_LIST_INGAME);
    }

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

    /**
     * 通知桌面上所有玩家。
     */
    private static void notifyAllOnTable(List<Player> players, String info, int type) {
        for (Player player : players) {
            notifyOneOnTable(player.getSession(), info, type);
        }
    }

    /**
     * 通知指定玩家。
     */
    private static void notifyOneOnTable(IoSession session, String info, int type) {
        Util.sendMsg(session, info, type);
    }

}
