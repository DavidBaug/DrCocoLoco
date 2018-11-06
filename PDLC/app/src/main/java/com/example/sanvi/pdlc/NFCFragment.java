package com.example.sanvi.pdlc;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class NFCFragment extends Fragment {

    private TextView texto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(getContext());

        if (!adapter.isEnabled()) {
            Toast.makeText(getContext(), "Si no activas el NFC poco te voy a contar...", Toast.LENGTH_LONG).show();
        }

        Bundle options = new Bundle();
        options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 5000);


        adapter.enableReaderMode(getActivity(),
                new NfcAdapter.ReaderCallback() {
                    @Override
                    public void onTagDiscovered(final Tag tag) {
                        // Aniadir texto personalizado a tags
                        texto.setText("asjdsd");
                    }
                },
                NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                options);



        return inflater.inflate(R.layout.fragment_nfc,null);
    }



}
