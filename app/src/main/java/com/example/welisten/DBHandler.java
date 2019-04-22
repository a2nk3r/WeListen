package com.example.welisten;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ttsdb.db";
    private static final String TABLE_NAME = "InputHistory";
    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_INPUT = "Input";
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( " + COLUMN_DATE + " TEXT PRIMARY KEY, " + COLUMN_INPUT +" TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public ArrayList<InputHistory> loadHandler(){
        InputHistory result;
        ArrayList<InputHistory> resultArray = new ArrayList<>();
        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext()){
            String r_0 = cursor.getString(0);
            String r_1 = cursor.getString(1);
            result = new InputHistory(r_0, r_1);
            resultArray.add(result);
        }
        cursor.close();
        db.close();
        return resultArray;
    }
    public void addHandler(InputHistory input){
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, input.getDate());
        values.put(COLUMN_INPUT, input.getInputText());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public void clearHandler(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("Delete from " + TABLE_NAME);
        db.close();
    }

}
