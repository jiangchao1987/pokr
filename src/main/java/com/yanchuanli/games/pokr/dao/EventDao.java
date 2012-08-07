package com.yanchuanli.games.pokr.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.model.event.Event;
import com.yanchuanli.games.pokr.model.event.LoginEvent;
import com.yanchuanli.games.pokr.model.event.WinOrLoseEvent;
import com.yanchuanli.games.pokr.util.EventConfig;
import com.yanchuanli.games.pokr.util.MongoDB;
import com.yanchuanli.games.pokr.util.MongoDBFactory;
import com.yanchuanli.games.pokr.util.TimeUtil;


public class EventDao {


    public static void insertLoginEvent(Player player) {
        Event loginEvent = new LoginEvent();
        loginEvent.setType(EventConfig.LOGIN);
        loginEvent.setUdid(player.getUdid());
        insertEvent(loginEvent);
    }

    public static void insertWinOrLoseEvent(Player player, String roomName, int money, String cardIndexes, boolean winOrLose) {
        WinOrLoseEvent winEvent = new WinOrLoseEvent();
        winEvent.setUdid(player.getUdid());
        if (winOrLose) {
            winEvent.setType(EventConfig.WIN);
        } else {
            winEvent.setType(EventConfig.LOSE);
        }

        winEvent.setRoomName(roomName);
        winEvent.setNameOfBestHand(cardIndexes);
        winEvent.setMoney(money);
        insertEvent(winEvent);
    }


    /**
     * 插入个人动态
     *
     * @param event
     */
    private static void insertEvent(Event event) {
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
