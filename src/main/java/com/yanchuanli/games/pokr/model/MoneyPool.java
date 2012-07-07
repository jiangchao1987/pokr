package com.yanchuanli.games.pokr.model;

import java.util.List;
import java.util.Map;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-20
 */
public class MoneyPool {

    private List<Pot> pots;
    private Pot currentpot;
    private Map<String, Integer> currentPot;
    private List<Record> currentAllInPlayers;
    private RecordComparator comparator;

    public MoneyPool() {

    }

    public void addRecord(Record record) {
//        pot.addRecord(record);
    }

    public void buildPotList(){

    }
}
