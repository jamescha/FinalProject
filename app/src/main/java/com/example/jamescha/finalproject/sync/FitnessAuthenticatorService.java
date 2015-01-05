package com.example.jamescha.finalproject.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by jamescha on 1/2/15.
 */
public class FitnessAuthenticatorService extends Service{
    private FitnessAuthenticator mFitnessAuthenticator;

    @Override
    public void onCreate() {
        mFitnessAuthenticator = new FitnessAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mFitnessAuthenticator.getIBinder();
    }
}
