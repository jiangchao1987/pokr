package com.jiangchao.games.pokr.texas.bots;

import java.util.List;
import java.util.Set;

import com.jiangchao.games.pokr.texas.Action;
import com.jiangchao.games.pokr.texas.Card;
import com.jiangchao.games.pokr.texas.Player;

/**
 * Note: 电脑玩家
 * Author: JiangChao
 * Date: 2012/6/14/21
 * Email: chaojiang@candou.com
 */
public class DummyBot extends Bot {

	@Override
	public void messageReceived(String message) {
	}

	@Override
	public void joinedTable(int bigBlind, List<Player> players) {
	}

	@Override
	public void handStarted(Player dealer) {
	}

	@Override
	public void actorRotated(Player actor) {
	}

	@Override
	public void playerUpdated(Player player) {
	}

	@Override
	public void boardUpdated(List<Card> cards, int bet, int pot) {
	}

	@Override
	public void playerActed(Player player) {
	}

	@Override
	public Action act(Set<Action> actions) {
		if (actions.contains(Action.CHECK)) {
			return Action.CHECK;
		} else {
			return Action.CALL;
		}
	}

}
