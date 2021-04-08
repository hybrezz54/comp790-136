package com.hamzahch.lab7;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions;
import org.tensorflow.lite.task.vision.classifier.Classifications;
import org.tensorflow.lite.task.vision.classifier.ImageClassifier;
import org.tensorflow.lite.task.vision.classifier.ImageClassifier.ImageClassifierOptions;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.min;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Slider slider;
    private EditText editTag;
    private EditText editSize;
    private SQLiteDatabase db;

    private ArrayList<Photo> photos = new ArrayList<>();
    private int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get views
        imageView = findViewById(R.id.imageView);
        slider = findViewById(R.id.slider);
        editTag = findViewById(R.id.editTag);
        editSize = findViewById(R.id.editSize);

        // slider event handler
        slider.addOnChangeListener((slider, value, fromUser) -> {
            int val = (int) value;
            if (val < photos.size())
                updateViewsFromPhoto(val);
        });

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

            try {
                // create classifier instance
                ImageClassifierOptions options = ImageClassifierOptions.builder()
                        .setMaxResults(5).build();
                ImageClassifier classifier = ImageClassifier.createFromFileAndOptions(this,
                        "mobilenet_v1_1.0_224_quant.tflite", options);

                // set props
                TensorImage image = TensorImage.fromBitmap(bitmap);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int cropSize = min(width, height);

                // run inference
                ImageProcessingOptions imageOptions =
                        ImageProcessingOptions.builder()
                                // .setOrientation(getOrientation(sensorOrientation))
                                // Set the ROI to the center of the image.
                                .setRoi(
                                        new Rect(
                                                /*left=*/ (width - cropSize) / 2,
                                                /*top=*/ (height - cropSize) / 2,
                                                /*right=*/ (width + cropSize) / 2,
                                                /*bottom=*/ (height + cropSize) / 2))
                                .build();

                List<Classifications> classifications = classifier.classify(image,
                        imageOptions);
                List<Integer> results = new ArrayList<>();

                classifications.forEach(classification -> {
                    classification.getCategories().forEach(category -> {
                        results.add(Integer.parseInt(category.getLabel()));
                    });
                });

                // set up results
                List<String> labels = new ArrayList<>();
                Collections.sort(results);
                BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("labels_mobilenet_quant_v1_224.txt")));
                int i = 0;

                // iterate over labels
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    if (results.size() <= 0)
                        break;

                    if (i == results.get(0)) {
                        labels.add(line);
                        results.remove(0);
                    }

                    i++;
                }

                br.close();
                editTag.setText(TextUtils.join(";", labels));
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        String tags = TextUtils.join("%' OR tags LIKE '%",
                editTag.getText().toString().split("\\s*;\\s*"));
        String size = editSize.getText().toString();

        if (!tags.equals("") && !size.equals("")) {
            int sizeNum = Integer.parseInt(size);
            int minSize = (int) (0.75 * sizeNum);
            int maxSize = (int) (1.25 * sizeNum);

            Cursor c = db.rawQuery("SELECT * FROM Photos WHERE (tags LIKE '%" +
                            tags + "%') AND size >= " + minSize + " AND size <= " + maxSize + ";",
                    null);
            Log.e("Lab6", "SELECT * FROM Photos WHERE tags LIKE '%" +
                    tags + "%' AND size >= " + minSize + " AND size <= " + maxSize + ";");
            updatePhotosFromResult(c);
        } else if (!tags.equals("")) {
            Cursor c = db.rawQuery("SELECT * FROM Photos WHERE tags LIKE '%" +
                    tags + "%';", null);
            Log.e("Lab6", "SELECT * FROM Photos WHERE tags LIKE '%" +
                    tags + "%';");
            updatePhotosFromResult(c);
        } else if (!size.equals("")) {
            int sizeNum = Integer.parseInt(size);
            int minSize = (int) (0.75 * sizeNum);
            int maxSize = (int) (1.25 * sizeNum);

            // retrieve data from db
            Cursor c = db.rawQuery("SELECT * FROM Photos WHERE size >= " + minSize +
                    " AND size <= " + maxSize + ";", null);
            updatePhotosFromResult(c);
        }
    }

    public void onCapture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    private void updatePhotosFromResult(Cursor c) {
        photos.clear();

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String t = c.getString(1);
                int s = c.getInt(2);
                byte[] ba = c.getBlob(3);

                Photo p = new Photo(t, s, ba);
                photos.add(p);
            }

            // update views
            slider.setValueTo(photos.size());
            updateViewsFromPhoto(0);
        } else {
            slider.setValue(0);
            slider.setValueTo(1);
            imageView.setImageBitmap(null);
        }
    }

    private void updateViewsFromPhoto(int index) {
        Photo p = photos.get(index);
        imageView.setImageBitmap(p.getImageBitmap());
        editTag.setText(p.getTags());
        editSize.setText("" + p.getSize());
    }

}