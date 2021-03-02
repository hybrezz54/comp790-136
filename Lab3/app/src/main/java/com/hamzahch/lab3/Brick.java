package com.hamzahch.lab3;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Brick {

    private int x, y, width, height;
    private boolean isCollided = false;

    public Brick(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(x, y, width + x, height + y, paint);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean getCollided() {
        return isCollided;
    }

    public void collide() {
        isCollided = true;
    }

}
