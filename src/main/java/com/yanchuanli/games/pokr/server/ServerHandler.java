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
import java.util.List;
import java.util.Map;

public class ServerHandler extends IoHandlerAdapter {
    // 当一个客端端连结进入时
    private static Logger log = Logger.getLogger(ServerHandler.class);
    private static final String EXCHANGE_NAME = "logs";
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private QueueingConsumer consumer;


    public ServerHandler() {
        super();
    }

    public void sessionOpened(IoSession session) throws Exception {
        log.info("incomming client : " + session.getRemoteAddress());
    }

    // 当一个客户端关闭时
    public void sessionClosed(IoSession session) {
        Player player = Memory.sessionsOnServer.get(String.valueOf(session.getId()));
        player.setAlive(false);
        player.setSession(null);
        Memory.sessionsOnServer.remove(String.valueOf(session.getId()));
        log.info(player.getName() + " is now disconnected !");
    }

    // 当客户端发送的消息到达时:
    public void messageReceived(IoSession session, Object message) throws Exception {
        if (message instanceof IoBuffer) {

            IoBuffer buffer = (IoBuffer) message;
            log.debug("remaining:"+buffer.remaining());

            SocketAddress remoteAddress = session.getRemoteAddress();
            log.info(remoteAddress + ":" + new String(buffer.array()));

            List<Map<Integer, String>> list = Util.ioBufferToString(buffer);
            for (Map<Integer, String> map : list) {
                ServiceCenter.getInstance().processCommand(session, map);
            }

        }

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
//        log.info("sessioncreated ...");
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
