package com.hamzahch.lab2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BallsView extends View {

    private ArrayList<Ball> balls = new ArrayList<>(100);
    private OnDrawListener onDrawListener;
    private Canvas canvas;

    // rect attrs
    private int rectX, rectY, rectWidth, rectHeight;

    public BallsView(Context context) {
        super(context);
    }

    public BallsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BallsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BallsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // get attrs
        rectX = 10;
        rectY = 10;
        rectWidth = getWidth() - rectX;
        rectHeight = getHeight() - rectY;

        // draw rect
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);
        canvas.drawRect(rectX, rectY, rectWidth, rectHeight, paint);

        // draw balls
        for (Ball b : balls) {
            Paint ballPaint = new Paint();
            ballPaint.setColor(Color.BLUE);
            ballPaint.setStyle(Paint.Style.FILL);

            int x = b.getX();
            int y = b.getY();
            int r = b.getRadius();
            int dx = b.getDx();
            int dy = b.getDy();

            List<Ball> collided = balls.stream().filter(ball -> b != ball && b.getDistance(ball.getX(), ball.getY()) < (r + ball.getRadius())).collect(Collectors.toList());
            if (collided.size() > 0) {
                collided.forEach(ball -> {
                    if (!ball.getCollided()) {
                        b.collide(ball.getDx(), ball.getDy());
                        ball.collide(dx, dy);
                    }
                });
            }

            if (x <= (rectX + r) || x >= (rectWidth - r))
                b.collideX();

            if (y <= (rectY + r) || y >= (rectHeight - r))
                b.collideY();

            b.move();
            b.draw(canvas, ballPaint);
        }

        AtomicInteger counter = new AtomicInteger();
        balls.forEach(ball -> {
            if (ball.getCollided()) {
                ball.resetCollided();
                counter.getAndIncrement();
            }
        });
        onDrawListener.onCollide(counter.get() / 2);

        invalidate();
    }

    public void addBall() {
        if (balls.size() < 100) {
            Ball ball = new Ball((rectWidth + rectX) / 2, (rectHeight + rectY) / 2);
            ball.generateSpeed();
            balls.add(ball);
            invalidate();
        }
    }

    public void setOnDrawListener(OnDrawListener listener) {
        onDrawListener = listener;
    }

}

interface OnDrawListener {
    public void onCollide(int collisions);
}
