package com.jiangchao.games.pokr.texas;

/**
 * Note: 一手牌
 * Author: JiangChao
 * Date: 2012/6/14/21
 * Email: chaojiang@candou.com
 */
public class Hand {
	
	// 一手牌中最多拥有的牌数
    private static final int MAX_NO_OF_CARDS = 7;
    // 这手牌中的所有牌
    private Card[] cards = new Card[MAX_NO_OF_CARDS];
    // 当前这手牌中还剩的牌数
    private int noOfCards = 0;
    
	public Hand() {
	}

	public Card[] getCards() {
        Card[] dest = new Card[noOfCards];
        System.arraycopy(cards, 0, dest, 0, noOfCards);
        return dest;
    }
	
	public void removeAllCards() {
        noOfCards = 0;
    }
}
