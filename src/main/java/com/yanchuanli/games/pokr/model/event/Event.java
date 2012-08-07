package com.yanchuanli.games.pokr.model.event;

import com.yanchuanli.games.pokr.util.TimeUtil;

public abstract class Event {

    private String udid;
    private int time;
    private int type;

    public Event() {
        time = TimeUtil.unixtime();
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public int getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
