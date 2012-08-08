package com.yanchuanli.games.pokr.messagequeue;

import com.yanchuanli.games.pokr.util.TimeUtil;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-7-27
 */
public class MQEvent {

    private int timestamp;
    private int type;
    private String info;

    public MQEvent(int type, String info) {
        this.timestamp = TimeUtil.unixtime();
        this.type = type;
        this.info = info;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
