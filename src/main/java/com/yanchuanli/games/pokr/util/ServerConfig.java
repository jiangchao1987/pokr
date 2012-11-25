package com.yanchuanli.games.pokr.util;

import com.yanchuanli.games.pokr.conf.Configure;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-7-27
 */
public class ServerConfig {

    public static final int gameServerPort = Integer.parseInt(Configure.getProperty("game_server_port"));
    public static final String gameServerAddress = Configure.getProperty("game_server_address");
    public static final String webServerBase = Configure.getProperty("webserver_base");
    public static final String rabbitMQServerAddress = Configure.getProperty("rabbitmq_server_address");
    public static final String rabbitMQServerVhost = Configure.getProperty("rabbitmq_server_vhost");
    public static final String rabbitMQServerUsername=Configure.getProperty("rabbitmq_server_username");
    public static final String rabbitMQServerPassword=Configure.getProperty("rabbitmq_server_password");



}
