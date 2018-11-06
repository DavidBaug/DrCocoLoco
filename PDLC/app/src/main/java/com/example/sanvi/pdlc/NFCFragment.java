package com.example.sanvi.pdlc;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NFCFragment extends Fragment implements NfcAdapter.ReaderCallback, SensorEventListener{

    public static final String TAG = "CardReaderFragment";

    // Este flag indica que la aplicación está interesada en dispositivos NFC-A (aunque incluye
    // tambien a los demás dispositivos) y que el sistema no debería comprobar la presencia de
    // información con formato NDEF (Android Beam por ejemplo)
    public static int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;

    // Declaramos el gestor de etiquetas
    private MifareUltralightTagTester tagManager;

    // Constantes para sensores:
    private static final float SHAKE_THRESHOLD = 1.15f;
    private static final int SHAKE_WAIT_TIME_MS = 400;
    private SensorManager mSensorManager;
    private Sensor mSensorAcc;
    private long mShakeTime = 0;

    // creamos el objeto para establecer la imagen que queremos que se ponga en función de la
    // etiqueta nfc que el usuario escanee
    public View gestorInterfaz;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtenemos los sensores que usaremos
        mSensorManager = (SensorManager) this.getContext().getSystemService(Context.SENSOR_SERVICE);
        mSensorAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Creamos el gestor de etiquetas NFC
        tagManager = new MifareUltralightTagTester();

        // Activamos el modo lectura del dispositivo que ejecuta este programa
        enableReaderMode();

    }


    // Detecta el escaneo de una etiqueta con el dispositivo
    @Override
    public void onTagDiscovered(final Tag tag) {

        Log.i(TAG, "New tag discovered");

        // ejecutamos el cambio entre imagenes segun la etiqueta en la hebra principal
        // ya que es la única que puede encargarse de modificar la interfaz
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // Realizamos la lectura de la etiqueta con el gestor de etiquetas
                String indiceEtiqueta = tagManager.readTag(tag);

                // Creamos los objetos que harán referencia a los fragmentos de la interfaz que
                // deseamos cambiar en función de la etiqueta NFC que detecte nuestro dispositivo
                TextView texto = (TextView) gestorInterfaz.findViewById(R.id.textView3);


                // Una vez el usuario escanea una etiqueta NFC escondemos cualquier texto visible
                // en pantalla y hacemos aparecer la imagen asociada a la vista
                texto.setVisibility(View.INVISIBLE);

                // En función de la etiqueta que el usuario haya escaneado redirigimos la fuente de
                // la imagen existente en la interfaz hacia un lado o hacia otro, de esta manera
                // conseguimos que haya una sola imagen en la interfaz en lugar de dos con
                // dos fuentes distintas. Además cambiamos los booleanos necesarios para utilizarlos
                // más tarde a la hora de gestionar el acelerómetro.
                if (indiceEtiqueta.charAt(0) == '1') {
                    Log.i(TAG, "IDENTIFICADA LA PRIMERA ETIQUETA");

                } else if (indiceEtiqueta.charAt(0) == '2') {
                    Log.i(TAG, "IDENTIFICADA LA SEGUNDA ETIQUETA");
                }
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        gestorInterfaz = inflater.inflate(R.layout.fragment_nfc, container, false);

        return gestorInterfaz;
    }


    private void enableReaderMode() {
        Log.i(TAG, "Enabling reader mode");

        Activity activity = getActivity();

        // Comprobamos la validez del adaptador NFC del dispositivo
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);

        // En caso de ser válido
        if (nfc != null) {
            // Activamos el modo lectura
            nfc.enableReaderMode(activity, this, READER_FLAGS, null);
        }
    }


    // Detecta una agitación del dispositivo a partir del umbral predefinido
    private void detectShake(SensorEvent event) {

        // Calculamos el tiempo transcurrido entre la última vez que se agitó el
        // dispositivo y la actual, en caso de que sea menor que el tiempo
        // mínimo definido entre agitaciones (SHAKE_WAIT_TIME_MS), no se hace nada.
        long now = System.currentTimeMillis();
        if ((now - mShakeTime) > SHAKE_WAIT_TIME_MS) {
            mShakeTime = now;

            // Obtenemos los valores de agitación en todos los ejes
            float gX = event.values[0] / SensorManager.GRAVITY_EARTH;
            float gY = event.values[1] / SensorManager.GRAVITY_EARTH;
            float gZ = event.values[2] / SensorManager.GRAVITY_EARTH;

            // calculamos el módulo de la fuerza generada
            // cuando no haya movimiento gForce estará próxima a 1
            double gForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            // en caso de que el módulo de la fuerza sea mayor que el umbral mínimo establecido
            // lo aceptaremos como una interacción válida, en caso conrtario, el dispositivo no
            // hará nada.
            if (gForce >= SHAKE_THRESHOLD) {

                Log.i(TAG, "DETECTADO UN SHAKE");

                //Borrar pantalla

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Creamos un texto vacio
                        TextView texto = (TextView) gestorInterfaz.findViewById(R.id.textView3);



                        if(texto.getText()!= ""){

                            texto.setText("");

                        }

                    }
                });
            }
        }
    }

    // Registra el correspondiente eventListener al sensor dado.
    // Le señalamos que el sensor a utilizar es el aceleómetro
    // Y le establecemos el periodo de muestreo con el SENSOR_DELAY_NORMAL
    // mediante el gestor de sensores.
    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorAcc, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    // Función que detecta el cambio en algún sensor del dispositivo, en
    // este caso lo usamos solo para el acelerómetro
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            detectShake(event);
        }
    }

    // Se llama solo cuando la exactitud< del acelerómetro cambia
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }


}