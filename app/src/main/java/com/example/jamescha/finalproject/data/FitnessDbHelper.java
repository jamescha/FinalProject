package com.example.jamescha.finalproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.jamescha.finalproject.data.FitnessContract.StepsEntry;
import static com.example.jamescha.finalproject.data.FitnessContract.CharacterEntry;

/**
 * Created by jamescha on 12/31/14.
 */
public class FitnessDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "fitness.db";

    public FitnessDbHelper(Context context) {
        super (context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_STEPS_TABLE = "CREATE TABLE " + StepsEntry.TABLE_NAME + " (" +
                StepsEntry._ID + " INTEGER PRIMARY KEY, " +
                StepsEntry.COLUMN_STEPS_DATE + " TEXT UNIQUE NOT NULL, " +
                StepsEntry.COLUMN_STEPS_COUNT + " INTEGER NOT NULL" +
                " );";

        final String SQL_CREATE_CHARACTER_TABLE = "CREATE TABLE " + CharacterEntry.TABLE_NAME + " (" +
                CharacterEntry._ID + " INTEGER PRIMARY KEY, " +
                CharacterEntry.COLUMN_CHARACTER_NAME + " TEXT UNIQUE NOT NULL, " +
                CharacterEntry.COLUMN_CHARACTER_LEVEL + " INTEGER NOT NULL, " +
                CharacterEntry.COLUMN_CHARACTER_EXP + " INTEGER NOT NULL" +
                " );";

        db.execSQL(SQL_CREATE_STEPS_TABLE);
        db.execSQL(SQL_CREATE_CHARACTER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StepsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CharacterEntry.TABLE_NAME);
        onCreate(db);
    }
}
