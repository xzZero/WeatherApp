package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ListviewAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> cities, avgTemp, temp;

    public ListviewAdapter(@NonNull Context context, ArrayList<String> cities, ArrayList<String> avgTemp, ArrayList<String> temp) {
        super(context, R.layout.mylist, cities);
        this.context = context;
        this.cities = cities;
        this.avgTemp = avgTemp;
        this.temp = temp;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
//        LayoutInflater inflater = context.getLayoutInflater();
//        View.inflate(context, layout, parent)
        View rowView=inflater.inflate(R.layout.mylist, null,true);

//        View rowView = View.inflate(context, R.layout.mylist, parent);

        TextView twCity = (TextView) rowView.findViewById(R.id.twCity);
        TextView twAvgTemp = (TextView) rowView.findViewById(R.id.twAvgTemp);
        TextView twTemp = (TextView) rowView.findViewById(R.id.twTemp);
//
        twCity.setText(nullFilter(cities.get(position)));
        twTemp.setText(nullFilter(temp.get(position)) + "°C");
        twAvgTemp.setText("Average: " + nullFilter(avgTemp.get(position)) + "°C");

//        twCity.setText(cities.get(position));
//        twTemp.setText(temp.get(position));
//        twAvgTemp.setText(avgTemp.get(position));


        return rowView;

    };

    String nullFilter(String s){
        if (s.isEmpty()){
            return "";
        }
        return s;
    }
}
