package com.example.calendarv2.Database;

/**
 * created on 7/12/2019
 * BY: Moulue Huang
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Paychecks.db";
    public static final String TABLE_NAME = "Paychecks_table";
    public static final String DATE = "DATE";
    public static final String HOURS = "HOURS";
    public static final String PAY = "PAY";

    //constructor for the database
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null,1);
    }

    //create a database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (DATE TEXT,HOURS TEXT, PAY TEXT)");
    }

    //don't delete the database if a SQLiteDatabase already exists
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    //insert data into the database
    public boolean insertData(String date, String hours, String pay){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE,date);
        contentValues.put(HOURS,hours);
        contentValues.put(PAY,pay);

        long result = db.insert(TABLE_NAME,null,contentValues);

        if(result == -1) return false;
        else return true;
    }
    //return all the information that is in the database
    public Cursor getInformation(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME,null);
        return result;
    }
    public boolean deleteData(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        Integer isDeleted = db.delete(TABLE_NAME,"Date = ?", new String[] {date});
        if(isDeleted > 0) return true;
        return false;
    }
}
