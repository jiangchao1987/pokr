package com.yanchuanli.games.pokr.messagequeue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yanchuanli.games.pokr.conf.Configure;
import com.yanchuanli.games.pokr.util.ServerConfig;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Author: Yanchuan Li
 * Date: 5/27/12
 * Email: mail@yanchuanli.com
 */
public class Producer {
    private static Logger log = Logger.getLogger(Producer.class);

    public static void main(String[] args) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(ServerConfig.rabbitMQServerAddress);
        factory.setVirtualHost(ServerConfig.rabbitMQServerVhost);
        factory.setUsername(ServerConfig.rabbitMQServerUsername);
        factory.setPassword(ServerConfig.rabbitMQServerPassword);

        Connection connection = factory.newConnection();


        Channel channel = connection.createChannel();
        channel.exchangeDeclare(Configure.getProperty("game_server_address"), "fanout");
        String message = "hello all";
        channel.basicPublish(Configure.getProperty("game_server_address"), "", null, message.getBytes());

        channel.close();
        connection.close();
    }
}
