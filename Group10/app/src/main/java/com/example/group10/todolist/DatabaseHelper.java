package com.example.group10.todolist;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    // Database Name
    private static final String NAME = "toDoListDatabase";
    // tasks table name
    private static final String TODO_TABLE = "todo";
    // tasks Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TASK = "task";
    private static final String KEY_STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE IF NOT EXISTS " + TODO_TABLE + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TASK + " TEXT, "
            + KEY_STATUS + " INTEGER)";


    public DatabaseHelper(Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drop the older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        //Create tables again
        onCreate(db);
    }

    public void insertTask(ToDoModel task){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv  = new ContentValues();
        cv.put(KEY_TASK, task.getTask());
        cv.put(KEY_STATUS, task.getStatus());
        db.insert(TODO_TABLE, null, cv);
        db.close();
    }

    //read from database and store all values in an Array list.
    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<ToDoModel>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TODO_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ToDoModel toDoModel = new ToDoModel();
                toDoModel.setId(cursor.getInt(0));
                toDoModel.setTask(cursor.getString(1));
                toDoModel.setStatus(cursor.getInt(2));
                // Adding contact to list
                taskList.add(toDoModel);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        // return task list
        return taskList;
    }

    public void updateStatus(int id, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_STATUS, status);
        db.update(TODO_TABLE, cv, KEY_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TASK, task);
        db.update(TODO_TABLE, cv, KEY_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TODO_TABLE, KEY_ID + "= ?", new String[] {String.valueOf(id)});
    }

}