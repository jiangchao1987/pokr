package com.yanchuanli.games.pokr.core;

import com.google.code.tempusfugit.temporal.Duration;
import com.yanchuanli.games.pokr.dao.RoomDao;
import com.yanchuanli.games.pokr.game.Game;
import com.yanchuanli.games.pokr.game.GameConfig;
import com.yanchuanli.games.pokr.util.Config;

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
        List<String> roomsToPrepare = RoomDao.getRooms(Config.ROOM_LEVEL_BEGINNER);

        int id = 0;
        for (String name : roomsToPrepare) {
            GameConfig gc = new GameConfig(id, name, 20, 40, 0, 10000, 9, Duration.seconds(30), Duration.millis(500));
            Game game = new Game(gc);
            games.put(name, game);
            id++;
            new Thread(game).start();
        }
    }

    public static Game getGame(String name) {
        return games.get(name);
    }

    public static void stop() {
        for (String s : games.keySet()) {
            Game game = games.get(s);
            game.stopGame();
        }
    }
}
