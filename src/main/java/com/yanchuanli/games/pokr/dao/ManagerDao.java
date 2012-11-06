package com.yanchuanli.games.pokr.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.yanchuanli.games.pokr.util.MongoDB;
import com.yanchuanli.games.pokr.util.MongoDBFactory;

public class ManagerDao {

    public static boolean validateAdmin(String name, String pwd) {
        boolean result = false;

        DBCollection coll = MongoDBFactory.getCollection(MongoDB.DBNAME,
                MongoDB.COLL_MANAGER);
        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        query.put("pwd", pwd);
        DBCursor cur = coll.find(query);

        while (cur.hasNext()) {
            result = true;
        }

        return result;
    }

}
