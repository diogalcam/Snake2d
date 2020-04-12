package com.example.snake2d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainActivity extends AppCompatActivity {

    //esta clase habría que tocarla no me gusta como está

    Canvas canvas;
    SnakeAnimView snakeAnimView;

    Bitmap headAnimBitmap;
    Rect rectToBeDrawn;

    //Las dimensiones de un frame
    int frameHeight = 64;
    int frameWidth = 64;
    int numFrames  = 6;
    int frameNumber;

    int screenWidth;
    int screenHeight;

    long lastFrameTime;
    int fps;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        headAnimBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.head_sprite_sheet);

        snakeAnimView = new SnakeAnimView(this);
        setContentView(snakeAnimView);

        i = new Intent(this,GameActivity.class);


    }
    public class SnakeAnimView extends SurfaceView implements Runnable
    {
        Thread ourThread = null;
        SurfaceHolder ourHolder;
        volatile boolean playingSnake;
        Paint paint;

        public SnakeAnimView(Context context) {
            super(context);
            ourHolder = getHolder();
            paint = new Paint();
            frameHeight= headAnimBitmap.getHeight();
            frameWidth = headAnimBitmap.getWidth();


        }

        @Override
        public void run() {
            while (playingSnake){
                update();
                Draw();
                controlFPS();
            }
        }

        private void controlFPS() {

            long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
            long timeToSleep = 500-timeThisFrame;

            if (timeThisFrame > 0){
                fps = (int)(1000/ timeThisFrame);
            }
            if (timeToSleep > 0){
                try {
                    ourThread.sleep(timeToSleep);
                }catch (InterruptedException e){

                }
            }
            lastFrameTime = System.currentTimeMillis();
        }

        private void Draw() {

            if (ourHolder.getSurface().isValid()){
                canvas = ourHolder.lockCanvas();
                canvas.drawColor(Color.BLACK);//background color
                paint.setColor(Color.argb(255,255,255,255));
                paint.setTextSize(50);
                canvas.drawText("Pulsa para entrar en partida..",10,150,paint);
                paint.setTextSize(25);


                ourHolder.unlockCanvasAndPost(canvas);


            }
        }

        private void update() {

            //which frame should be drawn
            rectToBeDrawn = new Rect((frameNumber * frameWidth) - 1,0,
                    (frameNumber * frameWidth+frameWidth)-1,frameHeight);

            //now the next frame
            frameNumber++;


            //don't try and draw frames that don't exist
            if (frameNumber == numFrames){
                frameNumber = 0;//back to the first frame
            }
        }


        public void pause(){
            playingSnake = false;
            try {
                ourThread.join();
            }catch (InterruptedException e){

            }
        }

        public void resume(){
            playingSnake = true;
            ourThread = new Thread(this);
            ourThread.start();
        }



//la partida es empezada cuando se toca la pantalla con onTouchEvent

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            startActivity(i);
            return true;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        while (true){
            snakeAnimView.pause();
            break;
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        snakeAnimView.resume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        snakeAnimView.pause();
    }



    public boolean onKeyDwon(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){
            snakeAnimView.pause();
            finish();
            return true;
        }
        return false;
    }
}
