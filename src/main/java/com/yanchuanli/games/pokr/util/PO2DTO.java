package com.yanchuanli.games.pokr.util;

import com.yanchuanli.games.pokr.dto.PlayerDTO;
import com.yanchuanli.games.pokr.model.Player;

public class PO2DTO {

	public static PlayerDTO parsePlayer(Player player) {
		PlayerDTO playerDTO = new PlayerDTO();
		playerDTO.setUdid(player.getUdid());
		playerDTO.setName(player.getName());
		playerDTO.setMoneyInGame(player.getMoneyInGame());
		playerDTO.setCustomAvatar(player.getCustomAvatar());
		playerDTO.setAvatar(player.getAvatar());
		playerDTO.setSex(player.getSex());
		playerDTO.setAddress(player.getAddress());
		playerDTO.setSeatIndex(player.getSeatIndex());
		return playerDTO;
	}
	
}
