package com.jiangchao.games.pokr.texas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

import com.jiangchao.games.pokr.texas.bots.DummyBot;

/**
 * Note: 控制台玩游戏入口
 * Author: JiangChao
 * Date: 2012/6/14/16
 * Email: chaojiang@candou.com
 */
public class ConsoleClient implements Client {
	
	private static final int BIG_BLIND = 2;
	private static final int STARTING_CASH = 100;
	private final BufferedReader consoleReader;

	public ConsoleClient() {
		consoleReader = new BufferedReader(new InputStreamReader(System.in));
		Table table = new Table(BIG_BLIND);
		table.addPlayer(new Player("JiangChao", STARTING_CASH, this));
		table.addPlayer(new Player("Player", STARTING_CASH, this));
        table.addPlayer(new Player("Joe",    STARTING_CASH, new DummyBot()));
        table.addPlayer(new Player("Mike",   STARTING_CASH, new DummyBot()));
        table.addPlayer(new Player("Eddie",  STARTING_CASH, new DummyBot()));
        table.start();
	}
	
	public static void main(String[] args) {
        new ConsoleClient();
    }

	@Override
	public void messageReceived(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void joinedTable(int bigBlind, List<Player> players) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handStarted(Player dealer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actorRotated(Player actor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerUpdated(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void boardUpdated(List<Card> cards, int bet, int pot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerActed(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Action act(Set<Action> allowedActions) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
