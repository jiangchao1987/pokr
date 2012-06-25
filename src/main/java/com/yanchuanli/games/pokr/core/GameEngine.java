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
    private static Map<Integer, Game> games;

    public static void start() {
        RoomDao.init();
        games = new HashMap<Integer, Game>();
        List<Integer> roomsToPrepare = RoomDao.getRooms(Config.ROOM_LEVEL_BEGINNER);

        for (Integer roomId : roomsToPrepare) {
            GameConfig gc = new GameConfig(roomId, "room" + roomId, 20, 40, 0, 10000, 9, Duration.seconds(3000), Duration.millis(500), Duration.seconds(1));
            Game game = new Game(gc);
            games.put(roomId, game);
            new Thread(game).start();
        }
    }

    public static Game getGame(Integer roomId) {
        return games.get(roomId);
    }

    public static void stop() {
        for (Integer roomId : games.keySet()) {
            Game game = games.get(roomId);
            game.stopGame();
        }
    }
}
