package com.easofts.degrill.degrillhouse;

/**
 * Created by Muhammad Ehsan on 4/22/2018.
 */

public class menuitem {
    private int id;
    private String name;
    private int price;
    private boolean onion;
    private boolean cheese;
    private boolean tomato;

    public menuitem(int id, String name,int price) {
        this.name = name;
        this.id = id;
        this.price=price;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
    public void setCheese(boolean cheese) {
        this.cheese = cheese;
    }
    public void setOnion(boolean onion) {
        this.onion = onion;
    }
    public void setTomato(boolean tomato) {
        this.tomato = tomato;
    }
}