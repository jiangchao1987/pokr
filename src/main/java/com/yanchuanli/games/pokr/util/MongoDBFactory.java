package com.yanchuanli.games.pokr.util;

import com.mongodb.*;
import org.apache.log4j.Logger;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MongoDBFactory {
	protected static Logger logger = Logger.getLogger(MongoDBFactory.class);

	private static List<ServerAddress> servers;
	private static Mongo m;

	private MongoDBFactory() {
	}

	public static Mongo getMongo() {
		if (m == null) {
			servers = new ArrayList<ServerAddress>();
			try {
				for (String s : MongoDB.DBHOST) {
					servers.add(new ServerAddress(s, MongoDB.DBPORT));
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (MongoException e) {
				e.printStackTrace();
			}

            try {
                m = new Mongo("127.0.0.1", Integer.parseInt("27017"));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
//			m.setReadPreference(ReadPreference.SECONDARY);
		}

		return m;
	}

	public static DB getDB(String dbname) {
		return getMongo().getDB(dbname);
	}

	public static DBCollection getCollection(String dbname, String collection) {
		return getDB(dbname).getCollection(collection);
	}
}
