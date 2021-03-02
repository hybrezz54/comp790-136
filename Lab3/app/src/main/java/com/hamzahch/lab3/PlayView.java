package com.hamzahch.lab3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PlayView extends View {

    private Ball ball;
    private Bat bat;
    private ArrayList<Brick> bricks = new ArrayList<>(10);
    private OnViewListener listener;
    private Canvas canvas;

    // rect attrs
    private int rectX, rectY, rectWidth, rectHeight;

    public PlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        for (int i = 0; i < 10; i++) {
            int row = i / 5;
            int col = i % 5;

            Brick brick = new Brick(90 + 120 * col, 75 + 70 * row, 100, 50);
            bricks.add(brick);
        }
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

        // create ball
        if (ball == null)
            ball = new Ball((rectWidth + rectX) / 2, ((rectHeight + rectY) / 2) + 100, 7);

        // create bat
        if (bat == null)
            bat = new Bat(((rectWidth + rectX) / 2) - 75, (rectHeight + rectY - 20), 150);

        int ballX = ball.getX();
        int ballY = ball.getY();
        int ballR = ball.getRadius();
        int batX = bat.getX();
        int batY = bat.getY();
        int batX2 = batX + bat.getWidth();

        // border collision
        if (ballX <= (rectX + ballR) || ballX >= (rectWidth - ballR))
            ball.collideX();

        if (ballY <= (rectY + ballR))
            ball.collideY();
        else if (ballY >= (rectHeight - ballR)) {
            reset();
            listener.onGamePause();
        }

        // bat collision
        if (ballX >= (batX - ballR) && ballX <= (batX2 + ballR) && ballY >= (batY - ballR))
            ball.collideY();

        // draw bricks
        for (Brick b : bricks) {
            Paint brickPaint = new Paint();
            brickPaint.setColor(Color.BLACK);
            brickPaint.setStyle(Paint.Style.FILL);
            b.draw(canvas, brickPaint);

            int x = b.getX();
            int x2 = x + b.getWidth();
            int y = b.getY();
            int y2 = y + b.getHeight();

            if (ballX >= (x - ballR) && ballX <= (x2 + ballR) &&
                    ballY >= (y - ballR) && ballY <= (y2 + ballR)) {
                b.collide();

                if (ballX <= x || ballX >= x2)
                    ball.collideX();

                if (ballY <= y || ballY >= y2)
                    ball.collideY();
            }
        }

        // remove bricks that were collided with
        bricks = new ArrayList<Brick>(bricks.stream().filter(b -> !b.getCollided()).collect(Collectors.toList()));
        if (bricks.size() == 0) {
            reset();
            listener.onGameWin();
        }

        // draw ball
        Paint ballPaint = new Paint();
        ballPaint.setColor(Color.BLUE);
        ballPaint.setStyle(Paint.Style.FILL);
        ball.move();
        ball.draw(canvas, ballPaint);

        // draw bat
        Paint batPaint = new Paint();
        ballPaint.setColor(Color.GREEN);
        ballPaint.setStyle(Paint.Style.FILL);
        bat.draw(canvas, batPaint);

        invalidate();
    }

//    public void addBall() {
//        if (balls.size() < 100) {
//            Ball ball = new Ball((rectWidth + rectX) / 2, (rectHeight + rectY) / 2);
//            ball.generateSpeed();
//            balls.add(ball);
//            invalidate();
//        }
//    }

    public void setOnDrawListener(OnViewListener listener) {
        this.listener = listener;
    }

    public void reset() {
        ball.reset();
        bat.reset();
    }

    public void onStart() {
        ball.generateSpeed();
    }

    public void moveLeft() {
        bat.moveLeft(rectX);
    }

    public void moveRight() {
        bat.moveRight(rectX + rectWidth);
    }

}

interface OnViewListener {

    void onGameStart();

    void onGamePause();

    void onGameWin();

}
