package com.example.android.projectplanner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by tao on 8/26/15.
 */

public class ListNode{
    String projectName = "";
    double projectHour = 100;
    int  projectProgress = 0;
    boolean hasStarted = false;
    double elapsedTime = 0;
    String startTime;
    String endTime;

    public ListNode(String name, double hour, int progress, boolean started){
        projectName = name;
        projectHour = hour;
        projectProgress = progress;
        hasStarted = started;
        elapsedTime = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH");
        startTime = dateFormat.format(Calendar.getInstance().getTime());
        endTime = "";
    }
}