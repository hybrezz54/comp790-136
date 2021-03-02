package com.hamzahch.lab3;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Bat {

    private int x, y, width, ix;
    private int speed = 2;

    public Bat(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.ix = x;
        this.width = width;
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

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(x, y, width + x, 5 + y, paint);
    }

    public void reset() {
        this.x = this.ix;
    }

    public void moveLeft(int border) {
        if ((x - (width / 4) >= border))
            x -= (width / 4);
    }

    public void moveRight(int border) {
        if ((x + (5 * width / 4) <= border))
            x += (width / 4);
    }

}
