package com.yanchuanli.games.pokr.test;

import com.yanchuanli.games.pokr.util.ServiceCenter;
import org.apache.log4j.Logger;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class Test {

    private static Logger log = Logger.getLogger(Test.class);

    public static void main(String[] args) {
        ServiceCenter.getInstance().processCommand("createroom");
    }
}
