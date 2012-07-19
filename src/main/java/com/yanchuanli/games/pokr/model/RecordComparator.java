package com.yanchuanli.games.pokr.model;

import java.util.Comparator;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-7-6
 */
public class RecordComparator implements Comparator<Record> {
    @Override
    public int compare(Record o1, Record o2) {
        return o1.getBet() < o2.getBet() ? -1 : 1;
    }
}
