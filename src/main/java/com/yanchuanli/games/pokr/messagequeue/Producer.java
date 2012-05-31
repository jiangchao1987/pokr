package com.yanchuanli.games.pokr.messagequeue;

import com.rabbitmq.client.*;
import org.apache.log4j.Logger;
import org.junit.internal.matchers.StringContains;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

/**
 * Author: Yanchuan Li
 * Date: 5/27/12
 * Email: mail@yanchuanli.com
 */
public class Producer {
    private static Logger log = Logger.getLogger(Producer.class);
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String message = "hello all";
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());

        channel.close();
        connection.close();
    }
}
