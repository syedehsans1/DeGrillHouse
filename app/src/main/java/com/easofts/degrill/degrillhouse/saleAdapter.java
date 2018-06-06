package com.easofts.degrill.degrillhouse;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Muhammad Ehsan on 4/22/2018.
 */


public class saleAdapter extends ArrayAdapter<sale> {
    public saleAdapter(Context context, ArrayList<sale> sale) {
        super(context, 0, sale);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        sale item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sales, parent,false);
        }
        // Lookup view for data population
        TextView id = (TextView) convertView.findViewById(R.id.itemid);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView price = (TextView) convertView.findViewById(R.id.price);

        // Populate the data into the template view using the data object
        id.setText(Integer.toString(item.getId()));
        name.setText(item.getName());
        price.setText(item.getPrice());
        if(item.getType()==0){
            name.setBackgroundColor(Color.parseColor("#ff6347"));
        }
        else if(item.getType()==1){
            name.setBackgroundColor(Color.parseColor("#f5de50"));
        }
        else{
            name.setBackgroundColor(Color.parseColor("#9932cc"));
        }
        // Return the completed view to render on screen
        return convertView;
    }
}