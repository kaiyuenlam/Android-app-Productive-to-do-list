package com.example.group10.fragments.notesFragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.List;

public class NotesDatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    // Database Name
    private static final String NAME = "NotesDatabase";
    // tasks table name
    private static final String NOTES_TABLE = "notes";
    // tasks Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    //sqlite
    private static final String CREATE_NOTES_TABLE = "CREATE TABLE IF NOT EXISTS " + NOTES_TABLE + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TITLE + " TEXT, "
            + KEY_CONTENT + " TEXT)";


    public NotesDatabaseHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drop the older tables if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE);
        //Create tables again
        onCreate(sqLiteDatabase);
    }

    //read from database and store all values in a list and return the list.
    public List<NotesModel> getAllTasks(){
        List<NotesModel> notesList = new ArrayList<NotesModel>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + NOTES_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NotesModel notesModel = new NotesModel();
                notesModel.setId(cursor.getInt(0));
                notesModel.setTitle(cursor.getString(1));
                notesModel.setContent(cursor.getString(2));
                // Adding contact to list
                notesList.add(notesModel);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        // return task list
        return notesList;
    }

    public void insertTask(NotesModel notesModel){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv  = new ContentValues();
        cv.put(KEY_TITLE, notesModel.getTitle());
        cv.put(KEY_CONTENT, notesModel.getContent());
        db.insert(NOTES_TABLE, null, cv);
        db.close();
    }

    public void deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTES_TABLE, KEY_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(NotesModel notesModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, notesModel.getTitle());
        cv.put(KEY_CONTENT, notesModel.getContent());
        db.update(NOTES_TABLE, cv, KEY_ID + "= ?", new String[] {String.valueOf(notesModel.getId())});
    }
}
