package com.example.android.projectplanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity implements AddNewProjectDialog.AddNewProjectDialogListener, ProjectSettingDialog.ProjectSettingDialogListener{

    // onGoingProjectNodes is storing the currently running projects
    ArrayList<ListNode> onGoingProjectNodes = new ArrayList<ListNode>();
    // finishedProjectNodes stores finished projects
    ArrayList<ListNode> finishedProjectNodes = new ArrayList<ListNode>();
    // adapter
    MySimpleArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load projects
        moveDatabaseToOnGoingProjects();
        updateFinishedProjectsNodes();

        // set trophies button click listener
        Button trophiesButton = (Button) findViewById(R.id.trophies_button);
        trophiesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Perform action on click
                updateFinishedProjectsNodes();
                DataHolder.save("done", finishedProjectNodes);
                Intent myIntent = new Intent(view.getContext(), ActivityAccomplished.class);
                startActivityForResult(myIntent, 0);
            }
        });

        // set main list view click listener
        ListView mainList = (ListView) findViewById(R.id.list_view);
        if(onGoingProjectNodes.size() != 0) {
            adapter = new MySimpleArrayAdapter(this, onGoingProjectNodes);
            mainList.setAdapter(adapter);

            mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                    adb.setMessage("Want to remove this project?");
                    final int positionToRemove = position;
                    adb.setNegativeButton("No", null);
                    adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteNodeInDatabase(onGoingProjectNodes.get(positionToRemove).projectName);
                            onGoingProjectNodes.remove(positionToRemove);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    adb.show();
                }
            });
        }

        // add timer
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (onGoingProjectNodes.size() > 0) {
                            updateTime();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }, 0, 3000);

    }

    // update the elapsed time in each ongoing project
    public void updateTime(){
        for(int i = 0; i < onGoingProjectNodes.size(); i++){
            ListNode node = onGoingProjectNodes.get(i);
            if(node.elapsedTime < node.projectHour) {
                if (node.hasStarted) {
                    node.elapsedTime += 8.333333333333334E-4;
                    node.projectProgress = (int) (node.elapsedTime * 100 / node.projectHour);
                    if(node.elapsedTime >= node.projectHour){
                        node.projectProgress = 100;
                        node.hasStarted = false;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH");
                        node.endTime = dateFormat.format(Calendar.getInstance().getTime());
                    }
                    updateNodeInDatabase(node.projectName, node);
                }
            }

        }
    }


    public void updateFinishedProjectsNodes(){
        ArrayList<ListNode> newOnGoingProjectNodes = new ArrayList<ListNode>();
        for(int i = 0; i < onGoingProjectNodes.size(); i++){
            ListNode node = onGoingProjectNodes.get(i);
            if(node.elapsedTime < node.projectHour){
                newOnGoingProjectNodes.add(node);
            }
            else{
                finishedProjectNodes.add(node);
            }
        }

        onGoingProjectNodes = newOnGoingProjectNodes;

        adapter = new MySimpleArrayAdapter(this, onGoingProjectNodes);
        ListView mainList = (ListView) findViewById(R.id.list_view);
        mainList.setAdapter(adapter);
    }

    public void moveDatabaseToOnGoingProjects(){
        DatabaseHelper db = new DatabaseHelper(this);
        onGoingProjectNodes = db.getAllProjects();
    }
    
    public void addProjectToDatabase(ListNode node){
        DatabaseHelper db = new DatabaseHelper(this);
        db.addProject(node);
    }

    public void updateNodeInDatabase(String projectName, ListNode updatedProject){
        DatabaseHelper db = new DatabaseHelper(this);
        db.updateNode(projectName, updatedProject);
    }

    public void deleteNodeInDatabase(String projectName){
        DatabaseHelper db = new DatabaseHelper(this);
        db.deleteProject(projectName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            showAddNewProjectDialog();
        }

        if(id == R.id.action_other){
            AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
            adb.setMessage("By Monitoring the time you\'ve worked on a project and the progress you\'ve accomplished, " +
                    "this app helps dreamers like you weave dreams. " + "\n" +
                    "This app is totally free. I built it because one of my dreams is to be a software engineer whose product " +
                    "would benefit others." + "\n" +
                    "I will keep improving this app. If you have any suggestion or idea, please contact 545zhou@gmail.com.^o^" );
            adb.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showAddNewProjectDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddNewProjectDialog newProjectDialog = new AddNewProjectDialog();

        newProjectDialog.show(fragmentManager, "AddNewProjectDialog");
    }

    @Override
    public void onNewProjectDialogPositiveClick(DialogFragment dialog, String name, double hour) {
        // User touched the dialog's positive button
        ListNode project = new ListNode(name, hour, 0, false);
        onGoingProjectNodes.add(project);
        addProjectToDatabase(project);
        adapter = new MySimpleArrayAdapter(this, onGoingProjectNodes);
        ListView mainList = (ListView) findViewById(R.id.list_view);
        mainList.setAdapter(adapter);
    }

    @Override
    public void onNewProjectDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

    public void onProjectSettingDialogPositiveClick(DialogFragment dialog, String name, double hour, double hour_boost, double hour_reduce, int index) {
        // User touched the dialog's positive button
        ListNode node = onGoingProjectNodes.get(index);
        String oldName = node.projectName;
        if(name.length() != 0) {
            node.projectName = name;
        }

        if(hour > 0) {
            node.projectHour = hour;
        }

        if(hour_boost > 0){
            node.elapsedTime += hour_boost;
        }

        if(hour_reduce > 0){
            node.elapsedTime -= hour_reduce;
            if(node.elapsedTime < 0){
                node.elapsedTime = 0.0;
            }
        }

        if(node.elapsedTime >= node.projectHour){
            node.elapsedTime = node.projectHour;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH");
            node.endTime = dateFormat.format(Calendar.getInstance().getTime());
            node.hasStarted = false;
            node.projectProgress = 100;
        }

        node.projectProgress = (int) (node.elapsedTime * 100 / node.projectHour);


        updateNodeInDatabase(oldName, node);
        ListView mainList = (ListView) findViewById(R.id.list_view);
        mainList.setAdapter(adapter);
    }

    @Override
    public void onProjectSettingDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

    public void mySetClickHandler(View v) {

        RelativeLayout vwGrandpaRow = (RelativeLayout)(v.getParent().getParent());
        ListView vwGrandGrandpaList = (ListView) (vwGrandpaRow.getParent());
        int index = vwGrandGrandpaList.getPositionForView(vwGrandpaRow);
        showProjectSettingDialog(index);
    }

    public void showProjectSettingDialog(int index){
        FragmentManager fragmentManager = getSupportFragmentManager();
        ProjectSettingDialog newProjectSettingDialog = new ProjectSettingDialog();
        newProjectSettingDialog.getIndexInList(index);

        newProjectSettingDialog.show(fragmentManager, "ProjectSettingDialog");
    }

    public void myStartClickHandler(View v){

        RelativeLayout vwGrandpaRow = (RelativeLayout)(v.getParent().getParent());
        ListView vwGrandGrandpaList = (ListView) (vwGrandpaRow.getParent());
        int index = vwGrandGrandpaList.getPositionForView(vwGrandpaRow);

        ListNode curNode = onGoingProjectNodes.get(index);
        Button startButton = (Button) v;
        if(curNode.elapsedTime < curNode.projectHour) {
            if (startButton.getText().equals("Start")) {
                startButton.setText("Pause");
                curNode.hasStarted = true;
            } else {
                startButton.setText("Start");
                curNode.hasStarted = false;
            }
            updateNodeInDatabase(curNode.projectName, curNode);
        }

    }
}
