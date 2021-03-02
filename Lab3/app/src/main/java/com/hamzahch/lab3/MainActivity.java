package com.hamzahch.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnViewListener, SensorEventListener {

    private boolean isGameStarted = false;
    private int seconds = 0;

    private Thread mThread;
    private PlayView mPlayView;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayView = findViewById(R.id.playView);
        mPlayView.setOnDrawListener(this);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onShakeBtnClick(View view) {
        if (!isGameStarted) {
            mPlayView.onStart();
            onGameStart();
        }
    }

    private void updateTextView() {
        TextView textView = findViewById(R.id.textView);
        textView.setText("Elapsed Time (sec): " + seconds);
    }

    @Override
    public void onGameStart() {
        mThread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                seconds++;
                                updateTextView();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        mThread.start();
        isGameStarted = true;
    }

    @Override
    public void onGamePause() {
        mThread.interrupt();
        isGameStarted = false;
    }

    @Override
    public void onGameWin() {
        onGamePause();
        TextView textView = findViewById(R.id.textView);
        textView.setText("Game won after " + seconds + " seconds!");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float ax = event.values[0];
        float ay = event.values[1];
        float az = event.values[2];
        double acc = Math.sqrt(ax * ax + ay * ay + az * az);

        if (acc >= 13) {
            mPlayView.onStart();
            onGameStart();
        }

        if (isGameStarted) {
            if (ax < -1.0)
                mPlayView.moveLeft();
            else if (ax > 1.0)
                mPlayView.moveRight();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}