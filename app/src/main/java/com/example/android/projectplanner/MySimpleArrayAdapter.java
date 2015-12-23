package com.example.android.projectplanner;

/**
 * Created by tao on 4/6/2015.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MySimpleArrayAdapter extends ArrayAdapter<ListNode> {
    private final Context context;
    private final ArrayList<ListNode> listNodes;

    public MySimpleArrayAdapter(Context context, ArrayList<ListNode> listNodes) {
        super(context, R.layout.rowlayout, listNodes);
        this.context = context;
        this.listNodes = listNodes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        ListNode curNode = listNodes.get(position);
        // find the name id
        TextView project_name = (TextView) rowView.findViewById(R.id.label_project);

        // find the hour id
        TextView project_hours = (TextView) rowView.findViewById(R.id.label_hours);

        // find the progressbar id
        ProgressBar project_progressbar = (ProgressBar) rowView.findViewById(R.id.progressbar);

        // set the start time
        TextView project_start_time = (TextView) rowView.findViewById(R.id.start_time);
        int len = curNode.startTime.length();
        String start_time_string = curNode.startTime.substring(len - 2, len);
        String amOrPm = "pm";
        if(Integer.parseInt(start_time_string) <= 11){
            amOrPm = "am";
        }
        project_start_time.setText("Started at: " + curNode.startTime + amOrPm);

        //set listNodes names
        project_name.setText(curNode.projectName);

        // set the progressbar color to green
        project_progressbar.getProgressDrawable().setColorFilter(Color.GREEN, Mode.MULTIPLY);
        project_progressbar.setProgress(curNode.projectProgress);

        // get listNodes hours
        String hourText = String.format("%.1f", curNode.projectHour);
        if(curNode.projectHour == 1)
            hourText = hourText + " hour";
        else
            hourText = hourText + " hours";
        project_hours.setText(hourText);

        // set start button
        Button startButton = (Button) rowView.findViewById(R.id.button_status);
        if(curNode.hasStarted && curNode.projectProgress < 100){
            startButton.setText("Pause");
        }
        else if (!curNode.hasStarted && curNode.projectProgress < 100){
            startButton.setText("Start");
        }
        else{
            startButton.setText("Done!");
        }

        TextView textView = (TextView) rowView.findViewById(R.id.show_time);
        int elapsedHour = (int) (curNode.elapsedTime);
        int elapsedMin = (int) ((curNode.elapsedTime - elapsedHour) * 60);

        String showTime = Integer.toString(elapsedHour) + " hours " + Integer.toString(elapsedMin) + " mins";
        textView.setText(showTime);
        return rowView;
    }
}