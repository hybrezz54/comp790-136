package com.hamzahch.lab6;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText editTag;
    private EditText editSize;
    private SQLiteDatabase db;

    private int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get views
        imageView = findViewById(R.id.imageView);
        editTag = findViewById(R.id.editTag);
        editSize = findViewById(R.id.editSize);

        // setup db
        db = openOrCreateDatabase("PhotoDB", Context.MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS Photos");
        db.execSQL("CREATE TABLE Photos (id INT PRIMARY KEY, tags TEXT, size INTEGER, " +
                "data BLOB)");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);

            size = bitmap.getByteCount();
            editSize.setText("" + size);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.execSQL("DROP TABLE IF EXISTS Photos");
    }

    public void onSave(View view) {
        if (size > 0) {
            // get byte stream
            Bitmap b = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] ba = stream.toByteArray();

            // add to db
            ContentValues cv = new ContentValues();
            cv.put("tags", editTag.getText().toString());
            cv.put("size", size);
            cv.put("data", ba);
            db.insert("Photos", null, cv);
        }

        try (Cursor c = db.rawQuery("SELECT * FROM photos;", null)) {
            while (c.moveToNext()) {
                Log.v("Lab6", "" + c.getInt(0) + ": " +
                        c.getString(1) + ", " + c.getInt(2) + ", " +
                        c.getBlob((3)));
            }
        }
    }

    public void onLoad(View view) {
        String tags = editTag.getText().toString();
        String size = editSize.getText().toString();

        if (!tags.equals("") && !size.equals("")) {
            int sizeNum = Integer.parseInt(size);
            int minSize = (int) (0.75 * sizeNum);
            int maxSize = (int) (1.25 * sizeNum);

            Cursor c = db.rawQuery("SELECT * FROM Photos WHERE tags LIKE '" +
                    tags + "' AND size >= " + minSize + " AND size <= " + maxSize + ";",
                    null);
            updateViewFromResult(c);
        } else if (!tags.equals("")) {
            Cursor c = db.rawQuery("SELECT * FROM Photos WHERE tags LIKE '" +
                   tags + "';", null);
            updateViewFromResult(c);
        } else if (!size.equals("")) {
            int sizeNum = Integer.parseInt(size);
            int minSize = (int) (0.75 * sizeNum);
            int maxSize = (int) (1.25 * sizeNum);

            // retrieve data from db
            Cursor c = db.rawQuery("SELECT * FROM Photos WHERE size >= " + minSize +
                    " AND size <= " + maxSize + ";", null);
            updateViewFromResult(c);
        }
    }

    public void onCapture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    private void updateViewFromResult(Cursor c) {
        if (c.getCount() > 0) {
            c.moveToFirst();
            String t = c.getString(1);
            int s = c.getInt(2);
            byte[] ba = c.getBlob(3);

            // update views
            Bitmap b = BitmapFactory.decodeByteArray(ba, 0, ba.length);
            imageView.setImageBitmap(b);
            editTag.setText(t);
            editSize.setText("" + s);
        } else {
            imageView.setImageBitmap(null);
        }
    }

}