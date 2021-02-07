package com.example.ponggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PongGame extends SurfaceView implements Runnable {

    private int mScreenX;
    private int mScreenY;
    private int mFontSize;
    private int mFontMargin;
    private int mScore;
    private int mLives;
    private Bat mBat;
    private Ball mBall;
    private SurfaceHolder mOurHolder;
    private Canvas mCanvas;
    private Paint mPaint;
    private final boolean DEBUGGING = true;
    private long mFPS;
    private final int MILLIS_IN_SECOND = 1000;

    private Thread mGameThread = null;
    private volatile boolean mPlaying;
    private boolean mPaused = true;


    public PongGame(Context context, int x, int y) {
        super(context);
        mScreenX = x;
        mScreenY = y;
        mFontSize = mScreenX / 20;
        mFontMargin = mScreenX / 75;
        mOurHolder = getHolder();
        mPaint = new Paint();
        mBall = new Ball(mScreenX);
        startNewGame();
    }

    private void startNewGame(){
        mScore = 0;
        mLives = 3;
        mBall.reset(mScreenX, mScreenY);
    }
    private void draw() {
            if (mOurHolder.getSurface().isValid()) {
                mCanvas = mOurHolder.lockCanvas(); // Lock the canvas (graphics memory)
                mCanvas.drawColor(Color.argb(255, 26, 128, 182));
                mPaint.setColor(Color.argb(255, 255, 255, 255));
                mPaint.setTextSize(mFontSize);
                mCanvas.drawText("Score: " + mScore + " Lives: " + mLives,

                        mFontMargin, mFontSize, mPaint);
                mCanvas.drawRect(mBall.getRect(), mPaint);

                if (DEBUGGING) {
                    printDebuggingText();
                }
                mOurHolder.unlockCanvasAndPost(mCanvas);
            }
    }
    private void printDebuggingText(){
        int debugSize = mFontSize / 2;
        int debugStart = 150;
        mPaint.setTextSize(debugSize);
        mCanvas.drawText("FPS: " + mFPS ,  10, debugStart + debugSize, mPaint);
    }

    @Override
    public void run() {

        while (mPlaying) {

            long frameStartTime = System.currentTimeMillis();
            if(!mPaused){
                update(); // update new positions
                detectCollisions(); // detect collisions
            }
            draw();
            long timeThisFrame = System.currentTimeMillis() - frameStartTime;
            if (timeThisFrame > 0) {
                mFPS = MILLIS_IN_SECOND / timeThisFrame;
            }
        }
    }

    private void detectCollisions() {
    }

    private void update()
    {mBall.update(mFPS);
    }


    public void pause() {

        mPlaying = false;
        try {
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }
        public void resume() {
            mPlaying = true;
            mGameThread = new Thread(this);
            mGameThread.start();

        }
    }


