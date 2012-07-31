package com.yanchuanli.games.pokr.model;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-7-31
 */
public enum EXPTimeLevel {

    LEVEL1(1, 0, 300, 5),
    LEVEL2(2, 300, 900, 10),
    LEVEL3(2, 900, 1800, 20),
    LEVEL4(4, 1800, 3600, 30),
    LEVEL5(5, 3600, 5400, 50),
    LEVEL6(6, 5400, 7200, 100),
    LEVEL7(7, 7200, 10800, 20),
    LEVEL8(8, 10800, 14400, 20),
    LEVEL9(9, 14400, 18000, 20),
    LEVEL10(10, 18000, 21600, 20),
    LEVEL11(11, 21600, 25200, 20),
    LEVEL12(12, 25200, 28800, 20),
    LEVEL13(13, 28800, 32400, 20),
    LEVEL14(14, 32400, 36000, 20),
    LEVEL15(15, 36000, 39600, 20),
    LEVEL16(16, 39600, 43200, 20),
    LEVEL17(17, 43200, 46800, 20),
    LEVEL18(18, 46800, 50400, 20),
    LEVEL19(19, 50400, 54000, 20),
    LEVEL20(20, 54000, 57600, 20),
    LEVEL21(21, 57600, 61200, 20),
    LEVEL22(22, 61200, 64800, 20),
    LEVEL23(23, 64800, 68400, 20),
    LEVEL24(24, 68400, 72000, 20),
    LEVEL25(25, 72000, 75600, 20),
    LEVEL26(26, 75600, 79200, 20),
    LEVEL27(27, 79200, 82800, 20),
    LEVEL28(28, 82800, 86400, 20);


    private int minTimeInSeconds;
    private int maxTimeInSeconds;
    private int exp;
    private int level;

    EXPTimeLevel(int level, int minTimeInSeconds, int maxTimeInSeconds, int exp) {
        this.level = level;
        this.minTimeInSeconds = minTimeInSeconds;
        this.maxTimeInSeconds = maxTimeInSeconds;
        this.exp = exp;
    }

    public int getMinTimeInSeconds() {
        return minTimeInSeconds;
    }

    public int getMaxTimeInSeconds() {
        return maxTimeInSeconds;
    }

    public int getExp() {
        return exp;
    }

    public int getLevel() {
        return level;
    }
}
