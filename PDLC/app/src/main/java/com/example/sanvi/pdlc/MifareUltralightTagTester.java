package com.example.sanvi.pdlc;

import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.util.Log;
import java.io.IOException;
import java.nio.charset.Charset;

public class MifareUltralightTagTester {

    private static final String TAG = MifareUltralightTagTester.class.getSimpleName();


    // Función para leer un texto de una etiqueta NFC
    public String readTag(Tag tag) {
        // Obtiene un objeto de la clase MifareUltralight, que es un tipo de tecnología de etiquetas
        MifareUltralight mifare = MifareUltralight.get(tag);
        try {
            // establecemos la conexion con la etiqueta
            mifare.connect();

            // buffer de lectura en el que almacenamos los bytes correspondientes a la página 4 de la
            // memoria de la etiqueta NFC
            byte[] payload = mifare.readPages(4);

            // devolvemos lo leido
            return new String(payload, Charset.forName("US-ASCII"));
        } catch (IOException e) { //tratamiento de excepciones
            Log.e(TAG, "IOException while writing MifareUltralight message...", e);
        } finally {
            // cerramos la conexion en caso de ser válido
            if (mifare != null) {
                try {
                    mifare.close();
                }
                catch (IOException e) {
                    Log.e(TAG, "Error closing tag...", e);
                }
            }
        }
        return null;
    }
}