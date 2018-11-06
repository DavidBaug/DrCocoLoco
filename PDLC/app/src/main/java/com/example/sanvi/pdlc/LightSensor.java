package com.example.sanvi.pdlc;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;

public class LightSensor extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private boolean parado;
    private DialogFragment bot;

    public void onCreate(DialogFragment aux){

        //light sensor
        //------ Si no es activity main dice que no
        parado = false;
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        bot = aux;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LIGHT){
            if(event.values[0] < 10 && !parado){
                //parar bot
                parado = true;
                //llamamos al bot para que pare
                bot.onStop();
            }
            else{
                if(parado){
                    //esta parado hacemos que ande,porque tiene un nivel mayor
                    bot.onResume();
                }
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
