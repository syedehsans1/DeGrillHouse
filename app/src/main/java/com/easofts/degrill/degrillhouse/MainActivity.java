package com.easofts.degrill.degrillhouse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import org.w3c.dom.Text;

import java.lang.Object;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Firebase firebase;
    int totalbill=0;
    int x=0;
    private DBHelper saledb;
    ArrayList<menuitem> menuitems = new ArrayList<menuitem>();
    final ArrayList<sale> saleitems = new ArrayList<sale>();
    CheckBox type;
    CheckBox dtype;
    TextView total;
    int c=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://degrillhouse-48fc6.firebaseio.com");
        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        type=(CheckBox) findViewById(R.id.takeaway);
        dtype=(CheckBox) findViewById(R.id.delivery);
        total=(TextView)findViewById(R.id.total);
        final TextView oid=(TextView)findViewById(R.id.orderid);
        saledb=new DBHelper(this);
        int ids=0;
        if(saledb.getLast()>0) {
            ids = saledb.getLast();
        }
        ids+=1;
        oid.setText(Integer.toString(ids));
        // Construct the data source
// Create the adapter to convert the array to views
        final menuAdapter madapter = new menuAdapter(this, menuitems);
        final saleAdapter sadapter = new saleAdapter(this, saleitems);
// Attach the adapter to a ListView
        final ListView listView = (ListView) findViewById(R.id.menuitems);
        final ListView saletable = (ListView) findViewById(R.id.saletable);
        listView.setAdapter(madapter);
        saletable.setAdapter(sadapter);
        ArrayList<menuitem> items;
        menuitem newItem = new menuitem(1, "Thrillist",270);
        madapter.add(newItem);
        newItem = new menuitem(2, "Double Thrillist",370);
        madapter.add(newItem);
        newItem = new menuitem(3, "Beef Thrillist",330);
        madapter.add(newItem);
        newItem = new menuitem(4, "Double Beef Thrillist",480);
        madapter.add(newItem);
        newItem = new menuitem(5, "Dijon",300);
        madapter.add(newItem);
        newItem = new menuitem(6, "Double Dijon",400);
        madapter.add(newItem);
        newItem = new menuitem(7, "Mutton Burger",500);
        madapter.add(newItem);
        newItem = new menuitem(8, "Aftar Deal",550);
        madapter.add(newItem);
        newItem = new menuitem(31, "Thrillist Sandwich",260);
        madapter.add(newItem);
        newItem = new menuitem(32, "Dijon Sandwich",300);
        madapter.add(newItem);
        newItem = new menuitem(41, "Cheese Peppery",340);
        madapter.add(newItem);
        newItem = new menuitem(50, "Cold Drink",50);
        madapter.add(newItem);
        newItem = new menuitem(51, "Mint Margarita",120);
        madapter.add(newItem);
        newItem = new menuitem(52, "Water",40);
        madapter.add(newItem);
        newItem = new menuitem(53, "Plain Fries",100);
        madapter.add(newItem);
        newItem = new menuitem(54, "Curly Fries",200);
        madapter.add(newItem);
        newItem = new menuitem(55, "Beef Patty",150);
        madapter.add(newItem);
        newItem = new menuitem(56, "Chicken Fillet",120);
        madapter.add(newItem);
        newItem = new menuitem(57, "6 Drum Sticks",400);
        madapter.add(newItem);
        newItem = new menuitem(58, "12 Drum Sticks",750);
        madapter.add(newItem);
        newItem = new menuitem(59, "Chocolate Shake",180);
        madapter.add(newItem);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
            {
                x++;
                String y=madapter.getItem(position).getName();
                CheckBox cheese=(CheckBox)findViewById(R.id.cheese);
                CheckBox onion=(CheckBox)findViewById(R.id.onion);
                CheckBox tomato=(CheckBox)findViewById(R.id.tomato);
                CheckBox curly=(CheckBox)findViewById(R.id.curly);
                int p=madapter.getItem(position).getPrice();
                if(cheese.isChecked()) {
                    c=1;
                    p+=30;
                    y+=" with cheese ";
                }
                if(!onion.isChecked()) {
                    y+=" without onion ";
                }
                if(!tomato.isChecked()) {
                    y+=" without tomato ";
                }
                if(curly.isChecked()){
                    p+=50;
                    y+=" with curly fries ";
                }
                String i=(oid.getText().toString());
                String price=Integer.toString(p);
                int key=menuitems.get(position).getId();
                sale newsaleitem;
                TextView ordername=(TextView) findViewById(R.id.ordername);

                if(type.isChecked()) {
                    newsaleitem = new sale(Integer.parseInt(i), y, price, 1,key,ordername.getText().toString(),c);
                    c=0;
                }
                else if(dtype.isChecked()){
                    newsaleitem = new sale(Integer.parseInt(i), y, price, 2,key,ordername.getText().toString(),c);
                    c=0;
                }
                else{
                    newsaleitem = new sale(Integer.parseInt(i), y, price, 0,key,ordername.getText().toString(),c);
                    c=0;
                }
                //saleitems.add(newsaleitem);
                sadapter.add(newsaleitem);
                totalbill+=p;
                total.setText(Integer.toString(totalbill));
                Toast.makeText(getApplicationContext(),newsaleitem.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        saletable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                CheckBox c1=(CheckBox)findViewById(R.id.c1);
                if(c1.isChecked()) {
                    totalbill-=Integer.parseInt(sadapter.getItem(position).getPrice());
                    total.setText(Integer.toString(totalbill));
                    sadapter.remove(sadapter.getItem(position));
                }
            }
        });
        Button print=(Button) findViewById(R.id.print);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    saledb.insertsale(saleitems);
                for(int i=0;i<saleitems.size();i++) {
                    String Cid;
                    Cid = firebase.push().getKey();
                    firebase.child("Sales").child(Cid).setValue(saleitems.get(i));
                }
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        Button pending=(Button)findViewById(R.id.pending);
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Pending.class);
                startActivity(i);
            }
        });
    }
}
