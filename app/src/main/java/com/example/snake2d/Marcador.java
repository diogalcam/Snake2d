package com.example.snake2d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
    private TextView textView,textView2,textView3,textView4,textView5,textView6,textView8,textView9,textView10,textView11,textView12,textView13,
            textView14,textView15,textView16,textView17,textView18,textView19,textView20,textView21,textView22,textView23;
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

                    //dos listas separadas sacadas del MAP ordenado para después mostrarlas
                    nombres.add(clave);
                    puntuacion.add(puntos);

                }

                textView = (TextView) findViewById(R.id.textView);
                textView.setText("Nombres");
                textView.setTextColor(Color.parseColor("#008f39"));
                textView.setTextSize(40);

                textView8 = (TextView) findViewById(R.id.textView8);
                textView8.setText("Puntuación");
                textView8.setTextColor(Color.parseColor("#008f39"));
                textView8.setTextSize(40);

                //nombre con mayor puntuación empieza en la primera posición porque la lista está ordenada de mayor a menor
                textView2 = (TextView) findViewById(R.id.textView2);
                textView2.setText("1- "+nombres.get(0));
                textView2.setTextColor(Color.parseColor("#FFFFFF"));
                textView2.setTextSize(25);

                textView9 = (TextView) findViewById(R.id.textView9);
                textView9.setText(""+puntuacion.get(0));
                textView9.setTextColor(Color.parseColor("#FFFFFF"));
                textView9.setTextSize(25);

                textView3 = (TextView) findViewById(R.id.textView3);
                textView3.setText("2- "+nombres.get(1));
                textView3.setTextColor(Color.parseColor("#FFFFFF"));
                textView3.setTextSize(25);

                textView10 = (TextView) findViewById(R.id.textView10);
                textView10.setText(""+puntuacion.get(1));
                textView10.setTextColor(Color.parseColor("#FFFFFF"));
                textView10.setTextSize(25);

                textView4 = (TextView) findViewById(R.id.textView4);
                textView4.setText("3- "+nombres.get(2));
                textView4.setTextColor(Color.parseColor("#FFFFFF"));
                textView4.setTextSize(25);

                textView11 = (TextView) findViewById(R.id.textView11);
                textView11.setText(""+puntuacion.get(2));
                textView11.setTextColor(Color.parseColor("#FFFFFF"));
                textView11.setTextSize(25);

                textView5 = (TextView) findViewById(R.id.textView5);
                textView5.setText("4- "+nombres.get(3));
                textView5.setTextColor(Color.parseColor("#FFFFFF"));
                textView5.setTextSize(25);

                textView12 = (TextView) findViewById(R.id.textView12);
                textView12.setText(""+puntuacion.get(3));
                textView12.setTextColor(Color.parseColor("#FFFFFF"));
                textView12.setTextSize(25);

                textView6 = (TextView) findViewById(R.id.textView6);
                textView6.setText("5- "+nombres.get(4));
                textView6.setTextColor(Color.parseColor("#FFFFFF"));
                textView6.setTextSize(25);

                textView13 = (TextView) findViewById(R.id.textView13);
                textView13.setText(""+puntuacion.get(4));
                textView13.setTextColor(Color.parseColor("#FFFFFF"));
                textView13.setTextSize(25);

                textView14 = (TextView) findViewById(R.id.textView14);
                textView14.setText("6- "+nombres.get(5));
                textView14.setTextColor(Color.parseColor("#FFFFFF"));
                textView14.setTextSize(25);

                textView15 = (TextView) findViewById(R.id.textView15);
                textView15.setText(""+puntuacion.get(5));
                textView15.setTextColor(Color.parseColor("#FFFFFF"));
                textView15.setTextSize(25);

                textView16 = (TextView) findViewById(R.id.textView16);
                textView16.setText("7- "+nombres.get(6));
                textView16.setTextColor(Color.parseColor("#FFFFFF"));
                textView16.setTextSize(25);

                textView17 = (TextView) findViewById(R.id.textView17);
                textView17.setText(""+puntuacion.get(6));
                textView17.setTextColor(Color.parseColor("#FFFFFF"));
                textView17.setTextSize(25);

                textView18 = (TextView) findViewById(R.id.textView18);
                textView18.setText("8- "+nombres.get(7));
                textView18.setTextColor(Color.parseColor("#FFFFFF"));
                textView18.setTextSize(25);

                textView19 = (TextView) findViewById(R.id.textView19);
                textView19.setText(""+puntuacion.get(7));
                textView19.setTextColor(Color.parseColor("#FFFFFF"));
                textView19.setTextSize(25);

                textView20 = (TextView) findViewById(R.id.textView20);
                textView20.setText("9- "+nombres.get(8));
                textView20.setTextColor(Color.parseColor("#FFFFFF"));
                textView20.setTextSize(25);


                textView21 = (TextView) findViewById(R.id.textView21);
                textView21.setText(""+puntuacion.get(8));
                textView21.setTextColor(Color.parseColor("#FFFFFF"));
                textView21.setTextSize(25);


                textView22 = (TextView) findViewById(R.id.textView22);
                textView22.setText("10- "+nombres.get(9));
                textView22.setTextColor(Color.parseColor("#FFFFFF"));
                textView22.setTextSize(25);


                textView23 = (TextView) findViewById(R.id.textView23);
                textView23.setText(""+puntuacion.get(9));
                textView23.setTextColor(Color.parseColor("#FFFFFF"));
                textView23.setTextSize(25);




                Log.v("tag", String.valueOf(nombres));
                Log.v("tag", String.valueOf(puntuacion));
            }
        });

    }


}

