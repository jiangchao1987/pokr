package com.yanchuanli.games.pokr.util;

import com.yanchuanli.games.pokr.basic.Card;
import com.yanchuanli.games.pokr.dao.PlayerDao;
import com.yanchuanli.games.pokr.model.Player;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright Candou.com Author: Yanchuan Li Email: mail@yanchuanli.com Date:
 * 12-5-31
 */
public class Util {

    private static Logger log = Logger.getLogger(Util.class);
    private static Map<Long, List<Byte>> memoryByteMap = new HashMap<Long, List<Byte>>();

    public static void disconnectUser(IoSession session) {
        Player player = Memory.sessionsOnServer.get(String.valueOf(session.getId()));
        if (player.getRoomid() != Integer.MIN_VALUE) {
            log.debug(player.getName() + " is leaving room " + player.getRoomid());
            String info = player.getRoomid() + "," + player.getUdid() + "," + player.getName();
            ServiceCenter.getInstance().leaveRoom(session, info);
        }
        player.setOnline(false);
        player.setSession(null);

        Memory.sessionsOnServer.remove(String.valueOf(session.getId()));
        Memory.playersOnServer.remove(player.getUdid());
        PlayerDao.updateOnlineStatus(player);
        log.info(player.getUdid() + ":" + player.getName() + " is now disconnected !");
        session.close(true);
    }

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

    /**
     * 编码 。
     */
    public static byte[] stringToByteArray(int type, String input) {
        return encodeByteArray(type, input.getBytes());
    }

    /**
     * 解码。
     */
    public static List<Map<Integer, String>> ioBufferToString(long sessionId,
                                                              IoBuffer buffer) {
        List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();

        List<Map<Integer, byte[]>> byteArrayList = decodeByteArray(sessionId,
                buffer);
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
        return b;
    }

    /**
     * 从接收到的数据中解析出内容byteArray的Map。
     */
    private static List<Map<Integer, byte[]>> decodeByteArray(long sessionId,
                                                              IoBuffer buffer) {
        List<Map<Integer, byte[]>> list = new ArrayList<Map<Integer, byte[]>>();

        List<Byte> tempByteList = new ArrayList<Byte>();
        while (buffer.hasRemaining()) {
            byte tempByte = buffer.get();
            tempByteList.add(tempByte);
        }

        List<Byte> memoryByteList = memoryByteMap.get(sessionId);
        if (memoryByteList == null) {
            memoryByteList = new ArrayList<Byte>();
        }
        memoryByteList.addAll(tempByteList);

        if (tempByteList.get(tempByteList.size() - 1) == Config.START) {
            list = generateByteList(memoryByteList);
            memoryByteList.clear();
        }
        memoryByteMap.put(sessionId, memoryByteList);

        return list;
    }

    private static List<Map<Integer, byte[]>> generateByteList(
            List<Byte> memoryByteList) {
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
            IoBuffer answer = IoBuffer.allocate(
                    stringToByteArray(type, input).length, false);
            answer.setAutoExpand(false);
            answer.setAutoShrink(true);
            answer.put(stringToByteArray(type, input));
            answer.flip();
            session.write(answer);
            answer.free();
        }
    }

}
