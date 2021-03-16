package com.hamzahch.lab5;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class IncrementService extends Service {

    public IncrementService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() != null) {
            int n = intent.getExtras().getInt("N");
            Toast.makeText(getApplicationContext(), "Number received from activity: " + n,
                    Toast.LENGTH_SHORT).show();
        }

        return START_NOT_STICKY;
    }
}