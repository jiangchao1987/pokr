package com.yanchuanli.games.pokr.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.yanchuanli.games.pokr.model.Room;
import com.yanchuanli.games.pokr.util.Config;
import com.yanchuanli.games.pokr.util.MongoDB;
import com.yanchuanli.games.pokr.util.MongoDBFactory;
import com.yanchuanli.games.pokr.util.ServerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-22
 */
public class RoomDao {

    public static void updateCurrentPlayerCount(int roomId, int currentPlayerCount) {
        DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME,
                MongoDB.COLL_ROOM);

        DBObject query = new BasicDBObject();
        query.put("id", roomId);

        DBObject doc = new BasicDBObject().append("$set",
                new BasicDBObject().append("currentPlayerCount", currentPlayerCount));
        coll.update(query, doc);
    }

    public static void init() {
//        delete();
        insertNormal(Config.NORMAL_ROOM_LEVEL_BEGINNER);
        insertNormal(Config.NORMAL_ROOM_LEVEL_MASTER);
        insertNormal(Config.NORMAL_ROOM_LEVEL_PROFESSIONAL);
        insertNormal(Config.NORMAL_ROOM_LEVEL_VIP);
        
        insertFast(Config.FAST_ROOM_LEVEL_BEGINNER);
        insertFast(Config.FAST_ROOM_LEVEL_MASTER);
        insertFast(Config.FAST_ROOM_LEVEL_PROFESSIONAL);
        insertFast(Config.FAST_ROOM_LEVEL_VIP);
    }

    private static void delete() {
        DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME, MongoDB.COLL_ROOM);

        coll.remove(new BasicDBObject());
    }
    
    public static void deleteByIp(String ip, int port) {
    	DBCollection coll = 
    			MongoDBFactory.getCollection(MongoDB.DBNAME, MongoDB.COLL_ROOM);

        coll.remove(new BasicDBObject("serverIp", ip).append("serverPort", port));
    }

    private static void insertNormal(int level) {
        DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME, MongoDB.COLL_ROOM);

        for (int i = 1; i < 4; i++) {
            DBObject doc = new BasicDBObject();
            int roomId = Integer.parseInt(level + "000") + i;
            doc.put("id", roomId);
            switch (i) {
                case 1:
                    doc.put("name", "锦衣玉食");
                    break;
                case 2:
                    doc.put("name", "点石成金");
                    break;
                default:
                    doc.put("name", "财源滚滚");
                    break;
            }


            doc.put("smallBlindAmount", 100 * level);
            doc.put("bigBlindAmount", 200 * level);
            doc.put("minHolding", 1000 * level);
            doc.put("maxHolding", 10000  * level);
            doc.put("maxPlayersCount", 9);
            doc.put("currentPlayerCount", 0);
            doc.put("level", level);
            doc.put("bettingDuration", 30000);
            doc.put("inactivityCheckInterval", 500);
            doc.put("gameCheckInterval", 3000);
            doc.put("serverIp", ServerConfig.gameServerAddress);
            doc.put("serverPort", ServerConfig.gameServerPort);
            coll.insert(doc);
        }
    }
    
    private static void insertFast(int level) {
        DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME, MongoDB.COLL_ROOM);

        for (int i = 1; i < 4; i++) {
            DBObject doc = new BasicDBObject();
            int roomId = Integer.parseInt(level + "000") + i;
            doc.put("id", roomId);
            switch (i) {
                case 1:
                    doc.put("name", "锦衣玉食");
                    break;
                case 2:
                    doc.put("name", "点石成金");
                    break;
                default:
                    doc.put("name", "财源滚滚");
                    break;
            }


            doc.put("smallBlindAmount", 100 * (level - 4));
            doc.put("bigBlindAmount", 200 * (level - 4));
            doc.put("minHolding", 100 * (level - 4));
            doc.put("maxHolding", 10000  * (level - 4));
            doc.put("maxPlayersCount", 9);
            doc.put("currentPlayerCount", 0);
            doc.put("level", level);
            doc.put("bettingDuration", 15000);
            doc.put("inactivityCheckInterval", 500);
            doc.put("gameCheckInterval", 3000);
            doc.put("serverIp", ServerConfig.gameServerAddress);
            doc.put("serverPort", ServerConfig.gameServerPort);
            coll.insert(doc);
        }
    }

    private static List<Room> query(int level) {
        List<Room> rooms = new ArrayList<>();

        DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME,
                MongoDB.COLL_ROOM);
        BasicDBObject query = new BasicDBObject();
        query.put("level", level);
        DBCursor cur = coll.find(query);

        while (cur.hasNext()) {
            DBObject obj = cur.next();
            Room room = new Room();
            room.setId((Integer) obj.get("id"));
            room.setName((String) obj.get("name"));
            room.setSmallBlindAmount((Integer) obj.get("smallBlindAmount"));
            room.setBigBlindAmount((Integer) obj.get("bigBlindAmount"));
            room.setMinHolding((Integer) obj.get("minHolding"));
            room.setMaxHolding((Integer) obj.get("maxHolding"));
            room.setMaxPlayersCount((Integer) obj.get("maxPlayersCount"));
            room.setCurrentPlayerCount((Integer) obj.get("currentPlayerCount"));
            room.setLevel((Integer) obj.get("level"));
            room.setBettingDuration((Integer) obj.get("bettingDuration"));
            room.setInactivityCheckInterval((Integer) obj.get("inactivityCheckInterval"));
            room.setGameCheckInterval((Integer) obj.get("gameCheckInterval"));

            rooms.add(room);
        }

        return rooms;
    }

    public static List<Room> getRooms(int level) {
        return query(level);
    }

}
