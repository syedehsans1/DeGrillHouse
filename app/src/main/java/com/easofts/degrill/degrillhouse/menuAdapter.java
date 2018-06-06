package com.easofts.degrill.degrillhouse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Muhammad Ehsan on 4/22/2018.
 */

public class menuAdapter extends ArrayAdapter<menuitem> {
    public menuAdapter(Context context, ArrayList<menuitem> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        menuitem item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu, parent, false);
        }
        // Lookup view for data population
        TextView Name = (TextView) convertView.findViewById(R.id.itemName);
        // Populate the data into the template view using the data object
        Name.setText(item.getName());
        // Return the completed view to render on screen
        return convertView;
    }
}