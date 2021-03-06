package com.hamzahch.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // generate random number
        int n =  new Random().nextInt(10);

        // set it to textview
        TextView textView = findViewById(R.id.textView);
        textView.setText("Number generated by activity: " + n);

        // create intent for service
        Intent serviceIntent = new Intent(this, IncrementService.class);
        serviceIntent.putExtra("N", n);
        startService(serviceIntent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // stop service
        Intent serviceIntent = new Intent(this, IncrementService.class);
        stopService(serviceIntent);
    }
}