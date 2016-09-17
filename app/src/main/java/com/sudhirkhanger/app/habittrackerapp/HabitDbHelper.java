package com.sudhirkhanger.app.habittrackerapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HabitDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "habits.db";

    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATION_MEDITATION_TABLE =
                "CREATE TABLE " + HabitContract.MeditationEntry.TABLE_NAME + " (" +
                        HabitContract.MeditationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        HabitContract.MeditationEntry.COLUMN_DATE + " INTEGER UNIQUE NOT NULL, " +
                        HabitContract.MeditationEntry.COLUMN_DID_MEDITATION + " TEXT NOT NULL, " +
                        HabitContract.MeditationEntry.COLUMN_MEDITATION_LENGTH + " INTEGER NOT NULL" +
                        " );";
        sqLiteDatabase.execSQL(SQL_CREATION_MEDITATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HabitContract.MeditationEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
