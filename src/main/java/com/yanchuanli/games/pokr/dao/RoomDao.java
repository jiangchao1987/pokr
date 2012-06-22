package com.yanchuanli.games.pokr.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-6-22
 */
public class RoomDao {
    public static List<String> getRooms() {
        List rooms = new ArrayList();
        rooms.add("123");
        rooms.add("456");
        return rooms;
    }
}
