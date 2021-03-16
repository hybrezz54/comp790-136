package com.hamzahch.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private int c = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.textView);
    }

    public void onClickMain(View view) {
        foo(c);
        c++;
    }

    private void foo(int m) {
        Thread t = new Thread() {
            int seconds = 0;

            @Override
            public void run() {
                try {
                    while (seconds < m) {
                        Thread.sleep(1000);
                        seconds++;
                        Log.v("TAG", seconds + " seconds elapsed");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

}