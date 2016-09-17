package com.sudhirkhanger.app.habittrackerapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class HabitContract {

    public static final String CONTENT_AUTHORITY = "com.sudhirkhanger.app.habittrackerapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MEDITATION = "meditation";

    public static final class MeditationEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MEDITATION).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEDITATION;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEDITATION;

        public static final String TABLE_NAME = "meditation";

        public static final String COLUMN_DID_MEDITATION = "did_meditation";
        public static final String COLUMN_MEDITATION_LENGTH = "length";
        public static final String COLUMN_DATE = "date";

        public static Uri buildProductUri(long date) {
            return ContentUris.withAppendedId(CONTENT_URI, date);
        }
    }
}
