package com.hamzahch.lab1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;

    private boolean left, right = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    public void onLeftButtonClick(View view) {
        textView.setText(R.string.left_btn_msg);
        left = !left;
        if (left) ((Button) view).setBackgroundResource(R.drawable.train);
        else {
            view.setBackgroundResource(0);
            view.setBackgroundTintList(getColorStateList(R.color.design_default_color_error));
            view.setBackgroundColor(getColor(R.color.design_default_color_error));
        }
        checkBackground();
    }

    public void onRightButtonClick(View view) {
        textView.setText(R.string.right_btn_msg);
        right = !right;
        if (right) view.setBackgroundResource(R.drawable.train);
        else {
            view.setBackgroundResource(0);
            view.setBackgroundTintList(getColorStateList(R.color.design_default_color_error));
            view.setBackgroundColor(getColor(R.color.design_default_color_error));
        }
        checkBackground();
    }

    public void onSnapButtonClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    private void checkBackground() {
        if (left == right) Toast.makeText(this, R.string.same_bg_msg, Toast.LENGTH_SHORT).show();
    }
}