package com.example.idkeeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class databaseHelper2 extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "ID.db";
    private final static String TABLE_NAME = "ID_table";
    private final static String COL0 = "ID";
    private final static String COL1 = "IDtype";
    private final static String COL2 = "IDcode";
    private final static String COL3 = "Nationality";
    private final static String COL4 = "ExpDate";
    private final static String COL5 = "Bytes";

    public databaseHelper2(@Nullable Context context) {super(context, TABLE_NAME, null, 1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table = "CREATE TABLE "+TABLE_NAME+"("+COL0+" integer primary key autoincrement, "+COL1+" text, "+COL2+" text, "+COL3+" text, "+COL4+" text, "+COL5+", text)";
        db.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean addID(String idType, String idCode, String nac, String expDate, String bytes){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL1, idType);
        cv.put(COL2, idCode);
        cv.put(COL3, nac);
        cv.put(COL4, expDate);
        cv.put(COL5, bytes);
        long result = db.insert(""+TABLE_NAME, null, cv);
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getID(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME+"", null);
        return res;
    }

    public boolean updateID(String id, String idType, String idCode, String nac, String expDate, String bytes){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL0, id);
        cv.put(COL1, idType);
        cv.put(COL2, idCode);
        cv.put(COL3, nac);
        cv.put(COL4, expDate);
        cv.put(COL5, bytes);
        db.update(""+TABLE_NAME, cv, "id = ?", new String[]{id});
        return true;
    }
}
