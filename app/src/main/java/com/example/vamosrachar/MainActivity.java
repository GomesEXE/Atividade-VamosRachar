package com.example.vamosrachar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {
    EditText edtValor, edtGalera;
    TextView tvRes;
    FloatingActionButton share, tocar;
    TextToSpeech ttsPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtValor = (EditText) findViewById(R.id.edtValor);
        edtValor.addTextChangedListener(this);

        edtGalera = (EditText) findViewById(R.id.edtGalera);
        edtGalera.addTextChangedListener(this);

        tvRes= (TextView) findViewById(R.id.textViewResultado);
        share= (FloatingActionButton) findViewById(R.id.btShare);
        share.setOnClickListener(this);

        // check for TTS  data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 1122);

        tocar= (FloatingActionButton) findViewById(R.id.btTocar);
        tocar.setOnClickListener(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1122) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                // the user has the necessary data - create the TTS
                ttsPlayer = new TextToSpeech(this, this);
            } else {
                // no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent
                        .setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        Log.v("PDM", edtValor.getText().toString());

        try{
            double galera= Double.parseDouble(edtGalera.getText().toString());
            double res= Double.parseDouble(edtValor.getText().toString());
            res= (res / galera);
            DecimalFormat df= new DecimalFormat("#.00");
            tvRes.setText("R$"+df.format(res));

        }catch (Exception e){
            tvRes.setText("R$ 0.00");
        }
    }

    @Override
    public void onClick(View view) {
        if (view==share){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "A conta dividida por pessoa deu "+tvRes.getText().toString());
            startActivity(intent);
        }
        if(view==tocar) {
            if (ttsPlayer!=null) {
                ttsPlayer.speak("O valor por pessoa é de " + tvRes.getText().toString() + " reais", TextToSpeech.QUEUE_FLUSH, null, "ID1");
            }
        }
    }

    @Override
    public void onInit(int initStatus) {
        //checando a inicialização
        if (initStatus == TextToSpeech.SUCCESS) {
            Toast.makeText(this, "TTS ativado...",
                    Toast.LENGTH_LONG).show();
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sem TTS habilitado...",
                    Toast.LENGTH_LONG).show();
        }
    }

    /*
    @Override
    public void onConfigurationChanged (Configuration _novaConfig){
        super.onConfigurationChanged(_novaConfig);

        if (_novaConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            Toast t=Toast.makeText(this, "To deitado", Toast.LENGTH_LONG);
            t.show();
        }
    }
     */

}