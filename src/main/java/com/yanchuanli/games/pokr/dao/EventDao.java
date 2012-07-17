package com.yanchuanli.games.pokr.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yanchuanli.games.pokr.model.Event;
import com.yanchuanli.games.pokr.util.MongoDB;
import com.yanchuanli.games.pokr.util.MongoDBFactory;
import com.yanchuanli.games.pokr.util.TimeUtil;

public class EventDao {
	
	public static void insert(Event event) {
		DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME, MongoDB.COLL_EVENT);
		
		DBObject doc = new BasicDBObject();
		doc.put("udid", event.getUdid());
		doc.put("money", event.getMoney());
		doc.put("time", TimeUtil.unixtime());
		
		coll.insert(doc);
	}
	
}
