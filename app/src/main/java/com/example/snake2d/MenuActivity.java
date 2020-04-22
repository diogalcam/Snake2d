package com.example.snake2d;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.media.AudioManager;

import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MenuActivity extends AppCompatActivity {
    private Button button1;
    private TextView textView2;
    private Button button2;

    private ImageButton muteButton;
    private String muteText;

    static SoundPool soundPool;
    int music = -1;
    int i = 0;





    public RemoteMongoCollection<Document> datos;
    ResourceBundle resources;
    StitchAppClient stitchClient;
    List<Document> items = new ArrayList<>();

    //le metemos musica de fondo al menu? taría canelita

    //Para el menú podríamos ponerle un LinearLayout vertical , estaba probando con el ConstraintLayout


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        loadSound();



        //el texto de SNAKE lo he puesto un poco cutre LUL pero se le podría mirar alguna manera de mejorarlo
        //textView2 = (TextView) findViewById(R.id.textView2);
        //textView2.setTextSize(30);
        //textView2.setTextColor(Color.WHITE);

        //el botón de JUGAR(button1) . El otro botón de MARCADORES todavía no hace nada simplemente se muestra
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

        //el botón de marcadores lleva a una pestaña en la que se muestran los 10 o 15 o los que nos salgan de los huevos mejores resultados de la base de datos
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                openActivity3();
            }
        });


        muteButton = (ImageButton) findViewById(R.id.imageButton2);
        muteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                changeMute();
            }
        });


        StitchAppClient client = Stitch.initializeDefaultAppClient("snake2d-deosi");


    }


    public void openActivity2(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        //Si le damos a jugar se para la musica del menu
        soundPool.stop(1);

    }

    public void openActivity3(){
        Intent intent = new Intent(this,Marcador.class);
        startActivity(intent);
        //Podria ponerse aqui tambien para la musica o dejarla nose que es mejor
    }

    //Funcion para manejar el MUTE
    public void changeMute() {
        muteText = muteButton.getTransitionName();
        if (muteText == "muteoff") {
            muteButton.setTransitionName("muteon");
            muteButton.setImageResource(R.drawable.muteon);
            soundPool.pause(1);


        } else {
            muteButton.setTransitionName("muteoff");
            muteButton.setImageResource(R.drawable.muteoff);
            //la primera vez hará play, y la segunda hará reanudar
            if(i==0){
                soundPool.play(music, 1, 1, 0, -1, 1);
                i = i + 1;
            }else{
                soundPool.resume(1);
            }

        }
    }
    public void loadSound(){
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try {
            //requerido para abrir los sonidos
            AssetManager assetManager = getAssets();
            AssetFileDescriptor descriptor;


            descriptor = assetManager.openFd("music.ogg");
            music = soundPool.load(descriptor, 0);

        } catch (IOException e) {

            Log.e("error", "fallo al carga los sonidos");

        }
    }

}
