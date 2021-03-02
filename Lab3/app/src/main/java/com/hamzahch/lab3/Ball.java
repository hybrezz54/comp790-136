package com.hamzahch.lab3;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class Ball {

    private int x, y, dx, dy, r, ix, iy;
    private int speed = 3;

    public Ball(int x, int y, int r) {
        this(x, y, r, 0, 0);
    }

    public Ball(int x, int y, int r, int cx, int cy) {
        this.x = x;
        this.ix = x;
        this.y = y;
        this.iy = y;
        this.r = r;
        this.dx = cx;
        this.dy = cy;
    }

    public void generateSpeed() {
        Random r = new Random();
        dx = r.nextBoolean() ? speed : -speed;
        dy = r.nextBoolean() ? speed : -speed;
    }

    public void reset() {
        this.dx = 0;
        this.dy = 0;
        this.x = ix;
        this.y = iy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDx() { return dx; }

    public int getDy() { return dy; }

    public int getRadius() {
        return r;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawCircle(x, y, r, paint);
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public void collide() {
        this.dx = -dx;
        this.dy = -dy;
    }

    public void collideX() {
        dx = -dx;
    }

    public void collideY() {
        dy = -dy;
    }

    public double getDistance(int x, int y) {
        return Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
    }

}
