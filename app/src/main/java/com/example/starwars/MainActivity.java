package com.example.starwars;
import android.annotation.TargetApi;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Modelo.ColorAudio;
import Modelo.Game;
import Modelo.Starwars;
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
    Game game;
    int sonChewbacca;
    int sonDarkVader;
    int sonr2d2;
    int sonr2d201;
    long TIME_DELAY = 1000;
    int numCOLORS = 2;
    View.OnClickListener listener;
    ExecutorService executor;

    Button play;
    ImageView chewakablue,chewakared,c3poblue, c3pored,r2d2blue, r2d2red, darthvaderblue, darthvaderred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElements();
        game = new Game();
        initGame();
        createSoundPool();
        initAudios();
        getCharacterAPIColors();
        initListener();
        setListeners();
    }
    class RunnableColor implements Runnable {
        //directamente suene el audio de un color, constructor reciba el color que quiero que se repro
        //que me haga sonar un color, le tengo que pasar un color
        ColorAudio coloaudio;

        public RunnableColor(ColorAudio c) {
            coloaudio = c;
        }

        @Override
        public void run() {
            soundPool.play(coloaudio.getAudio(), 1, 1, 1, 0, 1);
            //iluminate color
            //iluminateColor(coloaudio.getColorSimon());
        }
    }

    private void generoLista() {
        //0 a 10 y por cada uno de ellos le doy un add, reproducir la lista
        game.getTiradasMachine().clear();
        for (int i = 0; i < 10; i++) {
            game.getTiradasMachine().add(getRandomColor());
            //PlayLista tengo una lista de colores y sonidos vamos a hacer que suene

        }
    }

    private ColorAudio getRandomColor() {
        ColorAudio colorAudio;
        //generamos un numero al azar
        int rnd = new Random().nextInt(numCOLORS);
        if (rnd == 0) {
            //le pasaremos el color y suene el azul
            colorAudio = new ColorAudio(sonAzul, Starwars.BLUE);
        } else if (rnd == 1) {
            colorAudio = new ColorAudio(sonc3po, Starwars.RED);
        } else if (rnd == 2) {
            colorAudio = new ColorAudio(sonAmarillo, Starwars.BLUE);
        } else {
            colorAudio = new ColorAudio(sonVerde, Starwars.BLUE);
        }
        return colorAudio;
    }
    private void setListeners() {
    }

    private void initListener() {
    }

    private void initGame() {
        game.initGame();

    }

    private void initAudios() {
        sonc3po = soundPool.load(this, R.raw.c3po, 1);
        sonChewbacca = soundPool.load(this, R.raw.chewbacca, 1);
        sonDarkVader = soundPool.load(this, R.raw.darthvader, 1);
        sonr2d2 = soundPool.load(this, R.raw.r2d2, 1);
        sonr2d201 = soundPool.load(this, R.raw.r2d201, 1);

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
        //play = findViewById(R.id.ibPlay);
        chewakablue = findViewById(R.id.chewakablue);
        chewakared = findViewById(R.id.chewakared);
        c3poblue = findViewById(R.id.c3poblue);
        c3pored = findViewById(R.id.c3pored);
        r2d2blue = findViewById(R.id.r2d2blue);
        r2d2red = findViewById(R.id.r2d2red);
        darthvaderblue = findViewById(R.id.darthvaderblue);
        darthvaderred = findViewById(R.id.darthvaderred);
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

