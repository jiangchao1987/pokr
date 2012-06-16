package com.yanchuanli.games.pokr.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.util.Memory;
import com.yanchuanli.games.pokr.util.ServiceCenter;
import com.yanchuanli.games.pokr.util.Util;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import java.io.IOException;
import java.net.SocketAddress;

public class NetworkServerHandler extends IoHandlerAdapter {
    // 当一个客端端连结进入时
    private static Logger log = Logger.getLogger(NetworkServerHandler.class);
    private static final String EXCHANGE_NAME = "logs";
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private QueueingConsumer consumer;


    public NetworkServerHandler() {
        super();
    }

    public void sessionOpened(IoSession session) throws Exception {
//        Memory.sessionsOnServer.put(String.valueOf(session.getId()), session);
        Player player = new Player(String.valueOf(session.getId()), String.valueOf("Player" + session.getId()), false);
        player.setMoney(10000);
        player.setSession(session);
        Memory.sessionsOnServer.put(String.valueOf(session.getId()), player);
        log.info("incomming client : " + session.getRemoteAddress());
        Util.sendMessage(session, "Hello Player" + String.valueOf("Player" + session.getId()));
//        initMQ();
    }

    // 当一个客户端关闭时
    public void sessionClosed(IoSession session) {
        Memory.sessionsOnServer.remove(String.valueOf(session.getId()));
        log.info("one Client Disconnect !");
    }

    // 当客户端发送的消息到达时:
    public void messageReceived(IoSession session, Object message) throws Exception {
        if (message instanceof IoBuffer) {

            IoBuffer buffer = (IoBuffer) message;
            SocketAddress remoteAddress = session.getRemoteAddress();
            log.info(remoteAddress);
            log.info(new String(buffer.array()));

            String cmd = Util.extractStringFromIoBuffer(buffer);
            log.info("received:" + cmd);
            ServiceCenter.getInstance().processCommand(session, cmd);


        }


//        while (true) {
//            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//            String mqmessage = new String(delivery.getBody());
//            session.write(mqmessage);
//            log.info("[x] Received '" + message + "'");
//        }

    }


    // 发送消息给客户机器
    public void messageSent(IoSession session, Object message) throws Exception {
        // log.info("发送消息给客户端: " + message);
    }

    // 发送消息异常
    public void exceptionCaught(IoSession session, Throwable cause) {
//        session.close();
    }

    // //sessiong空闲
    // public void sessionIdle( IoSession session, IdleStatus status )
    // {
    // }
    // 创建 session
    public void sessionCreated(IoSession session) {
        log.info("sessioncreated ...");
//        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
    }

    private void initMQ() throws IOException {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        log.info(" [*] Waiting for messages. To exit press CTRL+C");

        consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
    }
}
