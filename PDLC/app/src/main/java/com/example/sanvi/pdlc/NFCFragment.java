package com.example.sanvi.pdlc;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class NFCFragment extends Fragment {

    EditText txtTagContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        NfcAdapter nfc;

        nfc = NfcAdapter.getDefaultAdapter(getContext());



        return inflater.inflate(R.layout.fragment_nfc,null);
    }


}
