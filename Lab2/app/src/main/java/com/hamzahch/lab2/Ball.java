package com.hamzahch.lab2;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class Ball {

    private int x, y, dx, dy;
    private int r = 5;
    private int speed = 2;
    private boolean isCollided = false;

    public Ball(int x, int y) {
        this(x, y, 0, 0);
    }

    public Ball(int x, int y, int cx, int cy) {
        this.x = x;
        this.y = y;
        this.dx = cx;
        this.dy = cy;
    }

    public void generateSpeed() {
        Random r = new Random();
        dx = r.nextBoolean() ? speed : -speed;
        dy = r.nextBoolean() ? speed : -speed;
    }

    public void resetCollided() {
        isCollided = false;
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

    public boolean getCollided() { return isCollided; }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawCircle(x, y, r, paint);
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public void collide(int dx, int dy) {
        isCollided = true;
        this.dx = dx;
        this.dy = dy;
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
