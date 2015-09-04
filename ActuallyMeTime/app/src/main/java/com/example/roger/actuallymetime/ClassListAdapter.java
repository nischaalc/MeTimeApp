package com.example.roger.actuallymetime;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Roger on 9/3/2015.
 */
public class ClassListAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final ArrayList<String> className;
    private final ArrayList<String> startTime;
    private final ArrayList<String> endTime;

    public ClassListAdapter(Activity context, ArrayList<String> className, ArrayList<String> startTime, ArrayList<String> endTime){
        super(context, R.layout.class_row, className);

        this.context = context;

        this.className = className;
        this.startTime = startTime;
        this.endTime = endTime;

    }


    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.class_row, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.className);
        TextView txtStart = (TextView) rowView.findViewById(R.id.classStartTime);
        TextView txtEnd = (TextView) rowView.findViewById(R.id.classEndTime);

        txtTitle.setText(className.get(position));
        txtStart.setText(startTime.get(position));
        txtEnd.setText(endTime.get(position));

        return rowView;
    };

}
