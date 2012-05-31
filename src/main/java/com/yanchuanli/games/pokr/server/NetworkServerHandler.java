package com.yanchuanli.games.pokr.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IdleStatus;
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
        log.info("incomming client : " + session.getRemoteAddress());
        initMQ();
    }

    // 当一个客户端关闭时
    public void sessionClosed(IoSession session) {
        log.info("one Clinet Disconnect !");
    }

    // 当客户端发送的消息到达时:
    public void messageReceived(IoSession session, Object message) throws Exception {
        if (message instanceof IoBuffer) {
            log.info("iobuffer");
            IoBuffer buffer = (IoBuffer) message;
            SocketAddress remoteAddress = session.getRemoteAddress();
            log.info(remoteAddress);
            log.info(new String(buffer.array()));


            for (int i = 0; i < 1000; i++) {
                String result = "hello ".concat(String.valueOf(i));
                IoBuffer answer = IoBuffer.allocate(result.getBytes().length, false);
                answer.put(result.getBytes());
                answer.flip();
                session.write(answer);
                answer.free();
                Thread.sleep(10);
                log.info("["+result + "] sent");
            }

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
        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
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
