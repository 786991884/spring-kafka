package com.test;

import java.io.Serializable;

/**
 * @author Lenovo
 * @date 2016-08-26
 * @modify
 * @copyright
 */
public class Item implements Serializable {

    public Item() {
    }

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
