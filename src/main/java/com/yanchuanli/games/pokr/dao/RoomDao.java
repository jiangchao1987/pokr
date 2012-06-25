package com.yanchuanli.games.pokr.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.yanchuanli.games.pokr.model.Room;
import com.yanchuanli.games.pokr.util.Config;
import com.yanchuanli.games.pokr.util.MongoDB;
import com.yanchuanli.games.pokr.util.MongoDBFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-22
 */
public class RoomDao {
	
    public static void init() {
    	delete();
    	
    	insert(Config.ROOM_LEVEL_BEGINNER);
    	insert(Config.ROOM_LEVEL_MASTER);
    	insert(Config.ROOM_LEVEL_PROFESSIONAL);
    	insert(Config.ROOM_LEVEL_VIP);
    }
    
    private static void delete() {
    	DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME,
				MongoDB.COLL_ROOM);
    	
    	coll.remove(new BasicDBObject());
    }
    
    private static void insert(int level) {
    	DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME,
				MongoDB.COLL_ROOM);
    	
    	for (int i = 1; i < 3; i++) {
    		DBObject doc = new BasicDBObject();
    		int roomId = Integer.parseInt(level + "000") + i;
    		doc.put("id", roomId);
    		doc.put("name", "room" + roomId);
    		doc.put("smallBlindAmount", 5 * level);
    		doc.put("bigBlindAmount", 10 * level);
    		doc.put("minHolding", 100 * level);
    		doc.put("maxHolding", 20000 * 5 * level);
    		doc.put("maxPlayersCount", 9);
    		doc.put("currentPlayerCount", 0);
    		doc.put("level", level);
    		doc.put("bettingDuration", 5000);
    		doc.put("inactivityCheckInterval", 500);
    		doc.put("gameCheckInterval", 3000);
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

    /*public static List<Integer> getRooms(int roomlevel) {
        switch (roomlevel) {
            case Config.ROOM_LEVEL_BEGINNER:
                break;
            case Config.ROOM_LEVEL_PROFESSIONAL:
                break;
            case Config.ROOM_LEVEL_MASTER:
                break;
            case Config.ROOM_LEVEL_VIP:
                break;
        }

        List<Integer> rooms = new ArrayList<Integer>();
		rooms.add(1001);
		rooms.add(1002);
		return rooms;
    }*/
    
    public static List<Room> getRooms(int level) {
		return query(level);
    }
    
}
