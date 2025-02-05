package webservice;
import com.example.starwars.R;

public class CharacterColor {
    private String color;
    private String character;
    private int soundId;

    public String getColor() {
        return color;
    }
    public String getCharacter() {
        return character;
    }
    public int getSoundId() {
        return soundId;
    }
    // Setter Methods
    public void setColor(String color) {
        this.color = color;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public void determineSoundId() {
        switch (character) {
            case "C3PO":
                this.soundId = R.raw.c3po;
                break;
            case "Chewaka":
                this.soundId = R.raw.chewbacca;
                break;
            case "R2D2":
                this.soundId = R.raw.r2d2;
                break;
            case "Darth Vader":
                this.soundId = R.raw.darthvader;
                break;
            default:
                this.soundId = 0;
                break;
        }
    }

}
