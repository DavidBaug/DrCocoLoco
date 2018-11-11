package com.example.sanvi.pdlc;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class QRinfo extends AppCompatActivity implements SensorEventListener {

    private static final float SHAKE_THRESHOLD = 1.15f;
    private static final int SHAKE_WAIT_TIME_MS = 400;
    private SensorManager mSensorManager;
    private Sensor mSensorAcc;
    private long mShakeTime = 0;

    public TextView texto;
    public ImageView imagen;

    public TextView nombre;

    String raw;

    public void exit(View view){

        this.finish();

    }

    private void detectShake(SensorEvent event) {

        // Calculamos el tiempo transcurrido entre la última vez que se agitó el
        // dispositivo y la actual, si es muy pequeño, shake = true
        long now = System.currentTimeMillis();
        if ((now - mShakeTime) > SHAKE_WAIT_TIME_MS) {
            mShakeTime = now;

            // Obtenemos los valores de agitación en todos los ejes
            float gX = event.values[0] / SensorManager.GRAVITY_EARTH;
            float gY = event.values[1] / SensorManager.GRAVITY_EARTH;
            float gZ = event.values[2] / SensorManager.GRAVITY_EARTH;

            // Si la fuerza está dentro de un umbral la reconocemos como shake
            double gForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce >= SHAKE_THRESHOLD) {

                Toast.makeText(getBaseContext(), "Uhusahd", Toast.LENGTH_SHORT);
                this.finish();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            detectShake(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mSensorAcc, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSensorManager = (SensorManager) this.getSystemService(this.SENSOR_SERVICE);
        mSensorAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Bundle extra = getIntent().getExtras();
        raw=extra.getString("raw");


        // Leemos la info del intent y la separamos

        String[] parts = raw.split("###");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrinfo);


        texto = findViewById(R.id.textViewSalas);
        nombre = findViewById(R.id.textViewNombre);
        imagen = findViewById(R.id.imageView);

        nombre.setText(parts[0]);

        // Con picasso cargamos la imagen en el view
        Picasso.get().load(parts[1]).into(imagen);
        texto.setText(parts[2]);




    }
}
