package com.yanchuanli.games.pokr.test;

import org.apache.log4j.Logger;

import java.util.Scanner;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class Test {

    private static Logger log = Logger.getLogger(Test.class);

    public static void main(String[] args) {
//        ServiceCenter.getInstance().processCommand("createroom");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        log.debug("input:" + input);
    }
}
