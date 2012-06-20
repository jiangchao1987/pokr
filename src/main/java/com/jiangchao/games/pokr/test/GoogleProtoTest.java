package com.jiangchao.games.pokr.test;

import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.yanchuanli.games.pokr.model.MiniPlayerProtos.MiniPlayer;
import com.yanchuanli.games.pokr.model.MiniRoomProtos.MiniRoom;

public class GoogleProtoTest {

	public static void main(String[] args) throws InvalidProtocolBufferException {
		MiniRoom miniRoom = MiniRoom.parseFrom(getData());
		System.out.println(miniRoom);
	}
	
	private static byte[] getData() {
		List<MiniPlayer> miniPlayers = new ArrayList<MiniPlayer>();
		MiniPlayer miniPlayer1 = MiniPlayer.newBuilder().setId("1000")
				.setName("player-1000").setMoney(1000).setBet(200)
				.setInput("c").build();
		MiniPlayer miniPlayer2 = MiniPlayer.newBuilder().setId("1001")
				.setName("player-1001").setMoney(1001).setBet(200)
				.setInput("c").build();
		MiniPlayer miniPlayer3 = MiniPlayer.newBuilder().setId("1002")
				.setName("player-1002").setMoney(1002).setBet(200)
				.setInput("c").build();
		miniPlayers.add(miniPlayer1);
		miniPlayers.add(miniPlayer2);
		miniPlayers.add(miniPlayer3);
		
		MiniRoom miniRoom = MiniRoom.newBuilder().setId("1").setName("room-1")
				.addAllMiniPlayers(miniPlayers).build();
		byte[] data = miniRoom.toByteArray();
		return data;
	}

}
