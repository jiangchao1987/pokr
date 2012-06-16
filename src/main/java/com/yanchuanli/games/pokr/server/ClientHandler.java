package com.yanchuanli.games.pokr.server;

import com.yanchuanli.games.pokr.util.Memory;
import com.yanchuanli.games.pokr.util.Util;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

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
        Memory.sessionsOnClient.put(String.valueOf(session.getId()), session);
        log.info("sessionCreated ...");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        if (message instanceof IoBuffer) {
            IoBuffer buffer = (IoBuffer) message;
            String info = Util.extractStringFromIoBuffer(buffer);
            log.info("[messageReceived]" + info);
        } else {
            log.info("[messageReceived]illegal");
        }
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        log.info("sessionClosed");
        super.sessionClosed(session);
        Memory.sessionsOnClient.remove(String.valueOf(session.getId()));
    }
}
