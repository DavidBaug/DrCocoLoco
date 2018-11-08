package com.example.sanvi.pdlc;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QR_Fragment extends Fragment implements ZXingScannerView.ResultHandler {

    private TextView texto;
    private ZXingScannerView mScannerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        texto = (TextView) getView().findViewById(R.id.textViewQR);

        mScannerView = new ZXingScannerView(getActivity());
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }


    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(com.google.zxing.Result rawResult) {
//        Toast.makeText(getActivity(), rawResult.getText(), Toast.LENGTH_SHORT).show();

        setTextoQR(rawResult.getText());


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(QR_Fragment.this);
            }
        }, 2000);
    }

    public void setTextoQR(String qr){

        switch (qr){
            case "eureka":
                texto.setText("La Sala Eureka propone a los visitantes experimentar con diferentes " +
                        "fenómenos físicos y con la resolución de determinados problemas mediante " +
                        "elementos interactivos. Eureka está organizada en base a una serie de " +
                        "tópicos sugerentes y significativos, experiencias " +
                        "seleccionadas por su posibilidad de interacción y por la implicación de " +
                        "conceptos importantes desde el punto de vista científico.");
                break;
            case "biodomo":
                texto.setText("Un espacio dedicado a Educación, la Conservación y la Investigación de " +
                        "la biodiversidad en el planeta. El BioDomo recrea el clima de la franja tropical, " +
                        "y alberga más de 250 especies entre animales y plantas.");
                break;
            case "cuerpo humano":
                texto.setText("La salud y la vida son los ejes de este pabellón. A través de las más innovadoras " +
                        "técnicas expositivas el visitante conoce todo lo relacionado con: el cuerpo, la biomedicina, " +
                        "la anatomía, los trasplantes, los nuevos medicamentos, la revolución de la genética, la alimentación, " +
                        "la esperanza de vida, el patrimonio científico tecnológico, la relación entre los seres vivos y su entorno " +
                        "o las últimas investigaciones que se están desarrollando en este ámbito.");
                break;
            case "prevencion":
                texto.setText("En la vida cotidiana estamos expuestos a una diversidad de riesgos. Promover la " +
                        "cultura preventiva y la concienciación social frente al complejo mundo de la siniestralidad, " +
                        "haciendo uso de las nuevas tecnologías y los testimonios de expertos y afectados, es el objetivo " +
                        "que persigue el Pabellón Cultura de la Prevención, diseñado en colaboración con la Consejería de " +
                        "Empleo de la Junta de Andalucía. Se trata del único centro sobre cultura de la prevención que existe " +
                        "en España y el tercero que se ha creado en Europa con esta finalidad, junto a los centros de Tampere " +
                        "(Finlandia) y Dortmund (Alemania).");
                break;
            case "biosfera":
                texto.setText("Biosfera es el área central del Parque de las Ciencias. Se estructura en base " +
                        "a un hilo argumental básico: la vida en nuestro planeta. La Biosfera es aquí contemplada " +
                        "como un gran Ecosistema lleno de pequeños sistemas interrelacionados. En ella diversidad e " +
                        "interdependencia son los factores que más atención nos merecerán.");
                break;
            case "percepcion":
                texto.setText("Percepción es una Sala vinculada al mundo de los sentidos. Contiene " +
                        "diversas experiencias con Luz y Sonido y sobre la relación de estos fenómenos " +
                        "con la forma que tenemos de percibirlos. Comprender la naturaleza de la luz, las " +
                        "características físicas de lentes y espejos, la reflexión y refracción, etc. Otros " +
                        "módulos están dedicados a conocer mejor la forma en que nuestros sentidos y cerebro " +
                        "interpretan el mundo que nos rodea.");
                break;
            case "explora":
                texto.setText("En la Sala Explora los más pequeños pueden jugar con la ciencia con " +
                        "experiencias exclusivas para niños/as de 3 a 7 años. De martes a viernes, " +
                        "en período escolar, la sala está reservada a grupos. Los fines de semana y " +
                        "en período vacacional, está abierta a todo el público.");
                break;
        }

    }
}

