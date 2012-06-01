package com.yanchuanli.games.pokr.util;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import java.net.SocketAddress;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class Util {

    public static void sendMessage(IoSession session, String msg) {
        IoBuffer answer = IoBuffer.allocate(msg.getBytes().length, false);
        answer.put(msg.getBytes());
        answer.flip();
        session.write(answer);
        answer.free();
    }

    public static String extractStringFromIoBuffer(IoBuffer buffer) {
        byte[] b = new byte[buffer.limit()];
        buffer.get(b);
        String result = new String(b);
        return result;
    }
}
