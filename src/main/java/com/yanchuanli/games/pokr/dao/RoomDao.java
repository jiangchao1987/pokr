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

    public static void init() {

    }

    public static List<Integer> getRooms(int roomlevel) {
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

        List<Integer> rooms = new ArrayList<Integer>();
		rooms.add(1001);
		rooms.add(1002);
		return rooms;
    }
}
