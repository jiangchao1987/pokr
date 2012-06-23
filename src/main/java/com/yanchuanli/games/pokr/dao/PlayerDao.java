package com.yanchuanli.games.pokr.dao;

import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.util.Config;
import com.yanchuanli.games.pokr.util.URLFetchUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-22
 */
public class PlayerDao {

    private static Map<String, Player> players;
    private static int globalid = 0;

    static {
        players = new HashMap<>();
    }

   /* public static Player getPlayer(String username, int src) {
        switch (src) {
            case Config.SRC_IPHONE_GUEST:
                break;
            case Config.SRC_IPAD_GUEST:
                break;
        }
        if (!players.containsKey(username)) {
            Player player = new Player(username, username);
            player.setMoney(10000);
            players.put(username, player);
        }
        return players.get(username);

    }*/

    /**
     * 从WebServer获取用户信息 
     * 
     * @param udid
     * @param source 来源[0|1|...]
     * @return
     */
	public static Player getPlayer(String udid, String password,
			int source) {
		String htmlContent = URLFetchUtil.fetch(Config.webServerBase
				+ "login?udid=" + udid + "&password=" + password + "&source="
				+ source);
		if (htmlContent != null && !htmlContent.trim().isEmpty()) {
			String[] msgs = htmlContent.split(",");
			
			Player player = new Player(msgs[0], msgs[1]);
			player.setMoney(Integer.parseInt(msgs[2]));
			player.setExp(Integer.parseInt(msgs[3]));
			player.setWinCount(Integer.parseInt(msgs[4]));
			player.setLoseCount(Integer.parseInt(msgs[5]));
			player.setHistoricalBestHandRank(Integer.parseInt(msgs[6]));
			player.setHistoricalBestHand(msgs[7]);
			player.setMaxWin(Integer.parseInt(msgs[8]));
			player.setFace(msgs[9]);
			players.put(udid, player);
		}
		return players.get(udid);
	}

}
