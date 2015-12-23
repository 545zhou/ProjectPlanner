package com.example.android.projectplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by tao on 8/30/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    static final String dbName = "demoDB";
    static final String projectTable = "Projects";

    static final String name = "name";
    static final String hour = "hour";
    static final String progress = "progress";
    static final String elapsedTime = "elapsedTime";
    static final String hasStarted = "hasStarted";
    static final String startTime = "startTime";
    static final String endTime = "endTime";

    public DatabaseHelper(Context context) {
        super(context, dbName, null, 33);
        //onUpgrade(this.getWritableDatabase(), 0, 0);
    }

    public void onCreate(SQLiteDatabase db) {

        String CREATE_PROJECTS_TABLE = "CREATE TABLE "
                                        + projectTable + "("
                                        + name  + " TEXT,"
                                        + hour + " REAL,"
                                        + progress + " INTEGER,"
                                        + elapsedTime + " REAL,"
                                        + hasStarted + " INTEGER,"
                                        + startTime + " TEXT,"
                                        + endTime + " TEXT"
                                        + ")";

        db.execSQL(CREATE_PROJECTS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + projectTable);
        onCreate(db);
    }

    public void addProject(ListNode project) {
        ContentValues values = new ContentValues();
        values.put(name, project.projectName);
        values.put(hour, project.projectHour);
        values.put(progress, project.projectProgress);
        values.put(elapsedTime, project.elapsedTime);
        values.put(hasStarted, (project.hasStarted ? 1 : 0));
        values.put(startTime, project.startTime);
        values.put(endTime, project.endTime);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(projectTable, null, values);
        db.close();
    }

    public ListNode findListNode(String projectName) {
        String query = "Select * FROM " + projectTable + " WHERE " + name + " =  \"" + projectName + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ListNode project = new ListNode(projectName, 1, 0, false);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            project.projectName = cursor.getString(0);
            project.projectHour = Float.valueOf(cursor.getString(1));
            project.projectProgress= (Integer.parseInt(cursor.getString(2)));
            project.elapsedTime = Float.valueOf(cursor.getString(3));
            project.hasStarted = (Integer.parseInt(cursor.getString(4)) == 1);
            project.startTime = cursor.getString(5);
            project.endTime = cursor.getString(6);
            cursor.close();
        } else {
            project = null;
        }
        db.close();
        return project;
    }

    public void updateNode(String oldName, ListNode updatedProject){
        ContentValues values = new ContentValues();
        values.put(name, updatedProject.projectName);
        values.put(hour, updatedProject.projectHour);
        values.put(progress, updatedProject.projectProgress);
        values.put(elapsedTime, updatedProject.elapsedTime);
        values.put(hasStarted, (updatedProject.hasStarted ? 1 : 0));
        values.put(startTime, updatedProject.startTime);
        values.put(endTime, updatedProject.endTime);

        SQLiteDatabase db = this.getWritableDatabase();

        db.update(projectTable, values, name + " = '" + oldName + "'", null);
        db.close();
    }

    public boolean deleteProject(String projectName) {

        boolean result = false;

        String query = "Select * FROM " + projectTable + " WHERE " + name + " =  \"" + projectName + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);


        if (cursor.moveToFirst()) {
            db.delete(projectTable, name + " = ?", new String[]{projectName});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public void deleteAllProjects(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ projectTable);
        db.close();
    }

    public ArrayList<ListNode> getAllProjects(){

        ArrayList<ListNode> result = new ArrayList<ListNode>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor= db.rawQuery("SELECT * FROM " + projectTable, null);

        if(cursor.getCount()==0)
        {
            return result;
        }

        while(cursor.moveToNext())
        {
            ListNode project = new ListNode("0", 1, 0, false);
            project.projectName = cursor.getString(0);
            project.projectHour = Double.valueOf(cursor.getString(1));
            project.projectProgress= (Integer.parseInt(cursor.getString(2)));
            project.elapsedTime = Double.valueOf(cursor.getString(3));
            project.hasStarted = (Integer.parseInt(cursor.getString(4)) == 1);
            project.startTime = cursor.getString(5);
            project.endTime = cursor.getString(6);
            Log.i("load database", project.projectName);
            result.add(project);
        }
        cursor.close();
        db.close();
        return result;
    }

}