package com.example.sanvi.pdlc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class LeerDatos extends AsyncTask<String,Void,String> {

    private ImageView imageView;
    private TextView textView,textView2;
    private String cadena = "";
    private String titulo = "";
    private String descr = "";
    private String link_ini = "http://www.parqueciencias.com";
    URL url;

    public LeerDatos(TextView textView,TextView textView2, ImageView imageView){
        this.textView = textView;
        this.textView2 = textView2;
        this.imageView = imageView;
    }

    public String getLink_ini() {
        return link_ini;
    }

    @Override
    protected String doInBackground(String... strings) {
        Document doc = null;
        try {
            doc = Jsoup.connect(strings[0]).get();

            cadena = doc.outerHtml();

            String[] aux = cadena.split("<li>");
            String[] aux2 = aux[13].split("</li>");

            String[] aux3 = cadena.split(" <p class=\"negrita\">");
            String[] aux4 = aux3[2].split("</p>");

            String[] aux5 = cadena.split("src=\"");
            String[] aux6 = aux5[28].split("\"");
            System.out.println(aux6[0]);

            link_ini = link_ini + aux6[0];
            //            String[] aux6 = aux3[2].split("</p>");

            titulo = aux2[0];
            descr  = aux4[0];

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }


    @Override
    protected void onPostExecute(String temp){
        textView.setText(titulo);
        textView2.setText(descr);
        Picasso.get().load(link_ini).into(imageView);
//        BitmapFactory bm = new BitmapFactory();

        //imageView.setImageBitmap();


        //imageView.setImageURI(url);
    }


}
