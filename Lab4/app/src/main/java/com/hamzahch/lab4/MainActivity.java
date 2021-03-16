package com.hamzahch.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
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
        new UpdateTextViewTask().execute(m);
    }

    private class UpdateTextViewTask extends AsyncTask<Integer, String, Void> {

        private int seconds = 0;

        @Override
        protected Void doInBackground(Integer... nums) {
            try {
                while (seconds < nums[0]) {
                    Thread.sleep(1000);
                    seconds++;
                    publishProgress(seconds + " seconds elapsed");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            mTextView.setText(values[0]);
        }
    }

}

