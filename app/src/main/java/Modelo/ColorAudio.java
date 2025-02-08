package Modelo;

import webservice.CharacterColor;

public class ColorAudio {
    private Colors color;
    private int audio;
    private int imageId;

    public ColorAudio(Colors color, int audio, int imageId) {
        this.color = color;
        this.audio = audio;
        this.imageId = imageId;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Colors getColor() {
        return color;
    }

    public int getAudio() {
        return audio;
    }

    public int getImageId() {
        return imageId;
    }
}