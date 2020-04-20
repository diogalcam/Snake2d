package com.example.snake2d;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateOptions;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MenuActivity extends AppCompatActivity {
    private Button button1;
    private TextView textView2;
    private Button button2;
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

        //el botón de marcadores lleva a una pestaña en la que se muestran los 10 o 15 o los que nos salgan de los huevos mejores resultados de la base de datos
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                openActivity3();
            }
        });


        //Se conecta a la base de datos y coge lo que hay
        final StitchAppClient client =
                Stitch.initializeDefaultAppClient("snake2d-deosi");

        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "snake");

        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("snake").getCollection("snake");

        datos = coll;

        client.getAuth().loginWithCredential(new AnonymousCredential());


        Document newItem = new Document()
                .append("nombre", "compadre")
                .append("puntos", 40);


        final Task <RemoteInsertOneResult> insertTask = coll.insertOne(newItem);
        insertTask.addOnCompleteListener(new OnCompleteListener <RemoteInsertOneResult> () {
            @Override
            public void onComplete(@NonNull Task <RemoteInsertOneResult> task) {
                if (task.isSuccessful()) {
                    Log.d("app", String.format("successfully inserted item with id %s",
                            task.getResult().getInsertedId()));
                } else {
                    Log.e("app", "failed to insert document with: ", task.getException());
                }
            }
        });


        //FIND
        Document filterDoc = new Document()
                .append("reviews.0", new Document().append("$exists", true));

        RemoteFindIterable findResults = coll
                .find(filterDoc);

        Task <List<Document>> itemsTask = findResults.into(new ArrayList<Document>());
        itemsTask.addOnCompleteListener(new OnCompleteListener <List<Document>> () {
            @Override
            public void onComplete(@NonNull Task<List<Document>> task) {
                if (task.isSuccessful()) {
                    items = task.getResult();
                    Log.d("app", String.format("successfully found %d documents", items.size()));
                    for (Document item: items) {
                        Log.d("app", String.format("successfully found:  %s", item.toString()));
                    }
                } else {
                    Log.e("app", "failed to find documents with: ", task.getException());
                }
            }
        });

    }


    public void openActivity2(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void openActivity3(){
        Intent intent = new Intent(this,Marcador.class);
        startActivity(intent);
    }

}
