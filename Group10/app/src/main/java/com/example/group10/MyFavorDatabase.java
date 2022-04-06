package com.example.group10;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class MyFavorDatabase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    // Database Name
    private static final String NAME = "MyFavorDatabase";
    // tasks table name
    private static final String FAVOR_TABLE = "Favor";
    // tasks Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TYPE = "type";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_FC_front = "fcFront";
    private static final String KEY_FC_back = "fcBack";
    private static final String CREATE_FAVOR_TABLE = "CREATE TABLE IF NOT EXISTS " + FAVOR_TABLE + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TITLE + " TEXT, "
            + KEY_TYPE + " INTEGER, "
            + KEY_CONTENT + " TEXT, "
            + KEY_FC_front + " TEXT, "
            + KEY_FC_back + " TEXT)";

    public MyFavorDatabase(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_FAVOR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drop the older tables if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FAVOR_TABLE);
        //Create tables again
        onCreate(sqLiteDatabase);
    }

    public void insertTask(MyFavorModel myFavorModel){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv  = new ContentValues();
        if (myFavorModel.getType() == 1) {
            //insert note
            cv.put(KEY_TYPE, myFavorModel.getType());
            cv.put(KEY_TITLE, myFavorModel.getTitle());
            cv.put(KEY_CONTENT, myFavorModel.getContent());
        } else {
            //insert flip-card
            cv.put(KEY_TYPE, myFavorModel.getType());
            cv.put(KEY_TITLE, myFavorModel.getTitle());
            cv.put(KEY_FC_front, myFavorModel.getFront());
            cv.put(KEY_FC_back, myFavorModel.getBack());
        }
        db.insert(FAVOR_TABLE, null, cv);
        db.close();
    }

    public void deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FAVOR_TABLE, KEY_ID + "= ?", new String[] {String.valueOf(id)});
    }
}
