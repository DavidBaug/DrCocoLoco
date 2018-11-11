package com.example.sanvi.pdlc;

import android.Manifest;
import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.w3c.dom.Text;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import ai.api.ui.AIButton;
import ai.api.ui.AIDialog;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
//este es el fragment del bot
public class DialogFragment extends Fragment implements AIListener,SensorEventListener {

    @Nullable

    private RecyclerView mRecyclerView;
    private TextToSpeech mTextToSpeech;
    private AIService aiService;
    private Button listenButton;
    private TextView resultTextView;
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private SensorEventListener proximitySensorListener;
    private boolean parado;
    private double LIMIT;

    public DialogFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 0);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.NFC}, 0);


        View view =  inflater.inflate(R.layout.fragment_dialog, container, false);
        final AIConfiguration config = new AIConfiguration("ff0840a80d47483a9ec65d0dbd429051", AIConfiguration.SupportedLanguages.Spanish,AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(view.getContext(),config);
        aiService.setListener(this);

        mTextToSpeech = new TextToSpeech(view.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });

        FloatingActionButton btn = view.findViewById(R.id.micro);
        resultTextView = view.findViewById(R.id.textView2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aiService.startListening();
            }
        });

        sensorManager = (SensorManager)getActivity().getSystemService(Service.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        LIMIT = proximitySensor.getMaximumRange()/3;
        parado = false;

        proximitySensorListener = new SensorEventListener(){
            @Override
            //interpolar
            public void onSensorChanged(SensorEvent event) {
                if(event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                    if (event.values[0] <= LIMIT && !parado) {
                        //parar bot
                        parado = true;
                        //llamamos al bot para que pare
                        stop();
                    }
                    else {
                        if (parado && event.values[0] > LIMIT) {
                            //esta parado hacemos que ande,porque tiene un nivel mayor
                            run();
                        }
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        if(proximitySensor != null){
            Toast info = Toast.makeText(getContext(),"Función desactivar: Disponible", Toast.LENGTH_SHORT);
            info.show();

        }
        else{
            Toast info_warn = Toast.makeText(getContext(),"Función descativar: No disponible", Toast.LENGTH_SHORT);
            info_warn.show();
        }


        return view;

    }


    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();



        resultTextView.setText(result.getFulfillment().getSpeech());

        mTextToSpeech.speak(result.getFulfillment().getSpeech(), TextToSpeech.QUEUE_FLUSH,null,null);
    }

    @Override
    public void onError(AIError error) {
        Log.d("tag",error.toString());
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }


    public void run(){
        if(sensorManager != null){
            Toast info = Toast.makeText(getContext(),"Vuelve a estar activo", Toast.LENGTH_SHORT);
            info.show();
            parado = false;
            mTextToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {

                }
            });
        }

    }

    //para que pare
    public void stop() {
        if(sensorManager != null){
            if(mTextToSpeech != null){
                Toast info = Toast.makeText(getContext(),"Parado", Toast.LENGTH_SHORT);
                info.show();
                parado = true;
                mTextToSpeech.shutdown();
            }
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onStop() {
        //quitamos el sensor de luz al parar la app
        sensorManager.unregisterListener(proximitySensorListener,proximitySensor);
        parado = true;
        super.onStop();
    }

    @Override
    public void onPause(){
        if(sensorManager != null && proximitySensorListener != null && proximitySensor != null){
            sensorManager.unregisterListener(proximitySensorListener,proximitySensor);
        }
        super.onPause();
    }

    @Override
    public void onResume(){
        if(sensorManager != null && proximitySensorListener != null && proximitySensor != null){
            sensorManager.registerListener(proximitySensorListener,proximitySensor,sensorManager.SENSOR_DELAY_NORMAL);
        }
        super.onResume();
    }





    /**Calendar calendar = Calendar.getInstance(Time.getCurrentTimeZone());
     Para obtener la fecha actual .DAY_OF_WEEK.
     uso :
     Calendar.getInstance().get(Calendar.DAY_OF_WEEK)**/

}

