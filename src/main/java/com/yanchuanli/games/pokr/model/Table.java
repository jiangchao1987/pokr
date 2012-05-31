package com.yanchuanli.games.pokr.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class Table {
    private String id;
    private String name;
    private List<Player> players;

    public Table(String id, String name) {
        this.id = id;
        this.name = name;
        players = new ArrayList<Player>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
