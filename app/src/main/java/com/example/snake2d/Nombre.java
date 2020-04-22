package com.example.snake2d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Nombre extends AppCompatActivity {
    private TextView textView;
    private EditText editText;
    private Button buttonName;
    public String nombreARecuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nombre);

        textView = (TextView) findViewById(R.id.textViewName);
        textView.setText("Ingrese su nombre..");
        textView.setTextSize(30);

        editText = (EditText) findViewById(R.id.editText);


        buttonName = (Button) findViewById(R.id.buttonName);
        buttonName.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                nombreARecuperar = editText.getText().toString();
                Log.v("EditText", nombreARecuperar);
                openActivity();
            }
        });
    }

    public void openActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("nombreARecuperar",nombreARecuperar);
        startActivity(intent);

    }
}
