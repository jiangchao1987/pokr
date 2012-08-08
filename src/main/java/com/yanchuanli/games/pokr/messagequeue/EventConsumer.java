package com.yanchuanli.games.pokr.messagequeue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.yanchuanli.games.pokr.util.Config;
import com.yanchuanli.games.pokr.util.ServerConfig;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-7-27
 */
public class EventConsumer implements Runnable {

    private static Logger log = Logger.getLogger(EventConsumer.class);

    private static ConnectionFactory factory;
    private static Connection connection;
    private static Channel channel;
    private static QueueingConsumer consumer;
    private boolean stop = false;

    public EventConsumer() {
        factory = new ConnectionFactory();
        factory.setHost(ServerConfig.rabbitMQServerAddress);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.exchangeDeclare(Config.MQ_EXCHANGE, "fanout");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, Config.MQ_EXCHANGE, "");

            log.info(" [*] Waiting for messages. To exit press CTRL+C");

            consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, true, consumer);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        while (!stop) {

            try {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());
                log.debug(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
