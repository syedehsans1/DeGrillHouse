package com.easofts.degrill.degrillhouse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Pending extends AppCompatActivity{

    ListView pl;
    ListView pendingtotal;
    CheckBox c1;
    private int totalsales=0;
    private ArrayList<sale> pendingsales;
    private ArrayList<sale> Allsales;
    private DBHelper read;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        pl=(ListView) findViewById(R.id.pendinglist);
        pendingtotal=(ListView) findViewById(R.id.pendingtotal);
        c1=(CheckBox) findViewById(R.id.c1);
        read=new DBHelper(getApplicationContext());
        pendingsales=read.getAllPendingSales();
        Allsales=read.getAllSales();
        ArrayList <pendingtotal> pendingsalestotal=new ArrayList<pendingtotal>();
        if(pendingsales.size()>0){
            pendingsalestotal.add(new pendingtotal(pendingsales.get(0).getOrdername(),Integer.parseInt(pendingsales.get(0).getPrice())));
        }
        for(int j=0,i=1;i<pendingsales.size();i++){
            if(pendingsales.get(i).getOrdername().equals(pendingsalestotal.get(j).getName())){
                pendingsalestotal.get(j).addPrice(Integer.parseInt(pendingsales.get(i).getPrice()));
            }
            else{
                j++;
                pendingsalestotal.add(new pendingtotal(pendingsales.get(i).getOrdername(),Integer.parseInt(pendingsales.get(i).getPrice())));
            }
        }
        final pendingtotaladapter psadapter=new pendingtotaladapter(this,pendingsalestotal);
        final salependingAdapter sadapter=new salependingAdapter(this,pendingsales);
        pl.setAdapter(sadapter);
        pendingtotal.setAdapter(psadapter);
        pl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                if(c1.isChecked()) {
                    read.updateStatus(pendingsales.get(position));
                    Intent i=new Intent(getApplicationContext(),Pending.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        for(int i=0;i<Allsales.size();i++){
            totalsales+=Integer.parseInt(Allsales.get(i).getPrice());
        }
        TextView x=(TextView) findViewById(R.id.totalsale);
        x.setText(Integer.toString(totalsales));
        Button close=(Button) findViewById(R.id.closebutton);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                read.onUpgrade(read.getWritableDatabase(),1,2);
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
