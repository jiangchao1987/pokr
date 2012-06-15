package com.jiangchao.games.pokr.game;

import com.yanchuanli.games.pokr.model.Player;
import com.jiangchao.games.pokr.util.Memory;

public class StartGame {

	public void init() {
		Game game = new Game();
		
		for (Player player : Memory.playersOnServer.keySet()) {
			game.addPlayer(player);
		}
		
		game.start();
	}
	
}
