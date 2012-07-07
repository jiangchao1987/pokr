package com.yanchuanli.games.pokr.test;

import com.yanchuanli.games.pokr.model.Player;
import com.yanchuanli.games.pokr.model.Pot;
import com.yanchuanli.games.pokr.model.Record;
import com.yanchuanli.games.pokr.util.Config;
import org.apache.log4j.Logger;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class Test {

    private static Logger log = Logger.getLogger(Test.class);
    private static List<Player> players = new ArrayList<>();
    private static int actorPosition = 1;
    private static Player actor;

    public static void main(String[] args) throws UnknownHostException {

        Record record1 = new Record("a", Config.ACTION_TYPE_BET, 25);
        Record record2 = new Record("b", Config.ACTION_TYPE_ALL_IN, 12);
        Record record3 = new Record("c", Config.ACTION_TYPE_ALL_IN, 20);
        Record record4 = new Record("d", Config.ACTION_TYPE_CALL, 25);
        Record record5 = new Record("e", Config.ACTION_TYPE_RAISE, 30);
        Record record6 = new Record("a", Config.ACTION_TYPE_ALL_IN, 10);
        Record record7 = new Record("d", Config.ACTION_TYPE_CALL, 10);
        Record record9 = new Record("e", Config.ACTION_TYPE_FOLD, 0);

        Pot pot = new Pot();
        pot.addRecord(record1);
        pot.addRecord(record2);
        pot.addRecord(record3);
        pot.addRecord(record4);
        pot.addRecord(record5);
        pot.addRecord(record6);
        pot.addRecord(record7);
     //   pot.addRecord(record8);
        pot.addRecord(record9);
        pot.buildPotList();
        pot.finish();
    }

}
