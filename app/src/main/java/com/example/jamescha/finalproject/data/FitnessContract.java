package com.example.jamescha.finalproject.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jamescha on 12/27/14.
 */
public class FitnessContract {
    public static final String CONTENT_AUTHORITY = "com.example.jamescha.finalproject";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_STEPS = "steps";
    public static final String PATH_CHARACTER = "character";
//    public static final String PATH_STATS = "stats";
//    public static final String PATH_INVENTORY = "inventory";

    public static final String DATE_FORMAT = "yyyyMMdd";

    public static Date getDateFromDb(String dateText) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return dbDateFormat.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final class StepsEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEPS).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_STEPS;

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_STEPS;

        public static final String TABLE_NAME = "steps";

        public static final String COLUMN_STEPS_COUNT = "steps_count";
        public static final String COLUMN_STEPS_DATE = "steps_date";

        public static Uri buildStepsUri (long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CharacterEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHARACTER).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CHARACTER;

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_CHARACTER;

        public static final String TABLE_NAME = "character";

        public static final String COLUMN_CHARACTER_NAME = "character_name";
        public static final String COLUMN_CHARACTER_LEVEL = "character_level";
        public static final String COLUMN_CHARACTER_EXP = "character_exp";

        public static Uri buildCharacterUri (long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
