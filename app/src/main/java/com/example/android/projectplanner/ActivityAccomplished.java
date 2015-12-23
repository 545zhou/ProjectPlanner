package com.example.android.projectplanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by tao on 8/19/15.
 */
public class ActivityAccomplished extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accomplised_list);
        final ListView accomplishedList = (ListView) findViewById(R.id.list_view_accomplished);
        final ArrayList<ListNode> projects = (ArrayList<ListNode>)DataHolder.retrieve("done");
        final MySimpleArrayAdapterForAccomplished adapter = new MySimpleArrayAdapterForAccomplished(this, projects);
        accomplishedList.setAdapter(adapter);

        accomplishedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(ActivityAccomplished.this);
                adb.setMessage("Want to remove this project?");
                final int positionToRemove = position;
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNodeInDatabase(projects.get(positionToRemove).projectName);
                        projects.remove(positionToRemove);
                        adapter.notifyDataSetChanged();
                    }
                });
                adb.show();
            }
        });
    }

    public void deleteNodeInDatabase(String projectName){
        DatabaseHelper db = new DatabaseHelper(this);
        db.deleteProject(projectName);
    }
}
