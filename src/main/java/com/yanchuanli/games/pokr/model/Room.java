package com.yanchuanli.games.pokr.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Candou.com
 * Author: Yanchuan Li
 * Email: mail@yanchuanli.com
 * Date: 12-5-31
 */
public class Room {

    private String id;
    private String name;
    private List<Table> tables;
    private int numOfTables = 4;

    public Room(String id, String name) {
        this.id = id;
        this.name = name;
        tables = new ArrayList<Table>();
        for (int i = 0; i < numOfTables; i++) {
            tables.add(new Table(id, name + i));
        }
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

    public List<Table> getTables() {
        return tables;
    }


}
