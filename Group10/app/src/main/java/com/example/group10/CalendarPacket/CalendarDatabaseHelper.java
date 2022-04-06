package com.example.group10.CalendarPacket;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.example.group10.todolist.ToDoModel;

import java.util.ArrayList;

public class CalendarDatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    // Database Name
    private static final String NAME = "CalendarDatabase";
    // tasks table name
    private static final String CAL_TABLE = "Calendar";
    // tasks Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_MONTH = "month";
    private static final String KEY_DATE = "date";
    private static final String KEY_LIST = "content";
    private static final String KEY_FINISHED = "finished";
    private static final String CREATE_CAL_TABLE = "CREATE TABLE IF NOT EXISTS " + CAL_TABLE + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_MONTH + " TEXT, "
            + KEY_DATE + " TEXT, "
            + KEY_FINISHED + " INTEGER, "
            + KEY_LIST + " TEXT)";

    public CalendarDatabaseHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drop the older tables if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CAL_TABLE);
        //Create tables again
        onCreate(sqLiteDatabase);
    }

    public void insertData(CalendarDatabaseModel calendarDatabaseModel) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv  = new ContentValues();
        cv.put(KEY_MONTH, calendarDatabaseModel.getMonth());
        cv.put(KEY_DATE, calendarDatabaseModel.getDate());
        cv.put(KEY_LIST, calendarDatabaseModel.getStringToDoList());
        cv.put(KEY_FINISHED, calendarDatabaseModel.getFinished());
        db.insert(CAL_TABLE, null, cv);
        db.close();
    }

    public void updateList(CalendarDatabaseModel calendarDatabaseModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_LIST, calendarDatabaseModel.getStringToDoList());
        cv.put(KEY_FINISHED, calendarDatabaseModel.getFinished());
        db.update(CAL_TABLE, cv, KEY_ID + "= ?", new String[] {String.valueOf(calendarDatabaseModel.getId())});
    }

    @SuppressLint("Range")
    public CalendarDatabaseModel getTheDay(String month, String date) {
        CalendarDatabaseModel newModel = new CalendarDatabaseModel();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        String sql = "SELECT * FROM " + CAL_TABLE + " WHERE " + KEY_MONTH + "=" + month + " AND " + KEY_DATE + "=" + date;
        ArrayList<CalendarDatabaseModel> taskList = new ArrayList<>();
        try {
            cursor = db.rawQuery(sql, null);
            int count = 1;
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    newModel.setMonth(cursor.getString(cursor.getColumnIndex(KEY_MONTH)));
                    newModel.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                    newModel.setTodoStringList(cursor.getString(cursor.getColumnIndex(KEY_LIST)));
                    newModel.setFinished(cursor.getInt(cursor.getColumnIndex(KEY_FINISHED)));
                }
            } else {
                newModel.setMonth(month);
                newModel.setDate(date);
                newModel.setFinished(0);
            }
        } finally {
            cursor.close();
        }
        db.close();
        return newModel;
    }

}
