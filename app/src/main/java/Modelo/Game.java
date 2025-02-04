package Modelo;

import java.util.ArrayList;

public class Game {
    public static final int START = 1, MACHINE = 2, PLAYER =3;
    ArrayList<ColorAudio> tiradasMachine;
    ArrayList<ColorAudio> tiradasPlayer;
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
    }
    public void initGame(){
        //el mismo objeto solo resetearlo
        tiradasPlayer.clear();
        tiradasMachine.clear();
        state = START;
        numColors = 2;
    }
    //PARA COMPARAR COLORES
    public boolean CompareColors() {
        //si todos son iguales true
        boolean soniguales = true;
        for (int i = 0; i < tiradasPlayer.size(); i++) {
            if (tiradasPlayer.get(i).getColor() != tiradasMachine.get(i).getColor()) {
                soniguales = false;
                break;
            }
        }
        return soniguales;
    }
}
