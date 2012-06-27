package com.jiangchao.games.pokr.texas;

import java.util.ArrayList;
import java.util.List;

/**
 * Note: 完整的一次游戏
 * Author: JiangChao
 * Date: 2012/6/14/21
 * Email: chaojiang@candou.com
 */
public class Table {
	
	/** 每个玩家每次下注或加注的最大值。*/
	private static final int MAX_RAISES = 4;
	private final int bigBlind;
	private final List<Player> players;
	private final List<Player> activePlayers;
	/** 当前这副牌。*/
	private final Deck deck;
	private final List<Card> board;
	/** 当前发牌者所在位置。*/
	private int dealerPosition;
	/** 当前发牌者。*/
	private Player dealer;
	/** 当前出牌者所在位置。*/
	private int actorPosition;
	/** 当前出牌者。*/
	private Player actor;
	/** 当前这局的最小注。*/
	private int minBet;
	/** 当前这局目前的注。*/
	private int bet;
	/** 当前奖池里的钱。*/
	private int pot;
	private boolean gameOver;

	public Table(int bigBlind) {
		this.bigBlind = bigBlind;
		players = new ArrayList<Player>();
		activePlayers = new ArrayList<Player>();
		deck = new Deck();
		board = new ArrayList<Card>();
	}
	
	public void addPlayer(Player player) {
        players.add(player);
    }
	
	/**
	 * 游戏主入口。
	 */
	public void start() {
		resetGame();
		while (!gameOver) {
			playHand();
		}
		notifyMessage("Game over.");
	}
	
	private void resetGame() {
		dealerPosition = -1;
		actorPosition = -1;
		gameOver = false;
		for (Player player : players) {
			player.getClient().joinedTable(bigBlind, players);
		}
	}
	
	/**
	 * 玩一局牌。
	 */
	private void playHand() {
		resetHand();
		
		// Small blind
	}
	
	private void resetHand() {
		board.clear();
		bet = 0;
		pot = 0;
		notifyBoardUpdated();
		activePlayers.clear();
		for (Player player : players) {
			player.resetHand();
			if (!player.isBroke()) {
				activePlayers.add(player);
			}
		}
		dealerPosition = (dealerPosition + 1) % players.size();
		dealer = players.get(dealerPosition);
		deck.shuffle();	// 洗牌
		actorPosition = dealerPosition;
		minBet = bigBlind;	// 最小的注等于大盲注
		bet = minBet;	// 当前注等于初始注
		for (Player player : players) {
			player.getClient().handStarted(dealer);
		}
		notifyPlayersUpdated(false);
        notifyMessage("New hand, %s is the notifyCurrentDealer.", dealer);
	}
	
	/**
	 * 通知监听者。
	 */
	private void notifyMessage(String message, Object... args) {
		
	}
	
	private void notifyBoardUpdated() {
        for (Player player : players) {
            player.getClient().boardUpdated(board, bet, pot);
        }
    }
	
	/**
	 * 通知Clients一个或者多个players已经被更新了。
	 * 
	 * 一个玩家的私有信心只会被发到他自己的Client哪里; 
	 * 其他Client只能看到这个玩家的公共信息。
	 */
	private void notifyPlayersUpdated(boolean showdown) {
		for (Player playerToNotify : players) {
			for (Player player : players) {
				if (!showdown && player.equals(playerToNotify)) {
					// 隐藏私有信息
					player = player.publicClone();
				}
				playerToNotify.getClient().playerUpdated(player);
			}
		}
	}

}
