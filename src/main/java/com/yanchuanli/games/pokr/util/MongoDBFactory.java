package com.yanchuanli.games.pokr.util;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.yanchuanli.games.pokr.conf.Configure;
import org.apache.log4j.Logger;

import java.net.UnknownHostException;

public class MongoDBFactory {
    protected static Logger logger = Logger.getLogger(MongoDBFactory.class);

    public static final String MONGO_HOST = Configure.getProperty("mongodb_address");
    public static final int MONGO_PORT = Integer.parseInt(Configure.getProperty("mongodb_port"));
    private static Mongo mongo;

    private MongoDBFactory() {
    }

    public static Mongo getMongo() {
        if (mongo == null) {
            try {
                mongo = new Mongo(MONGO_HOST, MONGO_PORT);
            } catch (UnknownHostException e) {
                logger.error(e);
            } catch (MongoException e) {
                logger.error(e);
            }
        }

        return mongo;
    }

    public static DB getDB(String dbname) {
        return getMongo().getDB(dbname);
    }

    public static DBCollection getCollection(String dbname, String collection) {
//		logger.debug("Retrieving collection: " + collection);
        return getDB(dbname).getCollection(collection);
    }
}