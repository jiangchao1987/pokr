package com.jiangchao.games.pokr.texas;

/**
 * Note: static utils
 * Author: JiangChao
 * Date: 2012/6/14/21
 * Email: chaojiang@candou.com
 */
public class Card implements Comparable<Card> {
	
	/** The number of ranks in a deck. */
    public static final int NO_OF_RANKS = 13;
    
    /** The number of suits in a deck. */
    public static final int NO_OF_SUITS = 4;
    
    // The ranks.
    public static final int ACE      = 12;
    public static final int KING     = 11;
    public static final int QUEEN    = 10;
    public static final int JACK     = 9;
    public static final int TEN      = 8;
    public static final int NINE     = 7;
    public static final int EIGHT    = 6;
    public static final int SEVEN    = 5;
    public static final int SIX      = 4;
    public static final int FIVE     = 3;
    public static final int FOUR     = 2;
    public static final int THREE    = 1;
    public static final int DEUCE    = 0;
    
    // The suits.
    public static final int SPADES   = 3;	//黑桃
    public static final int HEARTS   = 2;	//红心
    public static final int CLUBS    = 1;	//梅花
    public static final int DIAMONDS = 0;	//方片
    
    /** The rank symbols. */
    public static final String[] RANK_SYMBOLS = {
        "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"
    };
    
    /** The suit symbols. */
    public static final char[] SUIT_SYMBOLS = { 'd', 'c', 'h', 's' };

    /** The rank. */
    private final int rank;
    
    /** The suit. */
    private final int suit;
    
    /**
     * Constructor based on rank and suit.
     * 
     * @param rank
     *            The rank.
     * @param suit
     *            The suit.
     * 
     * @throws IllegalArgumentException
     *             If the rank or suit is invalid.
     */
    public Card(int rank, int suit) {
        if (rank < 0 || rank > NO_OF_RANKS - 1) {
            throw new IllegalArgumentException("Invalid rank");
        }
        if (suit < 0 || suit > NO_OF_SUITS - 1) {
            throw new IllegalArgumentException("Invalid suit");
        }
        this.rank = rank;
        this.suit = suit;
    }

	@Override
	public int compareTo(Card arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
