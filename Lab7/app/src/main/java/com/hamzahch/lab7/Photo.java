package com.hamzahch.lab7;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class Photo {

    private String tags;
    private int size;
    private byte[] image;

    public Photo(String tags, int size) {
        this.tags = tags;
        this.size = size;
    }

    public Photo(String tags, int size, byte[] image) {
        this.tags = tags;
        this.size = size;
        this.image = image;
    }

    public void setImageBitmap(Bitmap b) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, stream);
        image = stream.toByteArray();
    }

    public String getTags() { return tags; }

    public int getSize() { return size; }

    public byte[] getImage() { return image; }

    public Bitmap getImageBitmap() {
        Bitmap b = BitmapFactory.decodeByteArray(image, 0, image.length);
        return b;
    }

}
