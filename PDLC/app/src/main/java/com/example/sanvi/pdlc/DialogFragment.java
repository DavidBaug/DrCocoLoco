package com.example.sanvi.pdlc;

import android.Manifest;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import static android.content.Context.SENSOR_SERVICE;

public class DialogFragment extends Fragment implements AIListener {

    @Nullable

    private RecyclerView mRecyclerView;
    private TextToSpeech mTextToSpeech;
    private AIService aiService;
    private Button listenButton;
    private TextView resultTextView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aiService.startListening();
            }
        });
        return view;

    }




    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();

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
    @Override //para que pare
    public void onStop() {
        super.onStop();

        if(mTextToSpeech != null){
            mTextToSpeech.shutdown();
        }
    }

    public void run(){
        Toast toastRun =
                Toast.makeText( (MainActivity)getActivity(),"Bot vuelve a escuchar", Toast.LENGTH_SHORT);
        toastRun.show();
        aiService.startListening();
    }


    /**Calendar calendar = Calendar.getInstance(Time.getCurrentTimeZone());
     Para obtener la fecha actual .DAY_OF_WEEK.

     uso :
     Calendar.getInstance().get(Calendar.DAY_OF_WEEK)* */
}
