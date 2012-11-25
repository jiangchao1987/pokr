package com.yanchuanli.games.pokr.test.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.yanchuanli.games.pokr.conf.Configure;
import com.yanchuanli.games.pokr.messagequeue.VoiceChatEventConsumer;
import com.yanchuanli.games.pokr.util.ServerConfig;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Scanner;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-7-27
 */
public class TestMQServer {

    private static Logger log = Logger.getLogger(TestMQServer.class);

    private final static String QUEUE_NAME = "hello";
    private static ConnectionFactory factory;
    private static Connection connection;
    private static Channel channel;
    private static QueueingConsumer consumer;

    public static void main(String[] args) throws IOException {
        VoiceChatEventConsumer ec = new VoiceChatEventConsumer();
        Thread t = new Thread(ec);
        t.start();


        factory = new ConnectionFactory();
        factory.setHost(ServerConfig.rabbitMQServerAddress);
        connection = factory.newConnection();
        channel = connection.createChannel();

        channel.exchangeDeclare(Configure.getProperty("game_server_address"), "fanout");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("quit")) {
            channel.basicPublish(Configure.getProperty("game_server_address"), "", null, input.getBytes());
            input = scanner.nextLine();
        }

        channel.close();
        connection.close();
    }

    public static void initMQ() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(ServerConfig.rabbitMQServerAddress);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(Configure.getProperty("game_server_address"), "fanout");

        String message = "123";

        channel.basicPublish(Configure.getProperty("game_server_address"), "", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
