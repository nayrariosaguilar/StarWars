package Modelo;

import java.util.ArrayList;
import java.util.Random;

import webservice.CharacterColor;

public class Game {
    public static final int START = 1, MACHINE = 2, PLAYER =3;
    public static final int ERROR = -1, OK_LIST_EQUALS = 1, OK_LIST_DIFFERENT = 2;
    ArrayList<ColorAudio> tiradasMachine;
    ArrayList<ColorAudio> tiradasPlayer;
    private ArrayList<ColorAudio> gameColors;
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
        gameColors = new ArrayList<>();
    }
    public void addColor(ColorAudio colorAudio) {
        gameColors.add(colorAudio);
    }
    public void initGame(){
        //el mismo objeto solo resetearlo
        tiradasPlayer.clear();
        tiradasMachine.clear();
        state = START;
        numColors = 2;
    }
    //PARA COMPARAR COLORES
    public int CompareColors() {
        int ret = OK_LIST_DIFFERENT;
        for (int i = 0; i < tiradasPlayer.size() && ret != ERROR; i++) {
            if (tiradasPlayer.get(i).getColor() !=
                    tiradasMachine.get(i).getColor()) {
                ret = ERROR;
            }/*else if(i == machineList.size() -1) { // Ultima comparacio machineList.size() -1
                ret = OK_LIST_EQUALS;
            }*/
        }
        if (tiradasMachine.size() == tiradasPlayer.size() && ret != ERROR) {
            ret = OK_LIST_EQUALS;
        }
        return ret;
    }
    public void nextLevel(CharacterColor characterColor) {
        state = MACHINE;
        tiradasPlayer.clear();
        Colors color = characterColor.getColor().equals("blue") ? Colors.BLUE : Colors.RED;
        int soundId = characterColor.getSoundId();
        tiradasMachine.add(new ColorAudio(color, soundId));
    }
    public void play(CharacterColor characterColor){
        state = MACHINE;
        tiradasMachine.clear();
        tiradasPlayer.clear();
        Colors color = characterColor.getColor().equals("blue") ? Colors.BLUE : Colors.RED;
        int soundId = characterColor.getSoundId();
        tiradasMachine.add(new ColorAudio(color, soundId));
    }

}
