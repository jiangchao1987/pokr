package com.yanchuanli.games.pokr.util;

import com.yanchuanli.games.pokr.core.Card;
import com.yanchuanli.games.pokr.model.Player;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class Util {

    private static Logger log = Logger.getLogger(Util.class);

    public static void sendToAll(String message) {
        for (String s : Memory.sessionsOnServer.keySet()) {
            Player player = Memory.sessionsOnServer.get(s);
            Util.sendMessage(player.getSession(), message);
        }

    }

    public static void sendMessage(IoSession session, String input) {
        log.debug("socket sent:" + input);
//        synchronized (session) {
            IoBuffer answer = IoBuffer.allocate(toByte(input).length, false);
            answer.put(toByte(input));
            answer.flip();
            session.write(answer);
            answer.free();
//        }
    }

    public static String extractStringFromIoBuffer(IoBuffer buffer) {
        System.out.println(buffer.get(0) + "-" + buffer.get(2) + "-" + (int) buffer.get(1) + "-" + buffer.get((int) buffer.get(1) + 3));
        if (buffer.get(0) == Config.START && buffer.get(2) == Config.SPLIT
                && buffer.get((int) buffer.get(1) + 3) == Config.START) {
            byte[] b = new byte[(int) buffer.get(1)];
            System.out.println(b.length);
            for (int index = 0; index < b.length; index++) {
                b[index] = buffer.get(index + 3);
            }
            return new String(b);
        }
        return null;
    }

    public static String cardsToString(List<Card> cardList) {
        String result = "";
        for (Card card : cardList) {
            result += card.toChineseString() + " ";
        }
        return result;
    }

    private static byte[] toByte(String input) {
        byte[] b = new byte[input.getBytes().length + 4];
        b[0] = Config.START;
        b[1] = (byte) input.getBytes().length;
        b[2] = Config.SPLIT;
        b[b.length - 1] = (byte) Config.START;
        for (int index = 3; index < b.length - 1; index++) {
            b[index] = input.getBytes()[index - 3];
        }
        return b;
    }

}
