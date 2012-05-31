package com.yanchuanli.games.pokr.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Author: Yanchuan Li
 * Date: 5/27/12
 * Email: mail@yanchuanli.com
 */
public class ClientHandler extends IoHandlerAdapter {
    private static Logger log = Logger.getLogger(ClientHandler.class);

    public ClientHandler() {

    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        log.info("sessionCreated ...");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        SocketAddress sa = session.getLocalAddress();
        InetSocketAddress isa = (InetSocketAddress) sa;
        log.debug(isa.getHostName() + ":" + String.valueOf(isa.getPort()) + "received:" + message);
    }
}
