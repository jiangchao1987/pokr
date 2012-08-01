package com.yanchuanli.games.pokr.event;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-7-27
 */
public class Event {
    private String udid;
    private int timestamp;
    private int type;
    private String info;
    private int processed;
    
    public Event() {
    }

    public Event(String udid, int timestamp, int type, String info) {
        this.udid = udid;
        this.timestamp = timestamp;
        this.type = type;
        this.info = info;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
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

	public int getProcessed() {
		return processed;
	}

	public void setProcessed(int processed) {
		this.processed = processed;
	}
    
}
