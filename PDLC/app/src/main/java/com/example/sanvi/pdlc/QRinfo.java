package com.example.sanvi.pdlc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class QRinfo extends AppCompatActivity {


    public TextView texto;
    public ImageView imagen;

    public TextView nombre;

    String raw;

    public void exit(View view){

        this.finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Bundle extra = getIntent().getExtras();
        raw=extra.getString("raw");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        nombre= findViewById(R.id.textViewNombre);
        imagen=findViewById(R.id.imageView);
        texto=findViewById(R.id.textViewSalas);

        String[] parts = raw.split("###");

        nombre.setText(parts[0]);

        Picasso.get().load(parts[1]).into(imagen);
        texto.setText(parts[2]);
    }
}
