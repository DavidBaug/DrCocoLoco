package com.example.sanvi.pdlc;

import android.Manifest;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QR_Fragment extends Fragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);

        //Instancia lector qr

        mScannerView = new ZXingScannerView(getActivity());

        Toast.makeText(getContext(),"Toca dos veces para activar el flash", Toast.LENGTH_SHORT).show();


        // Aniadido método para activar linterna
        mScannerView.setOnTouchListener(new OnDoubleTapListener(getContext()) {
            @Override
            public void onDoubleTap(MotionEvent e) {
                //Enciende flash
                mScannerView.toggleFlash();

            }
        });

        return mScannerView;
    }




    // Enciende la cámara
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();


    }


    // Para la cámara
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    // Lee info del qr y la pasa a una nueva actividad
    @Override
    public void handleResult(com.google.zxing.Result rawResult) {
//        Toast.makeText(getActivity(), rawResult.getText(), Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(QR_Fragment.this);
            }
        }, 2000);

        //Actividad que muestra la info leida
        Intent intent = new Intent(getActivity(), QRinfo.class);

        intent.putExtra("raw", rawResult.getText());

        startActivity(intent);

    }
}

