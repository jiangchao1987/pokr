package com.jiangchao.games.pokr.texas;

import java.util.List;
import java.util.Set;

/**
 * Note: 用于显示table信息和处理player行为的client, 
 * 		 player human or bot必须实现该接口
 * Author: JiangChao
 * Date: 2012/6/14/20
 * Email: chaojiang@candou.com
 */
public interface Client {

	void messageReceived(String message);
	
	/** 处理players加入一桌游戏。*/
	void joinedTable(int bigBlind, List<Player> players);
	
	/** 处理新的一局。*/
	void handStarted(Player dealer);
	
	/** 轮流切换发牌玩家。*/
	void actorRotated(Player actor);
	
	/** 更新player。*/
	void playerUpdated(Player player);
	
	void boardUpdated(List<Card> cards, int bet, int pot);
	
	/** 处理玩家执行事件。*/
	void playerActed(Player player);
	
	/** 执行命令操作。*/
	Action act(Set<Action> allowedActions);

}
