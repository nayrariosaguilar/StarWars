package com.example.starwars;
import android.annotation.TargetApi;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import webservice.CharacterColor;
import webservice.CharacterColorRestClient;

public class MainActivity extends AppCompatActivity {
    // començaria reproduint una seqüència de sons,
    // cadascun associat a un personatge i a la vegada es veurà un color.
    //webservice de https://bibils.net/dam2/listcolor.php?num=20
    //random del 1 al 10 que el jugador relique
    //num es el numero de iteraciones
    ArrayList<CharacterColor> listCharacter;
    SoundPool soundPool;
    int sonc3po;
    int sonChewbacca;
    int sonDarkVader;
    int sonr2d2;
    int sonr2d201;
    long TIME_DELAY = 1000;
    int numCOLORS = 2;
    View.OnClickListener listener;
    ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElements();
        getCharacterAPIColors();
    }

    private void initAudios() {
        sonc3po = soundPool.load(this, R.raw.c3po, 1);
        sonChewbacca = soundPool.load(this, R.raw.chewbacca, 1);
        sonDarkVader = soundPool.load(this, R.raw.darthvader, 1);
        sonr2d2 = soundPool.load(this, R.raw.r2d2, 1);
        sonr2d201 = soundPool.load(this, R.raw.r2d201, 1);
        //play = findViewById(R.id.ibPlay);
    }

    /**
     * How to use SoundPool on all API levels
     */
    protected void createSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }
    }

    /**
     * Create SoundPool for versions >= LOLLIPOP
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    /**
     * Create SoundPool for deprecated versions < LOLLIPOP
     */
    @SuppressWarnings("deprecation")
    protected void createOldSoundPool() {
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
    }

    private void initElements() {
        executor = Executors.newSingleThreadExecutor(); //executor per a fer-ho de forma asíncrona

    }

    private void getCharacterAPIColors() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //creo un objecte de la classe CharacterColorRestClient
                CharacterColorRestClient restClient = new CharacterColorRestClient();
                try {
                    //obtinc la llista de colors des de la API
                    listCharacter = restClient.listCharacters();
                    for (int i = 0; i < listCharacter.size(); i++)
                        System.out.println("id: " + listCharacter.get(i).getCharacter() + " Name: " + listCharacter.get(i).getColor());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

