package com.easofts.degrill.degrillhouse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Muhammad Ehsan on 4/22/2018.
 */

public class DBHelper extends SQLiteOpenHelper {
    Firebase firebase;

    public DBHelper(Context context){
        super(context,"SalesFile.db",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table sales " +
                        "(id integer primary key, orderid integer,orderdetails text,price text,timeofsale text,type integer,status integer,itemKey text,ordername text)"
        );
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS sales");
        onCreate(db);
    }
    public boolean insertsale (final ArrayList<sale> sales) {
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formattedDate = df.format(c.getTime());
        int i=0;
        while(i<sales.size()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("orderid", sales.get(i).getId());
            contentValues.put("orderdetails", sales.get(i).getName());
            contentValues.put("price", sales.get(i).getPrice());
            contentValues.put("itemKey", sales.get(i).getItemkey());
            contentValues.put("ordername", sales.get(i).getOrdername());
            contentValues.put("timeofsale", formattedDate);
            contentValues.put("status", 0);
            contentValues.put("type", sales.get(i).getType());
            db.insert("sales", null, contentValues);
/*            URL url = null;
            try {
                url = new URL("http://www.degrillhouse.com/saleinsertapi.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            final HttpURLConnection[] urlConnection = {null};
            final URL finalUrl = url;
            final int finalI = i;
            AsyncTask task=new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    try {
                        urlConnection[0] = (HttpURLConnection) finalUrl.openConnection();
                        urlConnection[0].setRequestMethod("POST");
                        // Create the data
                        String Salesid = "salesid="+Integer.toString(sales.get(finalI).getId());
                        String Itemname= "itemname="+sales.get(finalI).getName();
                        String Timeofsale="timeofsale="+formattedDate;
                        String Price="price="+sales.get(finalI).getPrice();
                        urlConnection[0].setDoOutput(true);
                        urlConnection[0].getOutputStream().write(Salesid.getBytes());
                        urlConnection[0].getOutputStream().write(Itemname.getBytes());
                        urlConnection[0].getOutputStream().write(Timeofsale.getBytes());
                        urlConnection[0].getOutputStream().write(Price.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        urlConnection[0].setDoOutput(true);
                        urlConnection[0].setChunkedStreamingMode(0);

                    } catch(Exception e){
                        urlConnection[0].disconnect();
                    }
                    return null;
                }
            };
            task.execute();*/
            i++;
        }
        return true;
    }
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from sales where orderid="+id+"", null );
        return res;
    }

    public int getLast() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT orderid m FROM sales ORDER BY orderid DESC LIMIT 1", null );
        res.moveToFirst();
        if(res.getCount()==0){
            return 0;
        }
        return res.getInt(res.getColumnIndex("m"));
    }
    public void updateStatus(sale s) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL( "update sales set status=1 where orderid='"+s.getId()+"' and orderdetails='"+s.getName()+"' and price ='"+s.getPrice()+"'");
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "sales");
        return numRows;
    }
    public ArrayList<sale> getAllPendingSales() {
        ArrayList<sale> sales = new ArrayList<sale>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from sales where status=0", null );
        res.moveToFirst();
        int x=0;
        while(res.isAfterLast() == false){
            int id=res.getInt(res.getColumnIndex("orderid"));
            String details=res.getString(res.getColumnIndex("orderdetails"));
            String price=res.getString(res.getColumnIndex("price"));
            int type=res.getInt(res.getColumnIndex("type"));
            int key=res.getInt(res.getColumnIndex("itemKey"));
            String ordername=res.getString(res.getColumnIndex("ordername"));
            sales.add(new sale(id,details,price,type,key,ordername,0));
            res.moveToNext();
        }
        return sales;
    }
    public ArrayList<sale> getAllSales() {
        ArrayList<sale> sales = new ArrayList<sale>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from sales where 1", null );
        res.moveToFirst();
        int x=0;
        while(res.isAfterLast() == false){
            int id=res.getInt(res.getColumnIndex("orderid"));
            String details=res.getString(res.getColumnIndex("orderdetails"));
            String price=res.getString(res.getColumnIndex("price"));
            int type=res.getInt(res.getColumnIndex("type"));
            int key=res.getInt(res.getColumnIndex("itemKey"));
            String ordername=res.getString(res.getColumnIndex("ordername"));
            sales.add(new sale(id,details,price,type,key,ordername,0));
            res.moveToNext();
        }
        return sales;
    }
}