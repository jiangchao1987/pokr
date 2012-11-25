package com.yanchuanli.games.pokr.core;

import com.google.code.tempusfugit.temporal.Duration;
import com.yanchuanli.games.pokr.dao.PlayerDao;
import com.yanchuanli.games.pokr.dao.RoomDao;
import com.yanchuanli.games.pokr.game.Game;
import com.yanchuanli.games.pokr.game.GameConfig;
import com.yanchuanli.games.pokr.messagequeue.VoiceChatEventConsumer;
import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.model.Room;
import com.yanchuanli.games.pokr.util.Config;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-22
 */


public class GameEngine {
    private static Map<Integer, Game> games;
    private static ExecutorService pool;
    private static Logger log = Logger.getLogger(GameEngine.class);

    public static void start() {

        /*
Signal.handle(new Signal("SIGTERM"), new SignalHandler() {
public void handle(Signal sig) {
   log.debug("123");
   System.out.println("Program execution took  seconds");
//                System.exit(0);
}
});
        */


        PlayerDao.resetOnlineStatusAndRoomId();

        RoomDao.init();
        games = new HashMap<Integer, Game>();

        List<Room> roomsToPrepare = new ArrayList<>();
        roomsToPrepare.addAll(RoomDao.getRooms(Config.NORMAL_ROOM_LEVEL_BEGINNER));
        roomsToPrepare.addAll(RoomDao.getRooms(Config.NORMAL_ROOM_LEVEL_PROFESSIONAL));
        roomsToPrepare.addAll(RoomDao.getRooms(Config.NORMAL_ROOM_LEVEL_MASTER));
        roomsToPrepare.addAll(RoomDao.getRooms(Config.NORMAL_ROOM_LEVEL_VIP));
        roomsToPrepare.addAll(RoomDao.getRooms(Config.FAST_ROOM_LEVEL_BEGINNER));
        roomsToPrepare.addAll(RoomDao.getRooms(Config.FAST_ROOM_LEVEL_PROFESSIONAL));
        roomsToPrepare.addAll(RoomDao.getRooms(Config.FAST_ROOM_LEVEL_MASTER));
        roomsToPrepare.addAll(RoomDao.getRooms(Config.FAST_ROOM_LEVEL_VIP));


        pool = Executors.newFixedThreadPool(roomsToPrepare.size() + 10);

        for (Room room : roomsToPrepare) {
            GameConfig gc = new GameConfig(room.getId(), room.getName(), room.getSmallBlindAmount(), room.getBigBlindAmount(), room.getMinHolding(), room.getMaxHolding(), room.getMaxPlayersCount(), Duration.millis(room.getBettingDuration()), Duration.millis(room.getInactivityCheckInterval()), Duration.millis(room.getGameCheckInterval()));
            Game game = new Game(gc);

            games.put(room.getId(), game);
            pool.execute(game);
        }

        LiveTimeChecker lec = new LiveTimeChecker();
        pool.execute(lec);

        VoiceChatEventConsumer vcec = new VoiceChatEventConsumer();
        pool.execute(vcec);

    }

    public static Game getGame(Integer roomId) {
        if (games != null) {
            return games.get(roomId);
        }
        return null;
    }

    public static void stop() {
        for (Integer roomId : games.keySet()) {
            Game game = games.get(roomId);
            for (String s : game.getStandingPlayers().keySet()) {
                Player p = game.getStandingPlayers().get(s);
                if (p.isOnline()) {
                    p.getSession().close(true);
                }
            }
            game.stopGame();
        }
        pool.shutdown();
    }
}
