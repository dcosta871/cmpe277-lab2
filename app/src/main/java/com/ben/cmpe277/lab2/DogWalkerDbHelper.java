package com.ben.cmpe277.lab2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DogWalkerDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DogWalkerContract.DogWalkerEntry.TABLE_NAME + " (" +
                    DogWalkerContract.DogWalkerEntry._ID + " INTEGER PRIMARY KEY," +
                    DogWalkerContract.DogWalkerEntry.COLUMN_NAME_NAME + " TEXT," +
                    DogWalkerContract.DogWalkerEntry.COLUMN_NAME_DOGS_WALKED + " TEXT," +
                    DogWalkerContract.DogWalkerEntry.COLUMN_NAME_DOGS_PHONE_NUMBER + " TEXT," +
                    DogWalkerContract.DogWalkerEntry.COLUMN_NAME_RATING + " TEXT," +
                    DogWalkerContract.DogWalkerEntry.COLUMN_SMALL_DOGS + " TEXT," +
                    DogWalkerContract.DogWalkerEntry.COLUMN_MEDIUM_DOGS + " TEXT," +
                    DogWalkerContract.DogWalkerEntry.COLUMN_LARGE_DOGS + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DogWalkerContract.DogWalkerEntry.TABLE_NAME;

    public DogWalkerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
