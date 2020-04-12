package com.example.snake2d;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MenuActivity extends AppCompatActivity {
    private Button button1;
    private TextView textView2;
    //le metemos musica de fondo al menu? taría canelita

    //Para el menú podríamos ponerle un LinearLayout vertical , estaba probando con el ConstraintLayout


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //el texto de SNAKE lo he puesto un poco cutre LUL pero se le podría mirar alguna manera de mejorarlo
        textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setTextSize(30);
        textView2.setTextColor(Color.WHITE);

        //el botón de JUGAR(button1) . El otro botón de MARCADORES todavía no hace nada simplemente se muestra
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

    }

    public void openActivity2(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
