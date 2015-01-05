package com.example.jamescha.finalproject.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.jamescha.finalproject.R;
import com.example.jamescha.finalproject.data.FitnessContract;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * Created by jamescha on 12/31/14.
 */

//TODO Finish to have data sync in the backround
public class FitnessSyncAdapter extends AbstractThreadedSyncAdapter {

    private final String LOG_TAG = FitnessSyncAdapter.class.getSimpleName();
    private static GoogleApiClient mClient = null;

    // Interval at which to sync with the weather, in milliseconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public FitnessSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");

        final String DATE_FORMAT = "yyyy-MM-dd";
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.MONTH, -1);
        long startTime = cal.getTimeInMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Log.i(LOG_TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(LOG_TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .read(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        Log.i(LOG_TAG, "Request Created");
        DataReadResult dataReadResult = Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);

        Vector<ContentValues> contentValuesVector = new Vector<ContentValues>(dataReadResult.getDataSets().size());

        DataSet dataSet = dataReadResult.getDataSets().get(0);

        for(DataPoint dataPoint : dataSet.getDataPoints()) {

            ContentValues fitnessValues = new ContentValues();

            fitnessValues.put(FitnessContract.StepsEntry.COLUMN_STEPS_DATE, dateFormat.format(dataPoint.getStartTime(TimeUnit.MILLISECONDS)));
            fitnessValues.put(FitnessContract.StepsEntry.COLUMN_STEPS_COUNT, dataPoint.getValue(dataPoint.getDataType().getFields().get(0)).asInt());

            contentValuesVector.add(fitnessValues);
        }

        if (contentValuesVector.size() > 0) {
            ContentValues[] contentValueses = new ContentValues[contentValuesVector.size()];
            contentValuesVector.toArray(contentValueses);
            getContext().getContentResolver().bulkInsert(FitnessContract.StepsEntry.CONTENT_URI, contentValueses);
        }

        Log.d(LOG_TAG, "Fitness Synced " + contentValuesVector.size() + " added.");

    }

    public static void initializeSyncAdapter(GoogleApiClient gClient, Context context) {
        Log.i("initializeSyncAdapter", "Initializing Sync Adapter");
        mClient = gClient;
        getSyncAccount(context);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        Bundle bundle = new Bundle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority)
                    .setExtras(bundle)
                    .build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString((R.string.sync_account_type)));

        if (null == accountManager.getPassword(newAccount)) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }

            onAccountCreated(newAccount, context);
        }

        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        FitnessSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        syncImmediately(context);
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }
}
