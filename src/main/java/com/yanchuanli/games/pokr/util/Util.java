package com.yanchuanli.games.pokr.util;

import com.yanchuanli.games.pokr.core.Card;
import com.yanchuanli.games.pokr.model.Player;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import java.util.ArrayList;
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
        synchronized (session) {
            IoBuffer answer = IoBuffer.allocate(toByte(input).length, false);
            answer.put(toByte(input));
            answer.flip();
            session.write(answer);
            answer.free();
            log.debug("socket sent:" + input);
        }
    }

    public static List<String> extractStringFromIoBuffer(IoBuffer buffer) {
        List<String> list = new ArrayList<String>();
        try {
            boolean flag = true;
            int part = 0;
            while (flag) {
                if (buffer.get(part + 0) == Config.START && buffer.get(part + 2) == Config.SPLIT) {
                    int size = (int) buffer.get(part + 1);
                    if (buffer.get(part + size + 3) == Config.START) {
                        byte[] b = new byte[size];
                        for (int index = 0; index < b.length; index++) {
                            b[index] = buffer.get(part + index + 3);
                        }
                        list.add(new String(b));
                    }
                    part = part + size + 4;
                } else {
                    flag = false;
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return list;
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
