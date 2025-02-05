package com.example.starwars;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
    // començaria reproduint una seqüència de sons,
    // cadascun associat a un personatge i a la vegada es veurà un color.
    //webservice de https://bibils.net/dam2/listcolor.php?num=20
    //random del 1 al 10 que el jugador relique
    //num es el numero de iteraciones
    ArrayList<CharacterColor> listCharacter;
    Game game;
    Button bsubmit;
    SoundPool soundPool;
    int sonc3po, sonChewbacca,sonDarkVader,sonr2d2,sonr2d201;
    static final int TIME_DELAY = 1000;
    View.OnClickListener listener;
    ExecutorService executor;
    ImageView chewakablue,chewakared,c3poblue, c3pored,r2d2blue, r2d2red, darthvaderblue, darthvaderred, ivcharacter;
    TextView tvcolor, tvscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        game = new Game();
        //Audio
        createSoundPool();
        initAudios();
        //EVENTS
        initElements();
        initListener();
        setListeners();
        //game
        initGame();
        //getCharacterAPIColors();

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

    //mejorar esto
    private ColorAudio getRandomColor() {
        ColorAudio colorAudio;
        //generamos un numero al azar
        int rnd = new Random().nextInt(10);
        if (rnd == 0) {
            colorAudio = new ColorAudio(Colors.BLUE,sonc3po );
        } else if (rnd == 1) {
            colorAudio = new ColorAudio(Colors.BLUE,sonChewbacca);
        } else if (rnd == 2) {
            colorAudio = new ColorAudio(Colors.BLUE, sonDarkVader);
        } else {
            colorAudio = new ColorAudio(Colors.RED,sonr2d2);
        }
        return colorAudio;
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
                //si el jugador ha pulsado el boton empezar
                if (game.getState() == Game.START) {
                    if (view.getId() == bsubmit.getId()) {
                        getCharacterAPIColors();
                        game.play();
                        playMachineSequence();
                        Handler h = new Handler();
                        h.postDelayed(new RunnableState(Game.PLAYER)
                                , TIME_DELAY * game.getTiradasMachine().size());
                    }
                } else if (game.getState() == Game.PLAYER) {
                    if (view.getId() == chewakablue.getId()) {
                        game.getTiradasPlayer().add(new ColorAudio(Colors.BLUE, sonChewbacca));
                    }else if(view.getId() == chewakared.getId()) {
                        game.getTiradasPlayer().add(new ColorAudio(Colors.RED, sonChewbacca));
                    }else if(view.getId() == c3poblue.getId()) {
                        game.getTiradasPlayer().add(new ColorAudio(Colors.BLUE, sonc3po));
                    }else if(view.getId() == c3pored.getId()) {
                        game.getTiradasPlayer().add(new ColorAudio(Colors.RED, sonc3po));
                    }else if(view.getId() == r2d2blue.getId()) {
                        game.getTiradasPlayer().add(new ColorAudio(Colors.BLUE, sonr2d2));
                    }else if (view.getId() == r2d2red.getId()) {
                        game.getTiradasPlayer().add(new ColorAudio(Colors.RED, sonr2d2));
                    }else if(view.getId() == darthvaderblue.getId()) {
                        game.getTiradasPlayer().add(new ColorAudio(Colors.BLUE, sonDarkVader));
                    }else if(view.getId() == darthvaderred.getId()) {
                        game.getTiradasPlayer().add(new ColorAudio(Colors.RED, sonDarkVader));
                    }
                    //ERROR, OK_LIST_EQUALS, OK_LIST_NOT_EQUALS
                    if(game.CompareColors() == Game.ERROR){
                       game.setState(Game.START);
                       //SI HAY AUDIO ERROR?
                    }else if(game.CompareColors() == Game.OK_LIST_EQUALS) {
                        game.nextLevel();
                        Handler h = new Handler();
                        h.postDelayed(new RunnnablePlayMachineSequence(), TIME_DELAY);
                        h.postDelayed(new RunnableState(Game.PLAYER), TIME_DELAY * game.getTiradasMachine().size()+TIME_DELAY);
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
    private void playMachineSequence(){
        Handler h = new Handler();
        for(int i = 0; i < game.getTiradasMachine().size(); i++){
            long timeDelay = MainActivity.TIME_DELAY * i;
            h.postDelayed(new MainActivity.RunnableColor(game.getTiradasMachine().get(i)), timeDelay);
        }
    }
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
        sonr2d201 = soundPool.load(this, R.raw.r2d201, 1);
        //RESTO
        game.addColor(new ColorAudio(Colors.BLUE, sonChewbacca));
        game.addColor(new ColorAudio(Colors.RED, sonChewbacca));
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
        //inicialitzo l'executor per a fer-ho de forma asíncrona
        executor = Executors.newSingleThreadExecutor();
        tvcolor = findViewById(R.id.tvcolor);
        //tvscore = findViewById(R.id.tvscore); PAL FINAL NAY
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

    private void getCharacterAPIColors() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //creo un objecte de la classe CharacterColorRestClient
                CharacterColorRestClient restClient = new CharacterColorRestClient();
                try {
                    //obtinc la llista de colors des de la API
                    listCharacter = restClient.listCharacters();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < listCharacter.size(); i++) {

                                if (Objects.equals(listCharacter.get(i).getCharacter(), "C3PO") && Objects.equals(listCharacter.get(i).getColor(), "blue")) {
                                    ivcharacter.setImageResource(R.drawable.c3po);
                                    tvcolor.setBackgroundColor(Color.BLUE);
                                } else if (Objects.equals(listCharacter.get(i).getCharacter(), "C3PO") && Objects.equals(listCharacter.get(i).getColor(), "red")) {
                                    ivcharacter.setImageResource(R.drawable.c3po);
                                    tvcolor.setBackgroundColor(Color.RED);
                                } else if (Objects.equals(listCharacter.get(i).getCharacter(), "Chewaka") && Objects.equals(listCharacter.get(i).getColor(), "blue")) {
                                    ivcharacter.setImageResource(R.drawable.chewaka_blue);
                                    tvcolor.setBackgroundColor(Color.BLUE);
                                } else if(Objects.equals(listCharacter.get(i).getCharacter(), "Chewaka") && Objects.equals(listCharacter.get(i).getColor(), "red")) {
                                    ivcharacter.setImageResource(R.drawable.chewaka_red);
                                    tvcolor.setBackgroundColor(Color.RED);
                                } else if(Objects.equals(listCharacter.get(i).getCharacter(), "R2D2") && Objects.equals(listCharacter.get(i).getColor(), "blue")) {
                                    ivcharacter.setImageResource(R.drawable.r2d2);
                                    tvcolor.setBackgroundColor(Color.BLUE);
                                } else if(Objects.equals(listCharacter.get(i).getCharacter(), "R2D2") && Objects.equals(listCharacter.get(i).getColor(), "red")) {
                                    ivcharacter.setImageResource(R.drawable.r2d2);
                                    tvcolor.setBackgroundColor(Color.RED);
                                } else if(Objects.equals(listCharacter.get(i).getCharacter(), "Darth Vader") && Objects.equals(listCharacter.get(i).getColor(), "blue")) {
                                    ivcharacter.setImageResource(R.drawable.darthvader);
                                    tvcolor.setBackgroundColor(Color.BLUE);
                                } else if(Objects.equals(listCharacter.get(i).getCharacter(), "Darth Vader") && Objects.equals(listCharacter.get(i).getColor(), "red")) {
                                    ivcharacter.setImageResource(R.drawable.darthvader);
                                    tvcolor.setBackgroundColor(Color.RED);
                                }
                            }
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

