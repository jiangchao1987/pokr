package com.yanchuanli.games.pokr.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.util.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-22
 */
public class PlayerDao {

    private static Map<String, Player> players;
    private static int globalid = 0;

    static {
        players = new HashMap<>();
    }
    
    /**
     * 从WebServer获取用户信息
     *
     * @param udid
     * @param source 来源[0|1|...]
     * @return
     */
    public static Player getPlayer(String udid, String password,
                                   int source) {
        String htmlContent = URLFetchUtil.fetch(Config.webServerBase
                + "login?udid=" + udid + "&password=" + password + "&source="
                + source);
        if (htmlContent != null && !htmlContent.trim().isEmpty()) {
            String[] msgs = htmlContent.split(",");

            Player player = new Player(msgs[0], msgs[1]);
            player.setTotalMoney(Integer.parseInt(msgs[2]));
            player.setExp(Integer.parseInt(msgs[3]));
            player.setWinCount(Integer.parseInt(msgs[4]));
            player.setLoseCount(Integer.parseInt(msgs[5]));
            player.setHistoricalBestHandRank(Integer.parseInt(msgs[6]));
            player.setHistoricalBestHand(msgs[7]);
            player.setMaxWin(Integer.parseInt(msgs[8]));
            player.setCustomAvatar(Integer.parseInt(msgs[9]));
            player.setAvatar(msgs[10]);
            player.setSex(Integer.parseInt(msgs[11]));
            player.setAddress(msgs[12]);
            players.put(udid, player);
        }
        return players.get(udid);
    }
    
    /**
     * 更新赢的次数
     * 
     * @param udid
     * @param winCount
     */
    public static void updateWinCount(String udid, int winCount) {
        DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME,
                MongoDB.COLL_USER);

        DBObject query = new BasicDBObject();
        query.put("udid", udid);

        DBObject doc = new BasicDBObject().append("$set",
                new BasicDBObject().append("win", winCount).append("update", TimeUtil.unixtime()));
        coll.update(query, doc);
    }
    
    /**
     * 更新输的次数
     * 
     * @param udid
     * @param loseCount
     */
    public static void updateLoseCount(String udid, int loseCount) {
        DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME,
                MongoDB.COLL_USER);

        DBObject query = new BasicDBObject();
        query.put("udid", udid);

        DBObject doc = new BasicDBObject().append("$set",
                new BasicDBObject().append("lose", loseCount).append("update", TimeUtil.unixtime()));
        coll.update(query, doc);
    }

    /**
     * 更新资产
     *
     * @param udid
     * @param money
     */
    public static void updateMoney(String udid, int money) {
        DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME,
                MongoDB.COLL_USER);

        DBObject query = new BasicDBObject();
        query.put("udid", udid);

        DBObject doc = new BasicDBObject().append("$set",
                new BasicDBObject().append("money", money).append("update", TimeUtil.unixtime()));
        coll.update(query, doc);
    }

    /**
     * 更新bestHand以及bestHandRank
     *
     * @param targetPlayer
     */
    public static void updateBestHandOfPlayer(Player targetPlayer) {
        Player player = queryByUdid(targetPlayer.getUdid());
        if (player != null) {
            if (player.getHistoricalBestHandRank() < targetPlayer.getBestHandRank()) {
                DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME,
                        MongoDB.COLL_USER);

                DBObject query = new BasicDBObject();
                query.put("udid", targetPlayer.getUdid());

                DBObject doc = new BasicDBObject().append("$set", new BasicDBObject()
                        .append("best", targetPlayer.getBestHand().getGIndexes()).append("br", targetPlayer.getBestHandRank()).append("update", TimeUtil.unixtime()));
                coll.update(query, doc);
            }
        }
    }

    /**
     * 更新最大赢取MaxWin
     *
     * @param udid
     * @param maxWin
     */
    public static void updateMaxWin(String udid, int maxWin) {
        Player player = queryByUdid(udid);
        if (player != null && (player.getMaxWin() < maxWin)) {
            DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME,
                    MongoDB.COLL_USER);

            DBObject query = new BasicDBObject();
            query.put("udid", udid);

            DBObject doc = new BasicDBObject().append("$set",
                    new BasicDBObject().append("max", maxWin).append("update", TimeUtil.unixtime()));
            coll.update(query, doc);
        }
    }

    public static Player queryByUdid(String udid) {
        DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME,
                MongoDB.COLL_USER);

        BasicDBObject query = new BasicDBObject();
        query.put("udid", udid);
        DBCursor cur = coll.find(query);

        Player player = null;
        while (cur.hasNext()) {
            DBObject obj = cur.next();
            player = new Player((String) obj.get("udid"), (String) obj.get("name"));
            player.setMoney((Integer) obj.get("money"));
            player.setExp((Integer) obj.get("exp"));
            player.setWinCount((Integer) obj.get("win"));
            player.setLoseCount((Integer) obj.get("lose"));
            player.setHistoricalBestHandRank((Integer) obj.get("br"));
            player.setHistoricalBestHand((String) obj.get("best"));
            player.setMaxWin((Integer) obj.get("max"));
            player.setCustomAvatar((Integer) obj.get("customAvatar"));
            player.setAvatar((String) obj.get("face"));
            player.setSex((Integer) obj.get("sex"));
            player.setAddress((String) obj.get("address"));
        }

        return player;
    }

    public void buyIn(Player player, int buyIn) {
        if (player.getTotalMoney() > buyIn) {
            player.setMoney(buyIn);

        } else {
            //TODO 弹出购买IAP的按钮
        }
    }

    public void cashBack(Player player, int holding) {

    }

}
