package com.yanchuanli.games.pokr.messagequeue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.yanchuanli.games.pokr.conf.Configure;
import com.yanchuanli.games.pokr.server.ServerConfig;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Author: Yanchuan Li
 * Date: 5/27/12
 * Email: mail@yanchuanli.com
 */
public class Consummer {

    private static Logger log = Logger.getLogger(Consummer.class);
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost(Configure.getProperty("rabbitmq_server_address"));
//        factory.setVirtualHost("texas");
//        factory.setUsername("texas");
//        factory.setPassword("arubatexas");

        factory.setHost(ServerConfig.rabbitMQServerAddress);
        factory.setVirtualHost(ServerConfig.rabbitMQServerVhost);
        factory.setUsername(ServerConfig.rabbitMQServerUsername);
        factory.setPassword(ServerConfig.rabbitMQServerPassword);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(Configure.getProperty("game_server_address"), "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, Configure.getProperty("game_server_address"), "");

        log.info(" [*] Waiting for messages. To exit press CTRL+C");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

//        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());

            log.info(" [x] Received '" + message + "'");
//        }
    }

}
