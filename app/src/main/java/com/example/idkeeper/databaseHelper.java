package com.example.idkeeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ThemedSpinnerAdapter;

import androidx.annotation.Nullable;

public class databaseHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "User.db";
    private final static String TABLE_NAME = "User_table";
    private final static String COL0 = "ID";
    private final static String COL1 = "Name";
    private final static String COL2 = "Surname";
    private final static String COL3 = "Pin";

    public databaseHelper(@Nullable Context context) {super(context, TABLE_NAME, null, 1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table = "CREATE TABLE "+TABLE_NAME+"("+COL0+" integer primary key autoincrement, "+COL1+" text, "+COL2+" text, "+COL3+" text)";
        db.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean addUser(String name, String surname, String pin){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL1, name);
        cv.put(COL2, surname);
        cv.put(COL3, pin);
        long result = db.insert(""+TABLE_NAME, null, cv);
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME+"", null);
        return res;
    }

    public boolean updateUser(String id, String name, String surname, String pin){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL0, id);
        cv.put(COL1, name);
        cv.put(COL2, surname);
        cv.put(COL3, pin);
        db.update(""+TABLE_NAME, cv, "id = ?", new String[]{id});
        return true;
    }
}
