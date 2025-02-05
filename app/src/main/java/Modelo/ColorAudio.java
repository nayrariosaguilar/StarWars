package Modelo;

import webservice.CharacterColor;

public class ColorAudio {
    Colors color;
    int audio;

    public ColorAudio(Colors color, int audio) {
        this.color = color;
        this.audio = audio;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }

    public Colors getColor() {
        return color;
    }

    public int getAudio() {
        return audio;
    }
}
