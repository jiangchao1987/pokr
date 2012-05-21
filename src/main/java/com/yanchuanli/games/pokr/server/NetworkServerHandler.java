package com.yanchuanli.games.pokr.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class NetworkServerHandler extends IoHandlerAdapter {
    // 当一个客端端连结进入时
    private static Logger log = Logger.getLogger(NetworkServerHandler.class);

    public NetworkServerHandler() {
        super();
    }

    public void sessionOpened(IoSession session) throws Exception {
        log.info("incomming client : " + session.getRemoteAddress());
    }

    // 当一个客户端关闭时
    public void sessionClosed(IoSession session) {
        log.info("one Clinet Disconnect !");
    }

    // 当客户端发送的消息到达时:
    public void messageReceived(IoSession session, Object message) throws Exception {

        String s = (String) message;
        log.debug(s);


//        session.write("none");

    }

    // 发送消息给客户机器
    public void messageSent(IoSession session, Object message) throws Exception {
        // log.info("发送消息给客户端: " + message);
    }

    // 发送消息异常
    public void exceptionCaught(IoSession session, Throwable cause) {
        session.close();
    }

    // //sessiong空闲
    // public void sessionIdle( IoSession session, IdleStatus status )
    // {
    // }
    // 创建 session
    public void sessionCreated(IoSession session) {

    }
}
