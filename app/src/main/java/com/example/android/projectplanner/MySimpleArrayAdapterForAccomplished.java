package com.example.android.projectplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tao on 8/19/15.
 */
public class MySimpleArrayAdapterForAccomplished extends ArrayAdapter<ListNode> {
    private final Context context;
    private final ArrayList<ListNode> projects;
    public MySimpleArrayAdapterForAccomplished(Context context, ArrayList<ListNode> projects) {
        super(context, R.layout.accomplished_row_layout,projects);
        this.context = context;
        this.projects = projects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.accomplished_row_layout, parent, false);


        // find the name id
        TextView projectName = (TextView) rowView.findViewById(R.id.accomplished_project);

        // find the hour id
        TextView projectHours = (TextView) rowView.findViewById(R.id.accomplished_hour);

        // find the progressbar id

        TextView projectStartTime = (TextView) rowView.findViewById(R.id.start_time);
        TextView projectDoneTime = (TextView) rowView.findViewById(R.id.end_time);

        ListNode curNode = projects.get(position);
        // set project names
        projectName.setText(curNode.projectName);

        //set project hours
        String hourText = String.format("%.1f", curNode.projectHour);
        if (curNode.projectHour == 1)
            hourText = hourText + " hour";
        else
            hourText = hourText + " hours";
        projectHours.setText(hourText);

        // set project start and finished time

        projectStartTime.setText(curNode.startTime);
        projectDoneTime.setText(curNode.endTime);

        return rowView;
    }
}
