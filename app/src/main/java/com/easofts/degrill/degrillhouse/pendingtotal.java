package com.easofts.degrill.degrillhouse;

/**
 * Created by Muhammad Ehsan on 4/22/2018.
 */


public class pendingtotal {
    private int id;
    private String name;
    private int price;

    public pendingtotal(int id,String name, int price) {
        this.name = name;
        this.id=id;
        this.price=price;
    }
    public pendingtotal(int id, String name) {
        this.id=id;
        this.name = name;
        this.price=0;
    }
    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
    public void addPrice(int value) {
        price+=value;
    }

    public int getId() {
        return id;
    }
}