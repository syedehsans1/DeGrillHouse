package com.easofts.degrill.degrillhouse;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Muhammad Ehsan on 4/22/2018.
 */


public class sale {
    private int id;
    private int itemkey;
    private String name;
    private String price;
    private int type;
    private int cheese;
    private String ordername;
    private String salesdate;

    public sale(int id, String detail,String price,int type,int itemkey,String Ordername,int cheese) {
        this.name = detail;
        this.id = id;
        this.cheese=cheese;
        this.price=price;
        this.type=type;
        this.itemkey=itemkey;
        ordername=Ordername;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final String formattedDate = df.format(c.getTime());
        salesdate=formattedDate;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPrice() {
        return price;
    }
    public int getType() {
        return type;
    }
    public int getItemkey() {return itemkey;}
    public String getsalesdate() {return salesdate;}
    public String getOrdername() {
        return ordername;
    }
    public int getCheese() {
        return cheese;
    }
}