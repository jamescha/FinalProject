package com.example.jamescha.finalproject.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FitnessSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static FitnessSyncAdapter sFitnessSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("FitnessSyncService", "onCreate - FitnessSyncService");
        synchronized (sSyncAdapterLock) {
            if(sFitnessSyncAdapter == null) {
                sFitnessSyncAdapter = new FitnessSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sFitnessSyncAdapter.getSyncAdapterBinder();
    }
}
