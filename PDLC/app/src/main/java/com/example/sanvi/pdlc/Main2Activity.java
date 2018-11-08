package com.example.sanvi.pdlc;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class Main2Activity extends Activity {

    static TextView texto;
    static ImageView imagen;

    static TextView nombre;

    String raw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Bundle extra = getIntent().getExtras();
        raw=extra.getString("raw");

        nombre= findViewById(R.id.textViewNombre);
        imagen=findViewById(R.id.imageView);
        texto=findViewById(R.id.textViewSalas);


//        String[] parts = raw.split("###");
//
//        nombre.setText(parts[0]);
//
//        Picasso.get().load(parts[1]).into(imagen);
//        texto.setText(parts[2]);


        Toast.makeText(this, raw, Toast.LENGTH_SHORT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

}
