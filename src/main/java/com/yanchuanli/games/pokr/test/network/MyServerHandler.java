package com.yanchuanli.games.pokr.test.network;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-7-2
 */
public class MyServerHandler extends IoHandlerAdapter {

    private static Logger log = Logger.getLogger(MyServerHandler.class);

    public void messageReceived(IoSession session, Object message) throws Exception {
        if (message instanceof IoBuffer) {
            IoBuffer buffer = (IoBuffer) message;
            log.debug("remaining:" + buffer.remaining());
            IoBuffer mybuffer = IoBuffer.allocate(buffer.remaining()).setAutoExpand(true).setAutoShrink(true);
            while (buffer.hasRemaining()) {
                mybuffer.put(buffer.get());
                log.debug("remaining:" + buffer.remaining());
            }
        }
    }
}
