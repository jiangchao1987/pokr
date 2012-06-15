package com.jiangchao.games.pokr.util;

import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.yanchuanli.games.pokr.core.Card;

/**
 * Note: static utils
 * Author: JiangChao
 * Date: 2012/6/14/11
 * Email: chaojiang@candou.com
 */
public class Util {
	
	public static void sendMessage(IoSession session, String input) {
        synchronized (session){
            IoBuffer answer = IoBuffer.allocate(toByte(input).length, false);
            answer.put(toByte(input));
            answer.flip();
            session.write(answer);
            answer.free();
        }
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
	
	public static byte[] toByte(String input) {
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
	
	public static String cardsToString(List<Card> cardList) {
        String result = "";
        for (Card card : cardList) {
            result += card.toChineseString() + " ";
        }
        return result;
    }
	
}
