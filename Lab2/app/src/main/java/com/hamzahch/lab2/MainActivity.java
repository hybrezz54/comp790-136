package com.hamzahch.lab2;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements OnDrawListener {

    int collisions = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BallsView ballsView = findViewById(R.id.ballsView);
        ballsView.setOnDrawListener(this);
        updateTextView();
    }

    public void onAddBtnClick(View view) {
        BallsView ballsView = findViewById(R.id.ballsView);
        ballsView.addBall();
        collisions = 0;
        updateTextView();
    }

    @Override
    public void onCollide() {
        collisions++;
        updateTextView();
    }

    private void updateTextView() {
        TextView textView = findViewById(R.id.textView);
        textView.setText("Collisions " + collisions);
    }
}