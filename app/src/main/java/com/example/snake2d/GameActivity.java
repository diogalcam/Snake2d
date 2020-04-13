package com.example.snake2d;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    //EN ESTA CLASE HE ARREGLADO VARIAS COSAS PERO AÚN HAY COSAS POR ARREGLAR , LA LÍNEA DE ABAJO BLANCA NO SALE NO HE CONSEGUIDO
    //ARREGLARLO .
    // Y TAMBIÉN ME GUSTARÍA PONER LA MANZANA MÁS GRANDE PERO NO LO HE CONSEGUIDO
    //ARREGLADO YA LUL
    Canvas canvas;
    SnakeView snakeView;

    Bitmap headBitmap;
    Bitmap bodyBitmap;
    Bitmap tailBitmap;
    Bitmap appleBitmap;

    //variables para los sonidos , se inicializa a -1 por si no existen o hay error
    private SoundPool soundPool;
    int sample1 = -1;
    int sample2 = -1;
    int sample3 = -1;
    int sample4 = -1;

    //para el movimiento de la serpiente
    int directionOfTravel=0;
    //0 = arriba, 1 = derecha, 2 = abajo , 3= izquierda


    int screenWidth;
    int screenHeight;
    int topGap;

    long lastFrameTime;
    int fps;
    int score;


    int [] snakeX;
    int [] snakeY;
    int snakeLength;
    int appleX;
    int appleY;


    int blockSize;
    int numBlocksWide;
    int numBlocksHigh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadSound();
        configureDisplay();
        snakeView = new SnakeView(this);
        setContentView(snakeView);

    }

    //la interfaz runnable vamos a usarla para manejar los hilos cuando se sale del juego , paramos, pausamos ,etc.
    public class SnakeView extends SurfaceView implements Runnable {
        Thread ourThread = null;
        SurfaceHolder ourHolder;
        volatile boolean playingSnake;
        Paint paint;

        public SnakeView(Context context) {
            super(context);
            ourHolder = getHolder();
            paint = new Paint();

            snakeX = new int[200];
            snakeY = new int[200];

            getSnake();
            getApple();
        }

        public void getSnake(){
            snakeLength = 3;
            //La cabeza de la serpiente empieza en el medio
            snakeX[0] = numBlocksWide/2;
            snakeY[0] = numBlocksHigh /2;

            //Luego por detrás viene el cuerpo
            snakeX[1] = snakeX[0]-1;
            snakeY[1] = snakeY[0];

            //y luego la cola
            snakeX[1] = snakeX[1]-1;
            snakeY[1] = snakeY[0];
        }

        public void getApple(){
            Random random = new Random();
            appleX = random.nextInt(numBlocksWide-1)+1;
            appleY = random.nextInt(numBlocksHigh-1)+1;
        }


        public void run() {
            while (playingSnake) {
                updateGame();
                drawGame();
                controlFPS();

            }

        }

        public void updateGame() {

            //Si el la serpiente ha cogido una manzana , aumenta de tamaño y aparece otra manzana random en el mapa , además aumenta la puntuación
            if(snakeX[0] == appleX && snakeY[0] == appleY){
                snakeLength++;
                getApple();
                score = score + snakeLength;
                soundPool.play(sample1, 1, 1, 0, 0, 1);
            }

            //Mover el cuerpo empezando desde atrás, la posición que ocupa el último bloque se va a poner en el penúltimo bloque ,
            //y así con todos
            for(int i=snakeLength; i >0 ; i--){
                snakeX[i] = snakeX[i-1];
                snakeY[i] = snakeY[i-1];
            }

            //Mueve desde la cabeza
            switch (directionOfTravel){
                case 0://up
                    snakeY[0]  --;
                    break;

                case 1://right
                    snakeX[0] ++;
                    break;

                case 2://down
                    snakeY[0] ++;
                    break;

                case 3://left
                    snakeX[0] --;
                    break;
            }


            boolean dead = false;
            //si nos estampamos contra la pared
            if(snakeX[0] == -1)dead=true;
            if(snakeX[0] >= numBlocksWide)dead=true;
            if(snakeY[0] == -1)dead=true;
            if(snakeY[0] == numBlocksHigh-6)dead=true;
            //if(snakeX[0] == (numBlocksHigh*blockSize) && snakeY[0] == (numBlocksHigh*blockSize)) dead=true;
            //si nos comemos a nosotros mismos
            for (int i = snakeLength-1; i > 0; i--) {
                if ((i > 4) && (snakeX[0] == snakeX[i]) && (snakeY[0] == snakeY[i])) {
                    dead = true;
                }
            }


            if(dead){
                //si muere empezamos de nuevo
                //aquí podriamos crear otra activity con dos botones para cuando muera , que se pause la actividad de GameActivity
                //y elegir si empezar la partida de nuevo o no.
                soundPool.play(sample4, 1, 1, 0, 0, 1);
                score = 0;
                getSnake();

            }

        }

        public void drawGame() {

            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();
                //el fondo de negro
                canvas.drawColor(Color.BLACK);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(topGap/2);
                canvas.drawText("Score:" + score, 10, topGap-6, paint);

                //para dibujar las 4 líneas blancas
                //habría que tocar la línea de abajo que se sobre sale
                paint.setStrokeWidth(8);
                canvas.drawLine(1,topGap,screenWidth-1,topGap,paint);
                canvas.drawLine(screenWidth-1,topGap,screenWidth-1,topGap+(numBlocksHigh*blockSize),paint);
                canvas.drawLine(screenWidth-1,(numBlocksHigh*blockSize)-50,1,(numBlocksHigh*blockSize)-50,paint);
                canvas.drawLine(1,topGap, 1,topGap+(numBlocksHigh*blockSize), paint);

                //dibuja la serpiente
                canvas.drawBitmap(headBitmap, snakeX[0]*blockSize, (snakeY[0]*blockSize)+topGap, paint);
                //dibuja el cuerpo
                for(int i = 1; i < snakeLength-1;i++){
                    canvas.drawBitmap(bodyBitmap, snakeX[i]*blockSize, (snakeY[i]*blockSize)+topGap, paint);
                }
                //dibuja la cola
                canvas.drawBitmap(tailBitmap, snakeX[snakeLength-1]*blockSize, (snakeY[snakeLength-1]*blockSize)+topGap, paint);

                //dibuja la manzana
                canvas.drawBitmap(appleBitmap, appleX*blockSize, (appleY*blockSize)+topGap, paint);

                ourHolder.unlockCanvasAndPost(canvas);
            }

        }

        //es un juego basado en frames entonces tenemos que controlar los frames por segundo(fps)
        public void controlFPS() {
            long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
            long timeToSleep = 100 - timeThisFrame;
            if (timeThisFrame > 0) {
                fps = (int) (1000 / timeThisFrame);
            }
            if (timeToSleep > 0) {
                try {
                    ourThread.sleep(timeToSleep);
                } catch (InterruptedException e) {
                    Log.e("error", "fallo");
                }

            }

            lastFrameTime = System.currentTimeMillis();
        }


        public void pause() {
            playingSnake = false;
            try {
                ourThread.join();
            } catch (InterruptedException e) {
            }

        }

        public void resume() {
            playingSnake = true;
            ourThread = new Thread(this);
            ourThread.start();
        }


        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    //comprueba la coordenada X donde se está pulsando
                    if (motionEvent.getX() >= screenWidth / 2) {
                        directionOfTravel ++;
                        if(directionOfTravel == 4) {
                            directionOfTravel = 0;
                        }
                    } else {
                        directionOfTravel--;
                        if(directionOfTravel == -1) {
                            directionOfTravel = 3;
                        }
                    }
            }
            return true;
        }


    }

    @Override
    protected void onStop() {
        super.onStop();

        while (true) {
            snakeView.pause();
            break;
        }

        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        snakeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        snakeView.pause();
    }

    //para volver al menú le damos para atrás y volvemos a la clase Main
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            snakeView.pause();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return false;
    }

    public void loadSound(){
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try {
            //requerido para abrir los sonidos
            AssetManager assetManager = getAssets();
            AssetFileDescriptor descriptor;


            descriptor = assetManager.openFd("sample1.ogg");
            sample1 = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("sample2.ogg");
            sample2 = soundPool.load(descriptor, 0);


            descriptor = assetManager.openFd("sample3.ogg");
            sample3 = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("sample4.ogg");
            sample4 = soundPool.load(descriptor, 0);


        } catch (IOException e) {

            Log.e("error", "fallo al carga los sonidos");

        }
    }

    public void configureDisplay(){
        //pilla el ancho y alto de la pantalla
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        topGap = screenHeight/20;

        //para determinar el tamaño de cada bloque en la pantalla
        blockSize = screenWidth/40;

        //Determina cuantos bloques habran en altura y anchura
        //le restamos topGap porque es ahí donde pondremos el score
        numBlocksWide = 40; //podríamos haber puesto screenWeeight-topGap/bs , pero así va flama también
        numBlocksHigh = ((screenHeight - topGap ))/blockSize;

        //decodificamos las imagenes que tenemos de la cabeza , cuerpo , cola y manzana
        headBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head);
        bodyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.body);
        tailBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tail);
        appleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.apple2);

        //escala las imagenes de bitmap al tamaño de los bloques que habíamos calculado anteriormente
        headBitmap = Bitmap.createScaledBitmap(headBitmap, blockSize, blockSize, false);
        bodyBitmap = Bitmap.createScaledBitmap(bodyBitmap, blockSize, blockSize, false);
        tailBitmap = Bitmap.createScaledBitmap(tailBitmap, blockSize, blockSize, false);
        appleBitmap = Bitmap.createScaledBitmap(appleBitmap, blockSize+20, blockSize+20, false);

    }
}
