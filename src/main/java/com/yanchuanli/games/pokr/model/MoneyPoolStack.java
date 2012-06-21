package com.yanchuanli.games.pokr.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-20
 */
public class MoneyPoolStack {
    private List<MoneyPool> pools;

    public MoneyPoolStack() {
        this.pools = new ArrayList<>();
    }

    public void addPool(MoneyPool pool) {
        pools.add(pool);
    }

    public int getCurrentBet() {
        int result = 0;
        if (pools.size() > 0) {
            result = pools.get(pools.size() - 1).getMoney();
        }

        return result;
    }
}
