package com.example.group10.FlipCardPacket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class FlipCardDatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    // Database Name
    private static final String NAME = "FlipCardDatabase";
    // tasks table name
    private static final String FC_TABLE = "FlipCard";
    // tasks Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_FRONT = "front";
    private static final String KEY_BACK = "back";
    //sqlite
    private static final String CREATE_FC_TABLE = "CREATE TABLE IF NOT EXISTS " + FC_TABLE + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TITLE + " TEXT, "
            + KEY_FRONT + " TEXT, "
            + KEY_BACK + " TEXT)";

    public FlipCardDatabaseHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_FC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drop the older tables if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FC_TABLE);
        //Create tables again
        onCreate(sqLiteDatabase);
    }

    //read from database and store all values in a list and return the list.
    public ArrayList<FlipCardModel> getAllTasks(){
        ArrayList<FlipCardModel> list = new ArrayList<FlipCardModel>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + FC_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FlipCardModel flipCard = new FlipCardModel();
                flipCard.setId(cursor.getInt(0));
                flipCard.setTitle(cursor.getString(1));
                flipCard.setFront(cursor.getString(2));
                flipCard.setBack(cursor.getString(3));
                // Adding contact to list
                list.add(flipCard);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        // return task list
        return list;
    }

    public void insertTask(FlipCardModel flipCardModel){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv  = new ContentValues();
        cv.put(KEY_TITLE, flipCardModel.getTitle());
        cv.put(KEY_FRONT, flipCardModel.getFront());
        cv.put(KEY_BACK, flipCardModel.getBack());
        db.insert(FC_TABLE, null, cv);
        db.close();
    }

    public void deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FC_TABLE, KEY_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(FlipCardModel flipCardModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, flipCardModel.getTitle());
        cv.put(KEY_FRONT, flipCardModel.getFront());
        cv.put(KEY_BACK, flipCardModel.getBack());
        db.update(FC_TABLE, cv, KEY_ID + "= ?", new String[] {String.valueOf(flipCardModel.getId())});
    }
}
