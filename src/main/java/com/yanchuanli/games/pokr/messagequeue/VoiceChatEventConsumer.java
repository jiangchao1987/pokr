package com.yanchuanli.games.pokr.messagequeue;

import com.rabbitmq.client.*;
import com.yanchuanli.games.pokr.conf.Configure;
import com.yanchuanli.games.pokr.core.GameEngine;
import com.yanchuanli.games.pokr.game.Game;
import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.util.Memory;
import com.yanchuanli.games.pokr.util.ServerConfig;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-7-27
 */
public class VoiceChatEventConsumer implements Runnable {

    private static Logger log = Logger.getLogger(VoiceChatEventConsumer.class);

    private static ConnectionFactory factory;
    private static Connection connection;
    private static Channel channel;
    private static QueueingConsumer consumer;
    private boolean stop = false;

    public VoiceChatEventConsumer() {
        factory = new ConnectionFactory();
        factory.setHost(ServerConfig.rabbitMQServerAddress);
        factory.setVirtualHost(ServerConfig.rabbitMQServerVhost);
        factory.setUsername(ServerConfig.rabbitMQServerUsername);
        factory.setPassword(ServerConfig.rabbitMQServerPassword);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.exchangeDeclare(Configure.getProperty("game_server_address"), "fanout");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, Configure.getProperty("game_server_address"), "");
            consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, true, consumer);
            log.debug("waiting for incoming msgs ...");
        } catch (IOException e1) {
            log.error(e1);
        }
    }

    @Override
    public void run() {
        while (!stop) {
            QueueingConsumer.Delivery delivery = null;
            try {
                delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());
                log.debug(message);
                String[] msgs = message.split(",");
                String udid = String.valueOf(msgs[0]);
                String roomid = String.valueOf(msgs[1]);
                String filepath = String.valueOf(msgs[2]);
                Game game = GameEngine.getGame(Integer.parseInt(roomid));
                Player player = Memory.playersOnServer.get(udid);
                if (game != null && player != null) {
                    game.voiceChat(player, filepath);
                }
            } catch (ShutdownSignalException e) {
                log.error("ShutdownSignalException", e);

                if (channel.isOpen()) {
                    try {
                        channel.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                if (connection.isOpen()) {
                    try {
                        connection.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                try {
                    connection = factory.newConnection();
                    channel = connection.createChannel();
                    channel.exchangeDeclare(Configure.getProperty("game_server_address"), "fanout");
                    String queueName = channel.queueDeclare().getQueue();
                    channel.queueBind(queueName, Configure.getProperty("game_server_address"), "");
                    consumer = new QueueingConsumer(channel);
                    channel.basicConsume(queueName, true, consumer);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (InterruptedException e1) {
                log.error(e1);
            }


        }
        try {
            channel.close();
            connection.close();
        } catch (IOException e) {
            log.error(e);
        }

    }


    public static void main(String[] args) {
        VoiceChatEventConsumer ec = new VoiceChatEventConsumer();
        Thread t = new Thread(ec);
        t.start();
    }
}
