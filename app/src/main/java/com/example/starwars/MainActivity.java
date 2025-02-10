package com.example.starwars;
import static android.content.ContentValues.TAG;
import static Modelo.Game.MACHINE;
import static Modelo.Game.START;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Modelo.ColorAudio;
import Modelo.Colors;
import Modelo.Game;
import webservice.CharacterColor;
import webservice.CharacterColorRestClient;

public class MainActivity extends AppCompatActivity {

    ArrayList<CharacterColor> listCharacter;
    Game game;
    Button bsubmit;
    SoundPool soundPool;
    private int score = 0;
    int sonc3po, sonChewbacca,sonDarkVader,sonr2d2;
    static final int TIME_DELAY = 2000;
    View.OnClickListener listener;
    ExecutorService executor;
    ImageView chewakablue,chewakared,c3poblue, c3pored,r2d2blue, r2d2red, darthvaderblue, darthvaderred, ivcharacter;
    TextView tvcolor, tvscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        initElements();
        game = new Game();
        createSoundPool();
        initAudios();
        initGame();
        initListener();
        setListeners();

    }

    private void initElements() {
        executor = Executors.newSingleThreadExecutor();
        tvcolor = findViewById(R.id.tvcolor);
        tvscore = findViewById(R.id.tvPuntuacio);
        bsubmit = findViewById(R.id.bsubmit);
        ivcharacter = findViewById(R.id.ivcharacter);
        chewakablue = findViewById(R.id.chewakablue);
        chewakared = findViewById(R.id.chewakared);
        c3poblue = findViewById(R.id.c3poblue);
        c3pored = findViewById(R.id.c3pored);
        r2d2blue = findViewById(R.id.r2d2blue);
        r2d2red = findViewById(R.id.r2d2red);
        darthvaderblue = findViewById(R.id.darthvaderblue);
        darthvaderred = findViewById(R.id.darthvaderred);
    }

    class RunnableColor implements Runnable {
        ColorAudio coloaudio;

        public RunnableColor(ColorAudio c) {
            coloaudio = c;
        }
        //seteamos los valores que ha escogido el jugador a la UI
        @Override
        public void run() {
            int img= coloaudio.getImageId();
            ivcharacter.setImageResource(img);
            tvcolor.setBackgroundColor(coloaudio.getColor() == Colors.BLUE ? Color.BLUE : Color.RED);
            soundPool.play(coloaudio.getAudio(), 1, 1, 1, 0, 1);
        }
    }
    private void setListeners() {
        bsubmit.setOnClickListener(listener);
        chewakablue.setOnClickListener(listener);
        chewakared.setOnClickListener(listener);
        c3poblue.setOnClickListener(listener);
        c3pored.setOnClickListener(listener);
        r2d2blue.setOnClickListener(listener);
        r2d2red.setOnClickListener(listener);
        darthvaderblue.setOnClickListener(listener);
        darthvaderred.setOnClickListener(listener);
    }
    private void initListener() {
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.getState() == Game.START) {
                    if (view.getId() == bsubmit.getId()) {
                            game.play();
                        game.getTiradasMachine().clear();
                        getCharacterAPIColors();

                    }
                } else if (game.getState() == Game.PLAYER) {
                    ColorAudio playerMove = null;
                    //añade la opcion que el jugador clica a la lista de jugadas del jugador
                    if (view.getId() == chewakablue.getId()) {
                        playerMove= new ColorAudio(Colors.BLUE, sonChewbacca, R.drawable.chewaka);
                    } else if (view.getId() == chewakared.getId()) {
                        playerMove= new ColorAudio(Colors.RED, sonChewbacca, R.drawable.chewaka);
                    } else if (view.getId() == c3poblue.getId()) {
                        playerMove= new ColorAudio(Colors.BLUE, sonc3po, R.drawable.c3po);
                    } else if (view.getId() == c3pored.getId()) {
                        playerMove= new ColorAudio(Colors.RED, sonc3po, R.drawable.c3po);
                    } else if (view.getId() == r2d2blue.getId()) {
                        playerMove= new ColorAudio(Colors.BLUE, sonr2d2, R.drawable.r2d2);
                    } else if (view.getId() == r2d2red.getId()) {
                        playerMove= new ColorAudio(Colors.RED, sonr2d2, R.drawable.r2d2);
                    } else if (view.getId() == darthvaderblue.getId()) {
                        playerMove= new ColorAudio(Colors.BLUE, sonDarkVader, R.drawable.darthvader);
                    } else if (view.getId() == darthvaderred.getId()) {
                        playerMove= new ColorAudio(Colors.RED, sonDarkVader, R.drawable.darthvader);
                    }

                    if(playerMove != null){

                        game.getTiradasPlayer().add(playerMove);
                        Handler  h = new Handler();
                        h.post(new RunnableColor(playerMove));
                        boolean isCorrect = game.CompareColors();
                        if (!isCorrect) {
                            game.setState(Game.START);
                            game.getTiradasMachine().clear();
                            game.getTiradasPlayer().clear();
                            tvscore.setText("0");
                            tvcolor.setBackgroundColor(Color.WHITE);
                            ivcharacter.setImageResource(R.drawable.c3po);
                            return;
                        }
                    //Se limpian las jugadas del jugadores, y se pasa a la maquina
                        if (game.getTiradasPlayer().size() == game.getTiradasMachine().size()) {
                            game.getTiradasPlayer().clear();
                            game.setState(MACHINE);
                            h = new Handler();
                     //control de las jugadas de la maquina despues de la primera tirada
                            h.postDelayed(new RunnableState(Game.PLAYER),
                                    TIME_DELAY * game.getTiradasMachine().size() + 1);
                            getCharacterAPIColors();
                            tvscore.setText("" + game.getScore());
                        }


                    }

                }

            }
        };
    }
    class RunnnablePlayMachineSequence implements Runnable {
        public RunnnablePlayMachineSequence() {
        }
        @Override
        public void run() {
            playMachineSequence();
        }
    }

    //metodo que reproduce la secuencia de la maquina
    private void playMachineSequence(){
        Handler h = new Handler();
        for(int i = 0; i < game.getTiradasMachine().size(); i++){
            long timeDelay = MainActivity.TIME_DELAY * i;
            h.postDelayed(new MainActivity.RunnableColor(game.getTiradasMachine().get(i)), timeDelay);
        }
    }
    //controla el estado del juego (primero maquina despues jugadores y viceversa)
    class RunnableState implements Runnable
    {
        int state = 0;
        public RunnableState(int state) {
            this.state = state;
        }
        @Override
        public void run() {
           game.setState(state);
        }
    }
    private void initGame() {
        game.initGame();
    }
    private void initAudios() {

            sonc3po = soundPool.load(this, R.raw.c3po, 1);
            sonChewbacca = soundPool.load(this, R.raw.chewbacca, 1);
            sonDarkVader = soundPool.load(this, R.raw.darthvader, 1);
            sonr2d2 = soundPool.load(this, R.raw.r2d2, 1);

            //guardamos los audios de los personajes, para poder utilizar a lo largo del codigo
        game.addCharacter(new ColorAudio(Colors.BLUE, sonc3po, R.drawable.c3po));
        game.addCharacter(new ColorAudio(Colors.RED, sonc3po, R.drawable.c3po));
        game.addCharacter(new ColorAudio(Colors.BLUE, sonChewbacca, R.drawable.chewaka));
        game.addCharacter(new ColorAudio(Colors.RED, sonChewbacca, R.drawable.chewaka));
        game.addCharacter(new ColorAudio(Colors.BLUE, sonr2d2, R.drawable.r2d2));
        game.addCharacter(new ColorAudio(Colors.RED, sonr2d2, R.drawable.r2d2));
        game.addCharacter(new ColorAudio(Colors.BLUE, sonDarkVader, R.drawable.darthvader));
        game.addCharacter(new ColorAudio(Colors.RED, sonDarkVader, R.drawable.darthvader));

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

    //metodo que controla la conexion con el webservice, y apartir de ella reproduce la secuencia de la maquina
    private void getCharacterAPIColors() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                CharacterColorRestClient restClient = new CharacterColorRestClient();

                try {
                    listCharacter = restClient.listCharacters();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            for (CharacterColor characterColor : listCharacter) {
                                ColorAudio colorAudio = null;
                                if (characterColor.getCharacter().equals("C3PO") && characterColor.getColor().equals("blue")) {
                                    colorAudio = new ColorAudio(Colors.BLUE, sonc3po, R.drawable.c3po);
                                } else if (characterColor.getCharacter().equals("C3PO") && characterColor.getColor().equals("red")) {
                                    colorAudio = new ColorAudio(Colors.RED, sonc3po, R.drawable.c3po);
                                } else if (characterColor.getCharacter().equals("Chewaka") && characterColor.getColor().equals("blue")) {
                                    colorAudio = new ColorAudio(Colors.BLUE, sonChewbacca, R.drawable.chewaka);
                                } else if (characterColor.getCharacter().equals("Chewaka") && characterColor.getColor().equals("red")) {
                                    colorAudio = new ColorAudio(Colors.RED, sonChewbacca, R.drawable.chewaka);
                                } else if (characterColor.getCharacter().equals("R2D2") && characterColor.getColor().equals("blue")) {
                                    colorAudio = new ColorAudio(Colors.BLUE, sonr2d2, R.drawable.r2d2);
                                } else if (characterColor.getCharacter().equals("R2D2") && characterColor.getColor().equals("red")) {
                                    colorAudio = new ColorAudio(Colors.RED, sonr2d2, R.drawable.r2d2);
                                } else if (characterColor.getCharacter().equals("Darth Vader") && characterColor.getColor().equals("blue")) {
                                    colorAudio = new ColorAudio(Colors.BLUE, sonDarkVader, R.drawable.darthvader);
                                } else if (characterColor.getCharacter().equals("Darth Vader") && characterColor.getColor().equals("red")) {
                                    colorAudio = new ColorAudio(Colors.RED, sonDarkVader, R.drawable.darthvader);
                                }
                                if (colorAudio != null) {
                                    game.getTiradasMachine().add(colorAudio);
                                }
                            }
                            playMachineSequence();
                            new Handler().postDelayed(new RunnableState(Game.PLAYER),
                                    TIME_DELAY * game.getTiradasMachine().size());
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

