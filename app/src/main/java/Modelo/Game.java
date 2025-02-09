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
    private ArrayList<ColorAudio> availableCharacters;
    int numColors;
    int state;

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public ArrayList<ColorAudio> getTiradasPlayer() {
        return tiradasPlayer;
    }
    public int getState() {
        return state;
    }

    public ArrayList<ColorAudio> getTiradasMachine() {
        return tiradasMachine;
    }
    public void addCharacter(ColorAudio characterAudio) {
        availableCharacters.add(characterAudio);
    }

    public void setState(int state) {
        this.state = state;
    }


    public Game() {
        tiradasPlayer = new ArrayList<>();
        tiradasMachine = new ArrayList<>();
        availableCharacters = new ArrayList<>();

    }
    public void initGame(){
        tiradasPlayer.clear();
        tiradasMachine.clear();
        state = START;
        score = 0;
    }

    public boolean CompareColors() {
        if (tiradasPlayer.size() > tiradasMachine.size()) {
            return false;
        }

        for (int i = 0; i < tiradasPlayer.size(); i++) {
            ColorAudio playerMove = tiradasPlayer.get(i);
            ColorAudio machineMove = tiradasMachine.get(i);

            if (!movesMatch(playerMove, machineMove)) {
                return false;
            }
        }

        if (tiradasPlayer.size() == tiradasMachine.size()) {
            score++;
            return true;
        }

        return true;
    }

    private boolean movesMatch(ColorAudio playerMove, ColorAudio machineMove) {
        return playerMove.getImageId() == machineMove.getImageId() &&
                playerMove.getAudio() == machineMove.getAudio() &&
                playerMove.getColor() == machineMove.getColor();
    }

    public void play(){
        state = MACHINE;
        tiradasMachine.clear();
        tiradasPlayer.clear();
        score = 0;
    }

}
