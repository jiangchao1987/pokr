package com.yanchuanli.games.pokr.util;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-8-2
 */
public class Level {
    public static int getLevel(int exp) {
        int result = 1;
        if (exp >= 0 && exp < 5) {
            result = 1;
        } else if (exp >= 5 && exp < 15) {
            result = 2;
        } else if (exp >= 15 && exp < 50) {
            result = 3;
        } else if (exp >= 50 && exp < 120) {
            result = 4;
        } else if (exp >= 120 && exp < 300) {
            result = 5;
        } else if (exp >= 300 && exp < 700) {
            result = 6;
        } else if (exp >= 700 && exp < 1800) {
            result = 7;
        } else if (exp >= 1800 && exp < 4000) {
            result = 8;
        } else if (exp >= 4000 && exp < 7500) {
            result = 9;
        } else if (exp >= 7500 && exp < 13000) {
            result = 10;
        } else if (exp >= 13000 && exp < 20000) {
            result = 11;
        } else if (exp >= 20000 && exp < 30000) {
            result = 12;
        } else if (exp >= 30000 && exp < 55000) {
            result = 13;
        } else if (exp >= 55000 && exp < 77000) {
            result = 14;
        } else if (exp >= 77000 && exp < 107000) {
            result = 15;
        } else if (exp >= 107000 && exp < 150000) {
            result = 16;
        } else if (exp >= 150000 && exp < 200000) {
            result = 17;
        } else if (exp >= 200000 && exp < 880000) {
            result = 18;
        } else {
            result = 19;
        }
        return result;
    }
}
