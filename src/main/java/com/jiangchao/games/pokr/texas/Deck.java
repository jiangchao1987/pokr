package com.jiangchao.games.pokr.texas;

import java.util.Random;

/**
 * Note: 一副牌(无荷官)
 * Author: JiangChao
 * Date: 2012/6/14/21
 * Email: chaojiang@candou.com
 */
public class Deck {
	private static final int NO_OF_CARDS = Card.NO_OF_RANKS * Card.NO_OF_SUITS;
	private Card[] cards;
	private int nextCardIndex = 0;
	private Random random = new Random();

	public void shuffle() {
        for (int oldIndex = 0; oldIndex < NO_OF_CARDS; oldIndex++) {
            int newIndex = random.nextInt(NO_OF_CARDS);
            Card tempCard = cards[oldIndex];
            cards[oldIndex] = cards[newIndex];
            cards[newIndex] = tempCard;
        }
        nextCardIndex = 0;
    }
}
