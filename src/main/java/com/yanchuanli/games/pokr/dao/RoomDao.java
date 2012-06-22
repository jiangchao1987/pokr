package com.yanchuanli.games.pokr.dao;

import com.yanchuanli.games.pokr.util.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-22
 */
public class RoomDao {
    public static List<String> getRooms(int roomlevel) {
        switch (roomlevel) {
            case Config.ROOM_LEVEL_BEGINNER:
                break;
            case Config.ROOM_LEVEL_PROFESSIONAL:
                break;
            case Config.ROOM_LEVEL_MASTER:
                break;
            case Config.ROOM_LEVEL_VIP:
                break;
        }

        List rooms = new ArrayList();
        rooms.add("1");
        rooms.add("2");
        return rooms;
    }
}
