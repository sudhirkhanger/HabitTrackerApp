package com.sudhirkhanger.app.habittrackerapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ContentResolver mContentResolver;
    private ContentValues mContentValues;
    private String[] projection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContentResolver = getContentResolver();
        mContentValues = new ContentValues();
        projection = new String[]{
                HabitContract.MeditationEntry._ID,
                HabitContract.MeditationEntry.COLUMN_DID_MEDITATION,
                HabitContract.MeditationEntry.COLUMN_DATE,
                HabitContract.MeditationEntry.COLUMN_MEDITATION_LENGTH};

        insert();
//        update();
//        delete();
//        deleteDatabase();
//        logQueryResults(query());
//        logQueryResults(queryWhere());
    }

    /*
     * Get current time in YYYYMMDDHHMMSS format
     */
    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyyMMddhhmmss", Locale.US);
        String timestamp = mdformat.format(calendar.getTime()) + "L";
        return timestamp;
    }

    /*
     * Returns all rows
     */
    private Cursor query() {
        return mContentResolver.query(
                HabitContract.MeditationEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    /*
     * Will return rows with particular selection arguments
     * Make sure to append L for long in date
     */
    private Cursor queryWhere() {
        return mContentResolver.query(
                HabitContract.MeditationEntry.CONTENT_URI,
                projection,
                HabitContract.MeditationEntry.COLUMN_DATE + " = ?",
                new String[]{"20160917080554L"},
                null);
    }

    // Simple logger
    private void logQueryResults(Cursor cursor) {
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String did_meditation = cursor.getString(cursor.getColumnIndex(HabitContract.MeditationEntry.COLUMN_DID_MEDITATION));
                    long date = cursor.getLong(cursor.getColumnIndex(HabitContract.MeditationEntry.COLUMN_DATE));
                    int length = cursor.getInt(cursor.getColumnIndex(HabitContract.MeditationEntry.COLUMN_MEDITATION_LENGTH));
                    Log.d(LOG_TAG, "Meditated " + did_meditation + " on " + date + " for " + length + " mins");
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
    }


    private void update() {
        mContentValues.clear();
        mContentValues.put(HabitContract.MeditationEntry.COLUMN_DID_MEDITATION, "No");
        mContentValues.put(HabitContract.MeditationEntry.COLUMN_MEDITATION_LENGTH, 0);
        Uri uri = ContentUris.withAppendedId(HabitContract.MeditationEntry.CONTENT_URI, 20160917020600L);
        int rowsUpdated = mContentResolver.update(uri, mContentValues, null, null);
        Log.d(LOG_TAG, "Rows updated: " + rowsUpdated);
    }

    private void insert() {
        mContentValues.clear();
        mContentValues.put(HabitContract.MeditationEntry.COLUMN_DID_MEDITATION, "Yes");
        mContentValues.put(HabitContract.MeditationEntry.COLUMN_MEDITATION_LENGTH, 16);
        mContentValues.put(HabitContract.MeditationEntry.COLUMN_DATE, getCurrentTime());
        mContentResolver.insert(HabitContract.MeditationEntry.CONTENT_URI, mContentValues);
    }

    // Make sure to append L for long in date
    private void delete() {
        int rowsDeleted;
        rowsDeleted = mContentResolver.delete(
                HabitContract.MeditationEntry.CONTENT_URI,
                HabitContract.MeditationEntry.COLUMN_DATE + " = ?",
                new String[]{"20160917080554L"});
        Log.d(LOG_TAG, "# of Rows Deleted: " + rowsDeleted);
    }

    // Delete the whole database
    private void deleteDatabase() {
        new HabitDbHelper(this).deleteDatabase(this);
    }
}
