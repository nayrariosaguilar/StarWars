package Modelo;

import java.util.ArrayList;
import java.util.Random;

import webservice.CharacterColor;
import webservice.CharacterColorRestClient;

public class Game {
    public static final int START = 1, MACHINE = 2, PLAYER =3;
    private int score;
    ArrayList<ColorAudio> tiradasMachine;
    ArrayList<ColorAudio> tiradasPlayer;
    private ArrayList<ColorAudio> availableCharacters; // Nueva lista para personajes disponibles
    int numColors;
    int state;

    public ArrayList<ColorAudio> getTiradasPlayer() {
        return tiradasPlayer;
    }

    public int getState() {
        return state;
    }

    public int getNumColors() {
        return numColors;
    }

    public ArrayList<ColorAudio> getTiradasMachine() {
        return tiradasMachine;
    }
    public void addCharacter(ColorAudio characterAudio) {
        availableCharacters.add(characterAudio);
    }

    public void setNumColors(int numColors) {
        this.numColors = numColors;
    }

    public void setState(int state) {
        this.state = state;
    }


    public void setTiradasPlayer(ArrayList<ColorAudio> tiradasPlayer) {
        this.tiradasPlayer = tiradasPlayer;
    }

    public void setTiradasMachine(ArrayList<ColorAudio> tiradasMachine) {
        this.tiradasMachine = tiradasMachine;
    }

    public Game() {
        tiradasPlayer = new ArrayList<>();
        tiradasMachine = new ArrayList<>();
        availableCharacters = new ArrayList<>();

    }
//    public void addP(ColorAudio colorAudio) {
//        gameColors.add(colorAudio);
//    }
    public void initGame(){
        //el mismo objeto solo resetearlo
        tiradasPlayer.clear();
        tiradasMachine.clear();
        state = START;
        score = 0;
    }
    //PARA COMPARAR COLORES
    public boolean CompareColors() {
        if (tiradasPlayer.size() != tiradasMachine.size()) {
            return false;
        }

        for (int i = 0; i < tiradasPlayer.size(); i++) {
            ColorAudio playerMove = tiradasPlayer.get(i);
            ColorAudio machineMove = tiradasMachine.get(i);

            if (playerMove.getImageId() != machineMove.getImageId() ||
                    playerMove.getAudio() != machineMove.getAudio() ||
                    playerMove.getColor() != machineMove.getColor()) {
                return false;
            }
        }

        score++;
        return true;
    }
    public void nextLevel(CharacterColor characterColor) {
        state = MACHINE;
        tiradasPlayer.clear();;
        //tiradasMachine.add(gameColors.set());

    }
    public void play(){
        state = MACHINE;
        tiradasMachine.clear();
        tiradasPlayer.clear();
    }

}
