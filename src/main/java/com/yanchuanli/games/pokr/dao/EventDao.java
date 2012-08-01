package com.yanchuanli.games.pokr.dao;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.yanchuanli.games.pokr.event.Event;
import com.yanchuanli.games.pokr.util.MongoDB;
import com.yanchuanli.games.pokr.util.MongoDBFactory;
import com.yanchuanli.games.pokr.util.TimeUtil;

public class EventDao {
	
	/**
     * 持久化event
     * 
     * @param event
     */
    public static void insertEvent(Event event) {
    	DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME,
				MongoDB.COLL_EVENT);

		DBObject doc = new BasicDBObject();
		doc.put("udid", event.getUdid());
		doc.put("timestamp", TimeUtil.unixtime());
		doc.put("type", event.getType());
		doc.put("info", event.getInfo());
		doc.put("processed", event.getProcessed());

		coll.insert(doc);
    }
    
    
    /**
     * 更新event processed字段
     * 
     * @param event
     */
	public static void eventProcessed(Event event) {
		DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME,
				MongoDB.COLL_EVENT);

		DBObject query = new BasicDBObject();
		query.put("udid", event.getUdid());

		DBObject doc = new BasicDBObject().append("$set", new BasicDBObject(
				"processed", event.getProcessed()));
		coll.update(query, doc);
	}
    
    /**
     * 查找processed字段为0的event
     * 
     * @param udid
     * @return List<Event>
     */
	public static List<Event> getUnprocessedEvents(String udid) {
		List<Event> events = new ArrayList<Event>();

		DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME,
				MongoDB.COLL_EVENT);

		DBCursor cur = coll.find(new BasicDBObject("udid",
				udid));
		while (cur.hasNext()) {
			DBObject obj = cur.next();

			Event event = new Event();
			event.setUdid(udid);
			event.setTimestamp((Integer) obj.get("timestamp"));
			event.setType((Integer) obj.get("type"));
			event.setInfo((String) obj.get("info"));
			event.setProcessed((Integer) obj.get("processed"));
			events.add(event);
		}
		return events;
	}
	
}
