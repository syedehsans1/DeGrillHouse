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


public class pendingtotaladapter extends ArrayAdapter<pendingtotal> {
    public pendingtotaladapter(Context context, ArrayList<pendingtotal> sale) {
        super(context, 0, sale);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        pendingtotal item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pendingtotal, parent,false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.ordern);
        TextView price = (TextView) convertView.findViewById(R.id.ordertotal);

        // Populate the data into the template view using the data object
        name.setText(item.getName());
        price.setText(Integer.toString(item.getPrice()));

        // Return the completed view to render on screen
        return convertView;
    }
}