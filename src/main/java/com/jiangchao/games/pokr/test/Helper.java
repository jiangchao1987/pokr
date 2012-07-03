package com.jiangchao.games.pokr.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.jiangchao.games.pokr.util.Memory;
import com.yanchuanli.games.pokr.util.Config;

public class Helper {

//	private static List<Byte> memoryByteList = new ArrayList<Byte>();
	private static Logger log = Logger.getLogger(Helper.class);
	private static Map<Long, List<Byte>> memoryByteMap = new HashMap<Long, List<Byte>>();


	public static void sendToAllUser(String message, int type) {
		for (String s : Memory.sessionsOnServer.keySet()) {
			sendToOneUser(Memory.sessionsOnServer.get(s), message, type);
		}
	}

	public static void sendToOneUser(IoSession session, String input, int type) {
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

	public static byte[] stringToByteArray(int type, String input) {
		return encodeByteArray(type, input.getBytes());
	}

	private static byte[] encodeByteArray(int type, byte[] byteArray) {
//		byte[] b;
		/*byte[] b = null;
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

		b[b.length - 1] = (byte) Config.START;*/
		// log.info("encodeByteArray: " + new String(b));
		if (type == 0) {
			byte[] b = {36, 0, 124, 2, 0, 0, 0, 97};
			return b;
		}
		if (type == 1) {
			byte[] b = {97, 36, 36, 1, 124, 3, 0, 0, 0, 98, 98, 98, 36, 36, 2, 124};
			return b;
		}
		if (type == 2) {
			byte[] b = {2, 0, 0, 0, 99, 99, 36};
			return b;
		}
		return null;
	}

	public static byte[] intToByte(int i) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (0xff & i);
		bytes[1] = (byte) ((0xff00 & i) >> 8);
		bytes[2] = (byte) ((0xff0000 & i) >> 16);
		bytes[3] = (byte) ((0xff000000 & i) >> 24);
		return bytes;
	}

	public static List<Map<Integer, String>> ioBufferToString(long sessionId, IoBuffer buffer) {
		List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();

		List<Map<Integer, byte[]>> byteArrayList = decodeByteArray(sessionId, buffer);
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
	 * 从接收到的数据中解析出内容byteArray的Map。
	 */
	private static List<Map<Integer, byte[]>> decodeByteArray(long sessionId, IoBuffer buffer) {
		log.debug("sessionId: " + sessionId);
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
		memoryByteList.addAll(tempByteList);	//将缓存集合更新至最新
		
		if (tempByteList.get(tempByteList.size() - 1) == Config.START) {
//			show(memoryByteList);
			list = generateByteList(memoryByteList);	//将最新的缓存集合转换成合法格式
			memoryByteList.clear();
		}
		memoryByteMap.put(sessionId, memoryByteList);	//更新map中的缓存集合, 要么清空要么继续添加

//		if (tempByteList.get(0) != Config.START
//				|| tempByteList.get(tempByteList.size() - 1) != Config.START) {
//			memoryByteList.addAll(tempByteList);
//			return list;
//		}
//		if (counter % 2 == 1) {
//			memoryByteList.addAll(tempByteList);
//			return list;
//		}

		return list;
	}
	
	private static List<Map<Integer, byte[]>> generateByteList(List<Byte> memoryByteList) {
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
			e.printStackTrace();
        }
		
		return list;
	}

	private static void show(List<Byte> byteList) {
		int counter = 0;
		for (Byte b : byteList) {
			System.out.println(counter++ + "->" + b);
		}
	}

	public static int bytesToInt(byte[] bytes) {
		int length = bytes[0] & 0xFF;
		length |= ((bytes[1] << 8) & 0xFF00);
		length |= ((bytes[2] << 16) & 0xFF0000);
		length |= ((bytes[3] << 24) & 0xFF000000);
		return length;
	}

}
