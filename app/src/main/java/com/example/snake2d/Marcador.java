package com.example.snake2d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteDeleteResult;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class Marcador extends AppCompatActivity {
    String nombre;
    Integer puntuacion;
    public RemoteMongoCollection<Document> datos;
    ResourceBundle resources;
    StitchAppClient stitchClient;
    List<Document> items = new ArrayList<>();
    ArrayList objeto = new ArrayList();
    StitchAppClient client = Stitch.getAppClient("snake2d-deosi");

    RemoteMongoClient mongoClient =
            client.getServiceClient(RemoteMongoClient.factory, "snake");

    RemoteMongoCollection<Document> coll =
            mongoClient.getDatabase("snake").getCollection("snake");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcador);

        //Se conecta a la base de datos y coge lo que hay


        //datos = coll;

        client.getAuth().loginWithCredential(new AnonymousCredential());


        //FIND
        Document filterDoc = new Document()
                .append("reviews.0", new Document().append("$exists", true));

        RemoteFindIterable findResults = coll
                .find(filterDoc);

        Task<List<Document>> itemsTask = findResults.into(new ArrayList<Document>());
        itemsTask.addOnCompleteListener(new OnCompleteListener<List<Document>>() {
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

                HashMap<String,Integer> myMap = new HashMap<String,Integer>();
                for(Document item:items){
                    nombre = (String) item.get("nombre");
                    puntuacion = (Integer) item.get("puntos");
                    myMap.put(nombre,puntuacion);

                }
                //llamamos a la clase ValueComparator que extiende a comparable para ordenar por los valores del mapa
                ValueComparator bvc =  new ValueComparator(myMap);

                TreeMap<String,Integer> mapaOrdenado = new TreeMap<String,Integer>(bvc);
                mapaOrdenado.putAll(myMap);

                List<String> nombres = new ArrayList<>();
                List<Integer> puntuacion = new ArrayList<>();
                for(String clave: mapaOrdenado.keySet()){
                    Integer puntos = mapaOrdenado.get(clave);
                    Log.v("tag","Nombre->" + clave +" Puntuación->" +puntos);

                    nombres.add(clave);
                    puntuacion.add(puntos);

                }
                Log.v("tag", String.valueOf(nombres));
                Log.v("tag", String.valueOf(puntuacion));
            }
        });

    }


}

