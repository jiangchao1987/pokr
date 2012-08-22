package com.yanchuanli.games.pokr.util;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

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

			m = new Mongo(servers);
			m.setReadPreference(ReadPreference.SECONDARY);
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
