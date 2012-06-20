package com.jiangchao.games.pokr.game;

import org.apache.log4j.Logger;

import com.jiangchao.games.pokr.util.Memory;

/**
 * Note: Start Game
 * Author: JiangChao 
 * Date: 2012/6/15/13 
 * Email: chaojiang@candou.com
 */
public class StartGame {
	private Logger log = Logger.getLogger(StartGame.class);
	
	public void init() {
		Game game = new Game();

        for (String s : Memory.playersOnServer.keySet()) {
            game.addPlayer(Memory.playersOnServer.get(s));
        }
        
        log.debug(Memory.playersOnServer.keySet().size() + " players joined ...");
        
        game.start();
	}
	
}
