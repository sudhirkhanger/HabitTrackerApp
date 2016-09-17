package com.sudhirkhanger.app.habittrackerapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

//        insert("Yes", 4);
//        update();
//        delete();
//        deleteDatabase();
//        deleteAll();
        logQueryResults(queryAll());
//        logQueryResults(queryWhere());
//        logQueryResults(rubricCompliantQuery());
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
     * Uses content provider no need to get
     * readable database
     */
    private Cursor queryAll() {
        return mContentResolver.query(
                HabitContract.MeditationEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    /*
     * Follows rubric
     * returns Cursor
     * gets a readable data storage
     */
    private Cursor rubricCompliantQuery() {
        SQLiteDatabase db = new HabitDbHelper(this).getReadableDatabase();
        return db.query(
                HabitContract.MeditationEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
    }

    /*
     * Will return rows with particular selection arguments
     * Make sure to append L for long in date
     * Also change the time stamp based on data on your system
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

    /*
     * update the uri based on data on your system
     */
    private void update() {
        mContentValues.clear();
        mContentValues.put(HabitContract.MeditationEntry.COLUMN_DID_MEDITATION, "Yes");
        mContentValues.put(HabitContract.MeditationEntry.COLUMN_MEDITATION_LENGTH, 8);
        Uri uri = ContentUris.withAppendedId(HabitContract.MeditationEntry.CONTENT_URI, 20160917104605L);
        int rowsUpdated = mContentResolver.update(uri, mContentValues, null, null);
        Log.d(LOG_TAG, "Rows updated: " + rowsUpdated);
    }

    private void insert(String yesOrNo, int time) {
        mContentValues.clear();
        mContentValues.put(HabitContract.MeditationEntry.COLUMN_DID_MEDITATION, yesOrNo);
        mContentValues.put(HabitContract.MeditationEntry.COLUMN_MEDITATION_LENGTH, time);
        mContentValues.put(HabitContract.MeditationEntry.COLUMN_DATE, getCurrentTime());
        mContentResolver.insert(HabitContract.MeditationEntry.CONTENT_URI, mContentValues);
    }

    // Make sure to append L for long in date
    // update time stamp as per your system
    private void delete() {
        int rowsDeleted;
        rowsDeleted = mContentResolver.delete(
                HabitContract.MeditationEntry.CONTENT_URI,
                HabitContract.MeditationEntry.COLUMN_DATE + " = ?",
                new String[]{"20160917104325L"});
        Log.d(LOG_TAG, "# of Rows Deleted: " + rowsDeleted);
    }

    private void deleteAll() {
        int rowsDeleted;
        rowsDeleted = mContentResolver.delete(
                HabitContract.MeditationEntry.CONTENT_URI,
                null,
                null);
        Log.d(LOG_TAG, "# of Rows Deleted: " + rowsDeleted);
    }

    // Delete the whole database
    private void deleteDatabase() {
        new HabitDbHelper(this).deleteDatabase(this);
    }
}
