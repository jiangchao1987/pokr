package com.yanchuanli.games.pokr.util;

import com.yanchuanli.games.pokr.basic.Card;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class Util {

    private static Logger log = Logger.getLogger(Util.class);
    private static List<Byte> memoryByteList = new ArrayList<Byte>();

    public static String cardsToString(List<Card> cardList) {
        String result = "";
        for (Card card : cardList) {
            result += card.toChineseString() + " ";
        }
        return result;
    }

    public static String cardsToGIndexes(List<Card> cardList) {
        String result = "";
        for (Card card : cardList) {
            result += card.getIndex() + "_";
        }
        return result;
    }

    public static byte[] intToByte(int i) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (0xff & i);
        bytes[1] = (byte) ((0xff00 & i) >> 8);
        bytes[2] = (byte) ((0xff0000 & i) >> 16);
        bytes[3] = (byte) ((0xff000000 & i) >> 24);
        return bytes;
    }

    public static int bytesToInt(byte[] bytes) {
        int length = bytes[0] & 0xFF;
        length |= ((bytes[1] << 8) & 0xFF00);
        length |= ((bytes[2] << 16) & 0xFF0000);
        length |= ((bytes[3] << 24) & 0xFF000000);
        return length;
    }

    public static byte[] stringToByteArray(int type, String input) {
        return encodeByteArray(type, input.getBytes());
    }

    public static List<Map<Integer, String>> ioBufferToString(IoBuffer buffer) {
        List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();

        List<Map<Integer, byte[]>> byteArrayList = decodeByteArray(buffer);
        for (Map<Integer, byte[]> byteArrayMap : byteArrayList) {
            Map<Integer, String> map = new HashMap<Integer, String>();
            for (Integer key : byteArrayMap.keySet()) {
                map.put(key, new String(byteArrayMap.get(key)));
            }
            list.add(map);
        }

        return list;
    }

    /**
     * 给byteArray加上约定好的head信息。
     */
    private static byte[] encodeByteArray(int type, byte[] byteArray) {
        byte[] b = null;
        byte[] length = null;

        b = new byte[byteArray.length + 8];
        length = intToByte(byteArray.length);
        b[3] = length[0];
        b[4] = length[1];
        b[5] = length[2];
        b[6] = length[3];
        for (int index = 7; index < b.length - 1; index++) {
            b[index] = byteArray[index - 7];
        }
        b[0] = Config.START;
        b[1] = (byte) type;
        b[2] = Config.SPLIT;

        b[b.length - 1] = (byte) Config.START;
//		log.info("encodeByteArray: " + new String(b));
        return b;
    }

    /**
     * 从接收到的数据中解析出内容byteArray的Map。
     */
    private static List<Map<Integer, byte[]>> decodeByteArray(IoBuffer buffer) {
		List<Map<Integer, byte[]>> list = new ArrayList<Map<Integer, byte[]>>();

		List<Byte> tempByteList = new ArrayList<Byte>();
		while (buffer.hasRemaining()) {
			byte tempByte = buffer.get();
			tempByteList.add(tempByte);
		}

		memoryByteList.addAll(tempByteList);
		if (tempByteList.get(tempByteList.size() - 1) == Config.START) {
			list = generateByteList();
			memoryByteList.clear();
		}
		
		return list;
	}
	
	private static List<Map<Integer, byte[]>> generateByteList() {
		List<Map<Integer, byte[]>> list = new ArrayList<Map<Integer, byte[]>>();
		
		try {
            int part = 0;
            while (part < memoryByteList.size()) {
                if (memoryByteList.get(part + 0) == Config.START
                        && memoryByteList.get(part + 2) == Config.SPLIT) {
                    byte[] bb = new byte[4];
                    bb[0] = memoryByteList.get(part + 3);
                    bb[1] = memoryByteList.get(part + 4);
                    bb[2] = memoryByteList.get(part + 5);
                    bb[3] = memoryByteList.get(part + 6);
                    int size = bytesToInt(bb);
                    if (memoryByteList.get(part + size + 3 + 4) == Config.START) {
                        Map<Integer, byte[]> map = new HashMap<Integer, byte[]>();
                        byte[] b = new byte[size];
                        for (int index = 0; index < b.length; index++) {
                            b[index] = memoryByteList.get(part + index + 3 + 4);
                        }
                        map.put((int) memoryByteList.get(part + 1), b);
                        list.add(map);
                    }
                    part = part + size + 4 + 4;
                }
            }
        } catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
        }
		
		return list;
	}

    public static void sendMsg(IoSession session, String input, int type) {
        if (!Config.offlineDebug) {
            IoBuffer answer = IoBuffer.allocate(stringToByteArray(type, input).length, false);
            answer.setAutoExpand(false);
            answer.setAutoShrink(true);
            answer.put(stringToByteArray(type, input));
            answer.flip();
            session.write(answer);
            answer.free();
        }
    }

}
