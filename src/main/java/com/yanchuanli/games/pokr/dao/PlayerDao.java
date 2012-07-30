package com.yanchuanli.games.pokr.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.util.*;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-22
 */

/**
 * @author ASUS
 */
public class PlayerDao {

    private static Map<String, Player> players;
    private static int globalid = 0;
    private static Logger log = Logger.getLogger(PlayerDao.class);

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
        String htmlContent = URLFetchUtil.fetch(ServerConfig.webServerBase
                + "login?udid=" + udid + "&password=" + password + "&source="
                + source);
//        log.debug(htmlContent);
        if (htmlContent != null && !htmlContent.trim().isEmpty()) {
            String[] msgs = htmlContent.split(",");

            Player player = new Player(msgs[0], msgs[1]);
            player.setTotalMoney(Integer.parseInt(msgs[2]));
            player.setExp(Integer.parseInt(msgs[3]));
            player.setCurrentLevel(Integer.parseInt(msgs[4]));
            player.setWinCount(Integer.parseInt(msgs[5]));
            player.setLoseCount(Integer.parseInt(msgs[6]));
            player.setHistoricalBestHandRank(Integer.parseInt(msgs[7]));
            player.setHistoricalBestHand(msgs[8]);
            player.setMaxWin(Integer.parseInt(msgs[9]));
            player.setCustomAvatar(Integer.parseInt(msgs[10]));
            player.setAvatar(msgs[11]);
            player.setSex(Integer.parseInt(msgs[12]));
            player.setAddress(msgs[13]);
            player.setRoomid(Integer.MIN_VALUE);
            player.setLastTime(TimeUtil.unixtime());
            players.put(udid, player);
        }
        return players.get(udid);
    }

    /**
     * 更新赢的次数
     *
     * @param player
     */
    public static void updateWinCount(Player player) {
        DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME, MongoDB.COLL_USER);
        DBObject searchQuery = new BasicDBObject("udid", player.getUdid());
        DBObject incQuery = new BasicDBObject("$inc", new BasicDBObject("win", 1));
        coll.update(searchQuery, incQuery);
        player.setWinCount(player.getWinCount() + 1);
    }

    /**
     * 更新输的次数
     *
     * @param player
     */
    public static void updateLoseCount(Player player) {
        DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME, MongoDB.COLL_USER);
        DBObject searchQuery = new BasicDBObject("udid", player.getUdid());
        DBObject incQuery = new BasicDBObject("$inc", new BasicDBObject("lose", 1));
        coll.update(searchQuery, incQuery);
        player.setLoseCount(player.getLoseCount() + 1);
    }

    /**
     * 更新资产
     *
     * @param udid
     * @param money
     */
    private static void updateMoney(String udid, int money) {
        DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME,
                MongoDB.COLL_USER);

        DBObject query = new BasicDBObject();
        query.put("udid", udid);

        DBObject doc = new BasicDBObject().append("$set",
                new BasicDBObject().append("money", money));
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
                        .append("best", targetPlayer.getBestHand().getGIndexes()).append("br", targetPlayer.getBestHandRank()));
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
                    new BasicDBObject().append("max", maxWin));
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
            player.setTotalMoney((Integer) obj.get("money"));
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
            player.setRoomid(Integer.MIN_VALUE);
        }

        return player;
    }

    public static boolean buyIn(Player player, int buyIn) {
        boolean result = false;
        if (player.getTotalMoney() > buyIn) {
            player.setMoney(buyIn);
            log.debug("updatemoney:" + player.getUdid() + ":" + player.getTotalMoney() + "-" + buyIn + "=" + (player.getTotalMoney() - buyIn));
            log.debug(player.getName() + " has buyed in " + player.getMoney());
            result = true;
        }
        return result;
    }

    public static void cashBack(Player player, int holding) {
        log.debug("cashback:" + player.getUdid() + ":" + holding);
        Player persistence = queryByUdid(player.getUdid());
        // plus money
        updateMoney(player.getUdid(), persistence.getTotalMoney() + holding);

    }

    /**
     * 增加经验值
     *
     * @param player
     * @param exp
     * @return 增加exp后的player
     */
    public static void updateExpAndLastTime(Player player, int exp) {
        DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME, MongoDB.COLL_USER);
        DBObject searchQuery = new BasicDBObject("udid", player.getUdid());
        DBObject incQuery = new BasicDBObject("$inc", new BasicDBObject("exp", exp));
        DBObject updateOnlineTime = new BasicDBObject("$set", new BasicDBObject("update", TimeUtil.unixtime()));
        coll.update(searchQuery, incQuery);
        coll.update(searchQuery, updateOnlineTime);
        player.setExp(player.getExp() + exp);
    }

    public static void updateExp(Player player, int exp) {
        DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME, MongoDB.COLL_USER);
        DBObject searchQuery = new BasicDBObject("udid", player.getUdid());
        DBObject incQuery = new BasicDBObject("$inc", new BasicDBObject("exp", exp));
        coll.update(searchQuery, incQuery);
        player.setExp(player.getExp() + exp);
    }

    public static void updateLastTime(Player player) {
        int now = TimeUtil.unixtime();
        DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME, MongoDB.COLL_USER);
        DBObject searchQuery = new BasicDBObject("udid", player.getUdid());
        DBObject updateOnlineTime = new BasicDBObject("$set", new BasicDBObject("update", now));
        coll.update(searchQuery, updateOnlineTime);
        player.setLastTime(now);
    }

}
