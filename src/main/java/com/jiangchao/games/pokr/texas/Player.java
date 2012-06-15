package com.jiangchao.games.pokr.texas;

/**
 * Note: 一位玩家
 * Author: JiangChao
 * Date: 2012/6/14/21
 * Email: chaojiang@candou.com
 */
public class Player {
	
    private final String name;
    private final Client client;
    private final Hand hand;
    /** 当前玩家手上的现金。*/
    private int cash;
    private boolean hasCards;
    private int bet;
    private int raises;
    private int allInPot;
    private Action action;
    private int betIncrement;

	public Player(String name, int cash, Client client) {
		this.name = name;
		this.cash = cash;
		this.client = client;
		
		hand = new Hand();
		
		resetHand();
	}
	
	public Client getClient() {
        return client;
    }

	public void resetHand() {
		hand.removeAllCards();
		hasCards = false;
		resetBet();
	}
	
	public void resetBet() {
        bet = 0;
        action = null;
        raises = 0;
        allInPot = 0;
        betIncrement = 0;
    }
	
	public Card[] getCards() {
        return hand.getCards();
    }
	
	public boolean isBroke() {
        return (cash == 0);
    }
	
	public Player publicClone() {
		Player clone = new Player(name, cash, null);
		clone.hasCards = hasCards;
		clone.bet = bet;
        clone.raises = raises;
        clone.action = action;
        return clone;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
