package com.example.jamescha.finalproject.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by jamescha on 12/30/14.
 */
public class FitnessProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FitnessDbHelper fitnessDbHelper;

    private static final int STEPS = 100;
    private static final int CHARACTER = 201;
    private static final int STEPS_ID = 101;


    private static final SQLiteQueryBuilder sQueryBuilder;

    static {
        sQueryBuilder = new SQLiteQueryBuilder();
        sQueryBuilder.setTables(FitnessContract.StepsEntry.TABLE_NAME + ", " +
                                FitnessContract.CharacterEntry.TABLE_NAME);
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FitnessContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FitnessContract.PATH_STEPS, STEPS);
        matcher.addURI(authority, FitnessContract.PATH_STEPS + "/#", STEPS_ID);
        matcher.addURI(authority, FitnessContract.PATH_CHARACTER, CHARACTER);


        return matcher;
    }

    private Cursor getSteps(Uri uri, String[] projection, String sortOrder) {

        return sQueryBuilder.query(fitnessDbHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public boolean onCreate() {
        fitnessDbHelper = new FitnessDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            case STEPS: {
                retCursor = fitnessDbHelper.getReadableDatabase().query(
                        FitnessContract.StepsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case STEPS_ID: {
                retCursor = fitnessDbHelper.getReadableDatabase().query(
                        FitnessContract.StepsEntry.TABLE_NAME,
                        projection,
                        FitnessContract.StepsEntry._ID + " = '"+ ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case CHARACTER: {
                retCursor = fitnessDbHelper.getReadableDatabase().query(
                        FitnessContract.CharacterEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case STEPS:
                return FitnessContract.StepsEntry.CONTENT_TYPE;
            case STEPS_ID:
                return FitnessContract.StepsEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = fitnessDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case STEPS: {
                long _id = db.insert(FitnessContract.StepsEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FitnessContract.StepsEntry.buildStepsUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            case CHARACTER: {
                long _id = db.insert(FitnessContract.CharacterEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FitnessContract.CharacterEntry.buildCharacterUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = fitnessDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case STEPS:
                rowsUpdated = db.update(FitnessContract.StepsEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case CHARACTER:
                rowsUpdated = db.update(FitnessContract.CharacterEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = fitnessDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case STEPS: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value: values) {
                        long _id = db.insert(FitnessContract.StepsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
