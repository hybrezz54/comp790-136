package com.hamzahch.lab2;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity implements OnDrawListener {

    private int collisions = 0;
    private int seconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BallsView ballsView = findViewById(R.id.ballsView);
        ballsView.setOnDrawListener(this);
        updateTextView();

        Thread thread = new Thread() {

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

        thread.start();
    }

    public void onAddBtnClick(View view) {
        BallsView ballsView = findViewById(R.id.ballsView);
        ballsView.addBall();
        collisions = 0;
        seconds = 0;
        updateTextView();
    }

    private void updateTextView() {
        TextView textView = findViewById(R.id.textView);
        textView.setText("Collisions: " + collisions + " (in " + seconds + " seconds)");

    }

    @Override
    public void onCollide(int collisions) {
        this.collisions += collisions;
        updateTextView();
    }
}