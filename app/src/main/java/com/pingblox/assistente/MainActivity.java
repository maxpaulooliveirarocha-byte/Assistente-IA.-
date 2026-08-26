package com.pingblox.assistente;

import android.app.Activity;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.content.Intent;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity
        implements TextToSpeech.OnInitListener {

    TextToSpeech tts;
    TextView status;
    TextView texto;
    boolean escutando = false;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 60, 40, 40);

        status = new TextView(this);
        status.setText("Assistente IA");
        status.setTextSize(28);
        layout.addView(status);

        texto = new TextView(this);
        texto.setText("Toque em começar para falar.");
        texto.setTextSize(18);
        layout.addView(texto);

        Button iniciar = new Button(this);
        iniciar.setText("🎙️ Começar a escutar");
        layout.addView(iniciar);

        Button parar = new Button(this);
        parar.setText("⏹️ Parar escuta");
        layout.addView(parar);

        setContentView(layout);

        tts = new TextToSpeech(this, this);

        iniciar.setOnClickListener(v -> {
            escutando = true;
            ouvir();
        });

        parar.setOnClickListener(v -> {
            escutando = false;
            tts.stop();
            status.setText("⏹️ Escuta parada");
        });
    }

    void ouvir() {
        if (!escutando) return;

        Intent i = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        i.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "pt-BR");

        i.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        startActivityForResult(i, 100);
    }

    @Override
    protected void onActivityResult(
            int request,
            int result,
            Intent data) {

        super.onActivityResult(request, result, data);

        if (request == 100 &&
                result == RESULT_OK &&
                data != null) {

            ArrayList<String> resultados =
                    data.getStringArrayListExtra(
                            RecognizerIntent.EXTRA_RESULTS);

            if (resultados != null &&
                    !resultados.isEmpty()) {

                String pergunta = resultados.get(0);

                texto.setText(
                        "Você: " + pergunta);

                String resposta =
                        "Entendi. Você disse: "
                        + pergunta;

                texto.append(
                        "\n\nAssistente: "
                        + resposta);

                tts.speak(
                        resposta,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "resposta");
            }
        }

        if (escutando) {
            ouvir();
        }
    }

    @Override
    public void onInit(int status) {
        tts.setLanguage(
                new Locale("pt", "BR"));
    }
          }
