package com.yanchuanli.games.pokr.util;

import java.util.ArrayList;
import java.util.List;

import com.yanchuanli.games.pokr.model.MiniPlayerProtos.MiniPlayer;
import com.yanchuanli.games.pokr.model.MiniRoomProtos.MiniRoom;
import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.model.Room;

/**
 * Note: Translate POJO to GoogleProtoDTO 
 * Author: JiangChao 
 * Date: 2012/6/19/11 
 * Email: chaojiang@candou.com
 */
public class TranslationCenter {

	public MiniPlayer getMiniPlayer(Player player) {
		MiniPlayer miniPlayer = MiniPlayer.newBuilder().setId(player.getId())
				.setName(player.getName()).setMoney(player.getMoney())
				.setBet(player.getBet()).setInput(player.getInput()).build();
		return miniPlayer;
	}

	public MiniRoom getMiniRoom(Room room) {
		List<Player> players = room.getPlayers();
		List<MiniPlayer> miniPlayers = new ArrayList<MiniPlayer>();
		for (Player player : players) {
			miniPlayers.add(getMiniPlayer(player));
		}

		MiniRoom miniRoom = MiniRoom.newBuilder().setId(room.getId())
				.setName(room.getName()).addAllMiniPlayers(miniPlayers).build();
		return miniRoom;
	}

}
