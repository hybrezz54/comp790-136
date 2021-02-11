package com.hamzahch.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements OnDrawListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BallsView ballsView = findViewById(R.id.ballsView);
        ballsView.setOnDrawListener(this);
    }

    public void onAddBtnClick(View view) {
        BallsView ballsView = findViewById(R.id.ballsView);
        ballsView.addBall();
    }

    @Override
    public void onDraw(int counter) {
        TextView textView = findViewById(R.id.textView);
        textView.setText(textView.getText() + " " + counter);
    }
}