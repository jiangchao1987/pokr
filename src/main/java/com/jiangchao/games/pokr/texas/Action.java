package com.jiangchao.games.pokr.texas;

/**
 * Note: 所有支持的牌操作
 * Author: JiangChao
 * Date: 2012/6/14/21
 * Email: chaojiang@candou.com
 */
public enum Action {

	SMALL_BLIND("Small Blind", "posts the small blind"),
	BIG_BLIND("Big Blind", "posts the big blind"),
	CHECK("Check", "checks"),
	// 跟注
	CALL("Call", "calls"),
	// 下注
	BET("Bet", "bets"),
	// 加注
	RAISE("Raise", "raises"),
	// 弃牌
    FOLD("Fold", "folds"),
    CONTINUE("Continue", "continues"),
	;
	
	private final String name;
	private final String verb;
	
	Action(String name, String verb) {
		this.name = name;
		this.verb = verb;
	}
	
	public String getName() {
        return name;
    }
    
    public String getVerb() {
        return verb;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
}
