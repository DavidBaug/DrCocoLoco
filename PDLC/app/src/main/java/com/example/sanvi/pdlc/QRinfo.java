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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrinfo);

        nombre= findViewById(R.id.textViewNombre);
        imagen=findViewById(R.id.imageView);
        texto=findViewById(R.id.textViewSalas);

        texto.setMovementMethod(new ScrollingMovementMethod());

        String[] parts = raw.split("###");

        nombre.setText(parts[0]);

        Picasso.get().load(parts[1]).into(imagen);
        texto.setText(parts[2]);
    }
}
