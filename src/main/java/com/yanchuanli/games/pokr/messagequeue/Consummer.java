package com.yanchuanli.games.pokr.messagequeue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
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
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        log.info(" [*] Waiting for messages. To exit press CTRL+C");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());

            log.info(" [x] Received '" + message + "'");
        }
    }

}
