package com.yanchuanli.games.pokr.test;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Random;

/**
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 10/17/12
 */
public class TestFileOps {
    private static Logger log = Logger.getLogger(TestFileOps.class);

    public static void main(String[] args) throws IOException {
        String[] acc = {"0", "1", "2"};
        Random ran = new Random();
        for (int i = 0; i < 10; i++) {
            log.debug(acc[ran.nextInt(acc.length)]);
        }

    }
}
