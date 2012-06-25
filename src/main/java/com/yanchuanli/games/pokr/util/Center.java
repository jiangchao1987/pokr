package com.yanchuanli.games.pokr.util;

import com.yanchuanli.games.pokr.model.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class Center {

    private static Center instance;
    private List<Room> rooms;


    static {
        instance = new Center();
    }

    public Center() {
        rooms = new ArrayList<Room>();
        rooms.add(new Room());
    }

    public static Center getInstance() {
        if (instance == null) {
            instance = new Center();
        }
        return instance;
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
