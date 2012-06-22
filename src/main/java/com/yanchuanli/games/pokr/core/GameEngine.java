package com.yanchuanli.games.pokr.core;

import com.yanchuanli.games.pokr.dao.RoomDao;
import com.yanchuanli.games.pokr.game.Game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-22
 */
public class GameEngine {
    private static Map<String, Game> games;

    public static void start() {
        games = new HashMap<String, Game>();
        List<String> roomsToPrepare = RoomDao.getRooms();
        int id = 0;
        for (String name : roomsToPrepare) {
            Game game = new Game(id++, name);
            games.put(name, game);
        }
    }
}
