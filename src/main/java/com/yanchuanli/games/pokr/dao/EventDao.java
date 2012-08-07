package com.yanchuanli.games.pokr.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yanchuanli.games.pokr.model.Event;
import com.yanchuanli.games.pokr.model.LoginEvent;
import com.yanchuanli.games.pokr.model.WinOrLoseEvent;
import com.yanchuanli.games.pokr.util.EventConfig;
import com.yanchuanli.games.pokr.util.MongoDB;
import com.yanchuanli.games.pokr.util.MongoDBFactory;
import com.yanchuanli.games.pokr.util.TimeUtil;


public class EventDao {
	
	/**
	 * 插入个人动态
	 * @param event
	 */
	public static void insertEvent(Event event) {
		DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME,
				MongoDB.COLL_EVENT);

		DBObject doc = new BasicDBObject();
		doc.put("udid", event.getUdid());
		doc.put("time", TimeUtil.unixtime());
		doc.put("type", event.getType());

		if (event instanceof WinOrLoseEvent) {
			doc.put("roomName", ((WinOrLoseEvent) event).getRoomName());
			doc.put("nameOfBestHand", ((WinOrLoseEvent) event).getNameOfBestHand());
			doc.put("money", ((WinOrLoseEvent) event).getMoney());
		}
		
		coll.insert(doc);
	}
	
	public static void main(String[] args) {
		LoginEvent loginEvent = new LoginEvent();
		loginEvent.setUdid("lijun");
		loginEvent.setType(EventConfig.LOGIN);
		insertEvent(loginEvent);
		
		WinOrLoseEvent winEvent = new WinOrLoseEvent();
		winEvent.setUdid("lijun");
		winEvent.setType(EventConfig.WIN);
		winEvent.setRoomName("家财万贯");
		winEvent.setNameOfBestHand("21_23_51_19_31");
		winEvent.setMoney(88822);
		insertEvent(winEvent);
	}
	
}
