package com.sudhirkhanger.app.habittrackerapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class HabitProvider extends ContentProvider {

    private static final String LOG_TAG = HabitProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private HabitDbHelper mHabitDbHelper;

    private static final int MEDITATION = 100;
    private static final int MEDITATION_ID = 101;

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = HabitContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, HabitContract.MeditationEntry.TABLE_NAME, MEDITATION);
        matcher.addURI(authority, HabitContract.MeditationEntry.TABLE_NAME + "/#", MEDITATION_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mHabitDbHelper = new HabitDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;

        //TODO: REMOVE THIS
//        Log.d(LOG_TAG, "query(): where " + selection + " is " + selectionArgs[0]);

        switch (sUriMatcher.match(uri)) {
            case MEDITATION:
                retCursor = mHabitDbHelper.getReadableDatabase().query(
                        HabitContract.MeditationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;
            case MEDITATION_ID:
                retCursor = mHabitDbHelper.getReadableDatabase().query(
                        HabitContract.MeditationEntry.TABLE_NAME,
                        projection,
                        HabitContract.MeditationEntry.COLUMN_DATE + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEDITATION:
                return HabitContract.MeditationEntry.CONTENT_DIR_TYPE;
            case MEDITATION_ID:
                return HabitContract.MeditationEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mHabitDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MEDITATION:
                long _id = db.insert(HabitContract.MeditationEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = HabitContract.MeditationEntry.buildProductUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mHabitDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;

        //TODO REMOVE THIS
//        Log.d(LOG_TAG, "delete(): where " + selection + " is " + selectionArgs[0]);

        switch (match) {
            case MEDITATION:
                numDeleted = db.delete(
                        HabitContract.MeditationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MEDITATION_ID:
                numDeleted = db.delete(
                        HabitContract.MeditationEntry.TABLE_NAME,
                        HabitContract.MeditationEntry.COLUMN_DATE + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (numDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mHabitDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numUpdated = 0;

        if (contentValues == null) {
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch (match) {
            case MEDITATION:
                numUpdated = db.update(HabitContract.MeditationEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            case MEDITATION_ID:
                String id = String.valueOf(ContentUris.parseId(uri));
                numUpdated = db.update(HabitContract.MeditationEntry.TABLE_NAME,
                        contentValues,
                        HabitContract.MeditationEntry.COLUMN_DATE + " = ?",
                        new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (numUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numUpdated;
    }
}
