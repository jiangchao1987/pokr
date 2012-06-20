package com.jiangchao.games.pokr.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.google.protobuf.InvalidProtocolBufferException;
import com.yanchuanli.games.pokr.core.Card;
import com.yanchuanli.games.pokr.model.MiniRoomProtos.MiniRoom;
import com.yanchuanli.games.pokr.model.Player;

/**
 * Note: static utils 
 * Author: JiangChao 
 * Date: 2012/6/14/11 
 * Email: chaojiang@candou.com
 */
public class Util {
	private static Logger log = Logger.getLogger(Util.class);
	
	public static void sendToAll(String message) {
        if (!Config.offlineDebug) {
            for (String s : Memory.playersOnServer.keySet()) {
                Player player = Memory.playersOnServer.get(s);
                Util.sendMessage(player.getSession(), message);
            }
        }
    }

	public static void sendMessage(IoSession session, String message) {
		if (!Config.offlineDebug) {
			synchronized (session) {
				IoBuffer answer = IoBuffer.allocate(stringToByte(6, message).length,
						false);
				answer.put(stringToByte(6, message));
//				IoBuffer answer = IoBuffer.allocate(toByte(input).length, false);
//				answer.put(toByte(input));
				answer.flip();
				session.write(answer);
				answer.free();
			}
		}
	}

	public static List<String> fromByte1(IoBuffer buffer) {
		List<String> list = new ArrayList<String>();
		try {
			boolean flag = true;
			int part = 0;
			while (flag) {
				System.out.println("buffer.get(part + 0) = "
						+ buffer.get(part + 0));
				if (buffer.get(part + 0) == Config.HEAD
						&& buffer.get(part + 1) == Config.START
						&& buffer.get(part + 3) == Config.SPLIT) {
					byte[] bb = new byte[4];
					bb[0] = buffer.get(part + 4);
					bb[1] = buffer.get(part + 5);
					bb[2] = buffer.get(part + 6);
					bb[3] = buffer.get(part + 7);
					int size = bytesToInt(bb);
					if (buffer.get(part + size + 4 + 4) == Config.HEAD
							&& buffer.get(part + size + 5 + 4) == Config.START) {
						byte[] b = new byte[size];
						for (int index = 0; index < b.length; index++) {
							b[index] = buffer.get(part + index + 4 + 4);
						}
						System.out.println(new String(b));
						list.add(new String(b));
					}
					part = part + size + 6 + 4;
					System.out.println("part" + part);
				} else {
					flag = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static byte[] toByte1(int type, String input) {
		// byte[] b = null;
		// byte[] length = null;
		//
		// if (type == 6) { //变长
		// b = new byte[input.getBytes().length + 10];
		// length = intToByte(input.getBytes().length);
		// b[4] = length[0];
		// b[5] = length[1];
		// b[6] = length[2];
		// b[7] = length[3];
		// for (int index = 8; index < b.length - 2; index++) {
		// b[index] = input.getBytes()[index - 8];
		// }
		// } else { //定长
		// b = new byte[input.getBytes().length + 6];
		// for (int index = 4; index < b.length - 2; index++) {
		// b[index] = input.getBytes()[index - 4];
		// }
		// }
		// b[0] = Config.HEAD;
		// b[1] = Config.START;
		// b[2] = (byte) type;
		// b[3] = Config.SPLIT;
		//
		// b[b.length - 2] = (byte) Config.HEAD;
		// b[b.length - 1] = (byte) Config.START;

		byte[] b = { 35, 36, 6, 124, 3, 0, 0, 0, 97, 97, 97, 35, 36, 35, 36, 6,
				124, 4, 0, 0, 0, 97, 97, 97, 97, 35, 36, 35, 36, 6, 124, 2, 0,
				0, 0, 98, 98, 35, 36 };
		// byte[] b = {35, 36, 5, 124, 97, 35, 36, 35, 36, 5, 124, 97, 35, 36};
		return b;
	}
	
	public static List<String> byteToString(IoBuffer buffer) {
		List<String> list = new ArrayList<String>();
		try {
			boolean flag = true;
			int part = 0;
			while (flag) {
				if (buffer.get(part + 0) == Config.START
						&& buffer.get(part + 2) == Config.SPLIT) {
					byte[] bb = new byte[4];
					bb[0] = buffer.get(part + 3);
					bb[1] = buffer.get(part + 4);
					bb[2] = buffer.get(part + 5);
					bb[3] = buffer.get(part + 6);
					int size = bytesToInt(bb);
					if (buffer.get(part + size + 3 + 4) == Config.START) {
						byte[] b = new byte[size];
						for (int index = 0; index < b.length; index++) {
							b[index] = buffer.get(part + index + 3 + 4);
						}
						System.out.println(new String(b));
						list.add(new String(b));
					}
					part = part + size + 4 + 4;
				} else {
					flag = false;
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		log.info("list.size " + list.size());
		return list;
	}
	
	public static byte[] stringToByte(int type, String message) {
		byte[] b = null;
		byte[] length = null;

		b = new byte[message.getBytes().length + 8];
		length = intToByte(message.getBytes().length);
		b[3] = length[0];
		b[4] = length[1];
		b[5] = length[2];
		b[6] = length[3];
		for (int index = 7; index < b.length - 1; index++) {
			b[index] = message.getBytes()[index - 7];
		}
		b[0] = Config.START;
		b[1] = (byte) type;
		b[2] = Config.SPLIT;

		b[b.length - 1] = (byte) Config.START;
//		byte[] b = { 36, 6, 124, 3, 0, 0, 0, 97, 97, 97, 36, 36, 6,
//				124, 4, 0, 0, 0, 97, 97, 97, 97, 36, 36, 6, 124, 2, 0,
//				0, 0, 98, 98, 36 };
		return b;
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

	public static List<String> extractStringFromIoBuffer(IoBuffer buffer) {
		List<String> list = new ArrayList<String>();
		try {
			boolean flag = true;
			int part = 0;
			while (flag) {
				if (buffer.get(part + 0) == Config.START
						&& buffer.get(part + 2) == Config.SPLIT) {
					int size = (int) buffer.get(part + 1);
					if (buffer.get(part + size + 3) == Config.START) {
						byte[] b = new byte[size];
						for (int index = 0; index < b.length; index++) {
							b[index] = buffer.get(part + index + 3);
						}
						log.info(new String(b));
						list.add(new String(b));
					}
					part = part + size + 4;
				} else {
					flag = false;
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			log.error(e);
		}
		log.info("list.size " + list.size());
		return list;
	}

	// public static String extractStringFromIoBuffer(IoBuffer buffer) {
	// System.out.println(buffer.get(0) + "-" + buffer.get(2) + "-" + (int)
	// buffer.get(1) + "-" + buffer.get((int) buffer.get(1) + 3));
	// if (buffer.get(0) == Config.START && buffer.get(2) == Config.SPLIT
	// && buffer.get((int) buffer.get(1) + 3) == Config.START) {
	// byte[] b = new byte[(int) buffer.get(1)];
	// System.out.println(b.length);
	// for (int index = 0; index < b.length; index++) {
	// b[index] = buffer.get(index + 3);
	// }
	// return new String(b);
	// }
	// return null;
	// }

	public static byte[] toByte(String input) {
		byte[] b = new byte[input.getBytes().length + 4];
		b[0] = Config.START;
		b[1] = (byte) input.getBytes().length;
		b[2] = Config.SPLIT;
		b[b.length - 1] = (byte) Config.START;
		for (int index = 3; index < b.length - 1; index++) {
			b[index] = input.getBytes()[index - 3];
		}
		// byte[] b = {36, 3, 124, 97, 97, 97, 36};
		// byte[] b = { 36, 3, 124, 97, 97, 97, 36, 36, 4, 124, 97, 97, 97, 97,
		// 36 };
		return b;
	}

	public static String cardsToString(List<Card> cardList) {
		String result = "";
		for (Card card : cardList) {
			result += card.toChineseString() + " ";
		}
		return result;
	}

	public static byte[] getNewArray(byte[] a, byte[] b) {
		byte[] reustArray = new byte[a.length + b.length];
		int count = 0;
		for (int i = 0, j = 0; (i < a.length || j < b.length);) {
			if (i < a.length && j < b.length) {
				byte aTemp = a[i];
				byte bTemp = b[j];

				if (aTemp > bTemp) {
					reustArray[count++] = bTemp;
					j++;
				} else if (aTemp <= bTemp) {
					reustArray[count++] = aTemp;
					i++;
				}
			} else if (i < a.length) {
				reustArray[count++] = a[i];
				i++;
			} else {
				reustArray[count++] = b[j];
				j++;
			}
		}
		return reustArray;
	}
	
	//------------------------------google protocol buffers---------------------------------
	
	public static void sendMsg(IoSession session, byte[] byteArray) {
            IoBuffer answer = IoBuffer.allocate(
            		byteArray.length, false);
            answer.put(byteArray);
            answer.flip();
            session.write(answer);
            answer.free();
    }
	
	public static byte[] miniRoomToByteArray(int type, MiniRoom miniRoom) {
		return encodeByteArray(type, miniRoom.toByteArray());
	}
	
	public static List<MiniRoom> ioBufferToMiniRoom(IoBuffer buffer) {
		List<MiniRoom> list = new ArrayList<MiniRoom>();
		
		List<byte[]> byteArrayList = decodeByteArray(buffer);
		for (byte[] byteArray : byteArrayList) {
			try {
				list.add(MiniRoom.parseFrom(byteArray));
			} catch (InvalidProtocolBufferException e) {
				log.error(e);
			}
		}
		
		return list;
	}
	
	/** 给byteArray加上约定好的head信息。*/
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
		return byteArray;
	}
	
	/** 从接收到的数据中解析出内容byteArray的集合。*/
	private static List<byte[]> decodeByteArray(IoBuffer buffer) {
		List<byte[]> list = new ArrayList<byte[]>();
		
		try {
			boolean flag = true;
			int part = 0;
			while (flag) {
				if (buffer.get(part + 0) == Config.START
						&& buffer.get(part + 2) == Config.SPLIT) {
					byte[] bb = new byte[4];
					bb[0] = buffer.get(part + 3);
					bb[1] = buffer.get(part + 4);
					bb[2] = buffer.get(part + 5);
					bb[3] = buffer.get(part + 6);
					int size = bytesToInt(bb);
					if (buffer.get(part + size + 3 + 4) == Config.START) {
						byte[] b = new byte[size];
						for (int index = 0; index < b.length; index++) {
							b[index] = buffer.get(part + index + 3 + 4);
						}
						list.add(b);
					}
					part = part + size + 4 + 4;
				} else {
					flag = false;
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		
		return list;
	}

//	public static void main(String[] args) {
//		byte[] bb = stringToByte(5, "abcdef");
//		for (int i = 0; i < bb.length; i++) {
//			System.out.println(bb[i]);
//		}
//	}

}
