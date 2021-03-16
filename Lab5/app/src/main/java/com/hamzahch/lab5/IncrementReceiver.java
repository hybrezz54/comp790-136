package com.hamzahch.lab5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

public class IncrementReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            int n = intent.getExtras().getInt("N");

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Number received from broadcast: " + n,
                            Toast.LENGTH_LONG).show();
                }
            }, 4000);
        }
    }
}