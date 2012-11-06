package com.yanchuanli.games.pokr.util;

import com.yanchuanli.games.pokr.conf.Configure;

public class MongoDB {
	
//	public static final String DBHOST = "192.168.1.177";
//	public static final String DBHOST = "114.112.62.226";
	public static final String DBHOST = Configure.getProperty("db_host");
//	public static final int DBPORT = 27017;
	public static final int DBPORT = Integer.parseInt(Configure.getProperty("db_port"));
	
	public static final String DBNAME = "texas";
	public static final String COLL_USER = "user";
	public static final String COLL_ROOM = "room";
	public static final String COLL_EVENT = "event";
	public static final String COLL_MANAGER = "manager";
	
}