package Modelo;

import webservice.CharacterColor;

public class ColorAudio {
    CharacterColor color;
    int audio;

    public ColorAudio(CharacterColor color, int audio) {
        this.color = color;
        this.audio = audio;
    }

    public CharacterColor getColor() {
        return color;
    }

    public int getAudio() {
        return audio;
    }
}
